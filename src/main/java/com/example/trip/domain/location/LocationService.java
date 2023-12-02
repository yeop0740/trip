package com.example.trip.domain.location;

import com.example.trip.domain.location.domain.Location;
import com.example.trip.domain.location.domain.LocationPath;
import com.example.trip.domain.location.dto.*;
import com.example.trip.domain.location.exception.WrongLocationIdException;
import com.example.trip.domain.location.exception.WrongMemberException;
import com.example.trip.domain.location.repository.LocationPathRepository;
import com.example.trip.domain.location.repository.LocationRepository;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {


    //private final RestTemplate restTemplate;
    private final LocationRepository locationRepository;
    private final LocationPathRepository locationPathRepository;

    private final PostService postService;

    public void saveLocation(String title, Map<Position, PathTime> locationMap, Member member) {

        // 여행 경로 모음 생성
        LocationPath locationPath = LocationPath.builder()
                .title(title)
                .member(member)
                .build();

        // 앞서 순차적으로 저장한 위치 정보들을 가지고 Location 형태로 가공해서 DB에 넣는다.
        List<Location> locationList = new ArrayList<>();

        for (Position position : locationMap.keySet()) {

            LocalDateTime startTime = locationMap.get(position).getStartTime();
            LocalDateTime endTime = locationMap.get(position).getEndTime();
            boolean isImportant = false;
            Location location = null;


            // 주요 지점이라 판단 된 곳이라면, 주요 지점 체크하자
            if(startTime != null && endTime != null && !startTime.isEqual(endTime)) {
                isImportant = true;

                location = Location.builder()
                        .latitude(position.getLatitude())
                        .longitude(position.getLongitude())
                        .address("주요지점")
                        .startTime(locationMap.get(position).getStartTime())
                        .endTime(locationMap.get(position).getEndTime())
                        .isImportant(isImportant)
                        .build();
            }
            else{
                location = Location.builder()
                        .latitude(position.getLatitude())
                        .longitude(position.getLongitude())
                        .address("이동")
                        .startTime(locationMap.get(position).getStartTime())
                        .endTime(locationMap.get(position).getEndTime())
                        .isImportant(isImportant)
                        .build();
            }


            location.setLocationPath(locationPath);

            locationList.add(location);
        }

        locationPathRepository.save(locationPath);
        locationRepository.saveAll(locationList);

    }


    /**
     * Location Id의 리스트를 가지고 처음 진입 지점만 남기고 나머지 삭제
     * 이 때, 처음 진입 지점에 이름(주소) 및 시간(나간 시간) 업데이트
     */
    public void updateLocation(LocationUpdateRequest locationUpdateRequest) {

        for (LocationUpdateData locationUpdateData : locationUpdateRequest.getLocationUpdateDataList()) {

            List<Long> locationIdList = locationUpdateData.getLocationIdList();

            Optional<Location> findOption = locationRepository.findById(locationIdList.get(0));
            Optional<Location> findLastOption = locationRepository.findById(locationIdList.get(locationIdList.size() - 1));

            if(findOption.isPresent() && findLastOption.isPresent()){
                Location findLocation = findOption.get();
                Location findLastLocation = findLastOption.get();

                findLocation.updateLocation(locationUpdateData.getAddress(), findLastLocation.getEndTime());

            }

            for(int i = 1; i < locationIdList.size(); i++){
                locationRepository.deleteById(locationIdList.get(i));
            }

        }

    }

    public List<GetLocationPathResponse> getLocationPath(Member member) {

        List<GetLocationPathResponse> getLocationPathResponseList = new ArrayList<>();

        List<LocationPath> findPath = locationPathRepository.findAllByMember(member);

        for (LocationPath locationPath : findPath) {
            LocalDateTime startTime = locationRepository.findLocationPathStartTime(locationPath);
            LocalDateTime endTime = locationRepository.findLocationPathEndTime(locationPath);

            getLocationPathResponseList.add(GetLocationPathResponse.builder()
                            .id(locationPath.getId())
                            .title(locationPath.getTitle())
                            .startTime(startTime)
                            .endTime(endTime)
                            .build());
        }



        return getLocationPathResponseList;
    }

    public GetLocationInfoResponse getLocation(Member member, Long pathId) throws WrongMemberException {

        if(!locationPathRepository.existsByIdAndMember(pathId, member)){
            throw new WrongMemberException();
        }

        LocationPath locationPath = locationPathRepository.findById(pathId).get();
        List<Location> locationList = locationRepository.findAllByLocationPath(locationPath);

        List<LocationInfo> locationInfoList = new ArrayList<>();

        for (Location location : locationList) {
            locationInfoList.add(LocationInfo.builder()
                            .id(location.getId())
                            .address(location.getAddress())
                            .latitude(location.getLatitude())
                            .longitude(location.getLongitude())
                            .startTime(location.getStartTime())
                            .endTime(location.getEndTime())
                            .isImportant(location.isImportant())
                            .comment(location.getComment())
                            .build());
        }


        return new GetLocationInfoResponse(locationInfoList);
    }

    public void delLocationPath(Member member, Long pathId) throws WrongMemberException {

        if(!locationPathRepository.existsByIdAndMember(pathId, member)){
            throw new WrongMemberException();
        }

        LocationPath findLocationPath = locationPathRepository.findById(pathId).get();

        if(findLocationPath.getPost() != null){
            postService.deletePost(member.getId(), findLocationPath.getPost().getId());
        }



        locationPathRepository.deleteById(pathId);

    }

    // 없는게 하나라도 있으면, 삭제 x
    public void delLocation(Member member, Long pathId, DelLocationRequest delLocationRequest) throws WrongMemberException, WrongLocationIdException {

        if(!locationPathRepository.existsByIdAndMember(pathId, member)){
            throw new WrongMemberException();
        }

        for (Long locationId : delLocationRequest.getLocationIdList()) {

            if(!locationRepository.existsById(locationId)){
                throw new WrongLocationIdException();
            }
        }

        for (Long locationId : delLocationRequest.getLocationIdList()) {
            locationRepository.deleteById(locationId);
        }

    }

    public void updateLocationComment(Member member, UpdateLocationRequest updateLocationRequest) throws WrongMemberException, WrongLocationIdException {

        if(!locationPathRepository.existsByIdAndMember(updateLocationRequest.getLocationPathId(), member)){
            throw new WrongMemberException();
        }

        if(!locationRepository.existsById(updateLocationRequest.getLocationId())){
            throw new WrongLocationIdException();
        }

        Location findLocation = locationRepository.findById(updateLocationRequest.getLocationId()).get();

        findLocation.setComment(updateLocationRequest.getComment());
    }

    public void updateLocationPathName(Member member, Long pathId, String title) throws WrongMemberException {

        if(!locationPathRepository.existsByIdAndMember(pathId, member)){
            throw new WrongMemberException();
        }

        LocationPath findLocationPath = locationPathRepository.findById(pathId).get();

        findLocationPath.setTitle(title);
    }
}
