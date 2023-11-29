package com.example.trip.domain.location;

import com.example.trip.domain.location.dto.*;
import com.example.trip.domain.location.exception.DuplicateSaveException;
import com.example.trip.domain.location.exception.EmptyLocationException;
import com.example.trip.domain.location.exception.WrongLocationIdException;
import com.example.trip.domain.location.exception.WrongMemberException;
import com.example.trip.domain.location.repository.LocationPathRepository;
import com.example.trip.domain.location.repository.LocationRepository;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.annotation.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
@Tag(name = "Location", description = "위치 정보 관련 API")
public class LocationController {

    private final LocationService locationService;

    private final LocationPathRepository locationPathRepository;

    private final LocationRepository locationRepository;


    /**
     * 여행 경로 모음 이름 수정
     */
    @Operation(summary = "여행 경로 모음의 이름을 수정", description = "여행 경로 모음에 대한 이름을 수정합니다.")
    @PostMapping("/path/{pathId}")
    public BaseResponse updateLocationPathName(
            @Parameter(hidden = true) @Login Member member,
            @Parameter(description = "여행 경로 모음 번호(id)", in = ParameterIn.PATH) @PathVariable Long pathId,
            @Parameter(description = "여행 경로 모음 이름", in = ParameterIn.QUERY) @RequestParam("title") String title
    ) throws WrongMemberException {

        if(title != null && title.trim().length() == 0){
            throw new IllegalArgumentException("제목은 비어있거나 공백이면 안됩니다.");
        }


        locationService.updateLocationPathName(member, pathId, title);



        return new BaseResponse();
    }


    /**
     * 특정 지점에 대한 후기 남기기
     */
    @Operation(summary = "여행의 특정 지점에 후기 수정", description = "특정 여행 지점에 대한 후기를 수정합니다. 기본적으로 null값이 들어있습니다.")
    @PostMapping("/comment")
    public BaseResponse updateLocationComment(
            @Parameter(hidden = true) @Login Member member,
            @Validated @RequestBody UpdateLocationRequest updateLocationRequest,
            BindingResult bindingResult
    ) throws WrongMemberException, WrongLocationIdException {

        locationService.updateLocationComment(member, updateLocationRequest);


        return new BaseResponse();
    }


    /**
     * 회원의 여행 경로 목록 보기
     */
    @Operation(summary = "여행 경로 모음 가져오기", description = "특정 회원의 여행 경로 모음들의 목록을 가져옵니다.")
    @GetMapping("/list")
    public BaseResponse<List<GetLocationPathResponse>> getLocationPath(
            @Parameter(hidden = true) @Login Member member){

        List<GetLocationPathResponse> responseList = locationService.getLocationPath(member);

        return new BaseResponse<>(responseList);
    }


    /**
     * 회원의 특정 여행 경로 상세 보기
     */
    @Operation(summary = "여행 경로 가져오기", description = "특정 여행 경로 모음의 여행 경로들을 가져옵니다.")
    @GetMapping("/list/{pathId}")
    public BaseResponse<GetLocationInfoResponse> getLocation(
            @Parameter(hidden = true) @Login Member member,
            @Parameter(description = "여행 경로 모음 번호(id)", in = ParameterIn.PATH) @PathVariable Long pathId) throws WrongMemberException {

        GetLocationInfoResponse responseList = locationService.getLocation(member, pathId);

        return new BaseResponse<>(responseList);
    }

    /**
     * 경로 삭제는 특정 여행 경로에서의 특정 지점을 삭제
     */
    @Operation(summary = "특정 여행 경로들을 삭제", description = "특정 여행 경로들을 삭제할 수 있습니다.")
    @DeleteMapping("/list/{pathId}")
    public BaseResponse deleteLocation(
            @Parameter(hidden = true) @Login Member member,
            @Parameter(description = "여행 경로 모음 번호(id)", in = ParameterIn.PATH) @PathVariable Long pathId,
            @Validated @RequestBody DelLocationRequest delLocationRequest,
            BindingResult bindingResult) throws WrongMemberException, WrongLocationIdException {

        locationService.delLocation(member, pathId, delLocationRequest);

        return new BaseResponse();
    }

    /**
     * 경로 모음 삭제는 특정 회원의 특정 경로 모음을 삭제
     */
    @Operation(summary = "여행 경로 모음 삭제", description = "특정 회원의 여행 경로 모음을 삭제합니다.")
    @DeleteMapping("/{pathId}")
    public BaseResponse deleteLocationPath(
            @Parameter(hidden = true) @Login Member member,
            @Parameter(description = "여행 경로 모음 번호(id)", in = ParameterIn.PATH) @PathVariable Long pathId) throws WrongMemberException {

        locationService.delLocationPath(member, pathId);

        return new BaseResponse();
    }


    /**
     * 경로 수정은 Location Id를 가지고 해당 지점의 정보를 변경
     *
     * GPS 정보가 담겨 있는 Location Id 값을 받아 해당 Id 들을 묶어서 주요 지점으로 저장
     *
     * 한 번에 여러 값들을 입력 받을 수 있어야 한다. (여러 Location Id와 주요 지점의 이름 정보)
     * 또한, 리스트에 하나의 Location Id 값만이 들어올 수도 있다.
     */
    @Operation(summary = "특정 여행 경로들을 수정", description = "여행 경로들을 묶어서 주요 지점으로 설정 or 주요 지점의 이름을 변경합니다.")
    @PostMapping("/update")
    public BaseResponse updateLocation(
            @Validated @RequestBody LocationUpdateRequest locationUpdateRequest,
            BindingResult bindingResult,
            @Parameter(hidden = true) @Login Member member) throws WrongMemberException, EmptyLocationException {

        if(locationUpdateRequest.getLocationUpdateDataList().size() == 0){
            throw new EmptyLocationException();
        }

        if(!locationPathRepository.existsByIdAndMember(locationUpdateRequest.getLocationPathId(), member)){
            throw new WrongMemberException();
        }

        locationService.updateLocation(locationUpdateRequest);

        return new BaseResponse();
    }




    /**
     * 경로 저장에서는 단순하게 주요 지점에 대한 판단을 하고 DB에 저장한다.
     * 이미 저장되어 있는 정보가 들어왔는지는 체크한다. (가장 첫 시간 기준으로 해당 시간의 기록이 이미 있는지 확인한다.)
     *
     * GPS의 값을 소수점 4째자리까지만 사용한다 그 뒤는 절삭
     * 기본적으로 순차적으로 쌓인 값이 넘어온다고 생각 (시간순에 따른 정렬은 하지 않는다)
     * LinkedHashMap을 사용해서 GPS 값을 저장 및 가공한다.
     *
     * 오차 범위를 0.0001로 간주한다. (이론상 11.1m)
     * 최초 진입 지점으로 부터 +-0.0001 거리에 있는 위치는 같은 지점으로 간주한다.
     *
     * 같은 지점에 오래있었다면(2번 이상 GPS가 찍혔다면) 주요 지점으로 간주한다.
     *
     * 한 번에 넘어온 값은 하나의 여행 경로로서 간주한다.
     */
    @Operation(summary = "여행 경로 저장", description = "위치 정보에 대한 리스트를 가지고 여행 경로를 생성합니다.")
    @PostMapping("/save")
    public BaseResponse saveLocation(
            @Validated @RequestBody LocationSaveRequest locationSaveRequest,
            BindingResult bindingResult,
            @Parameter(hidden = true) @Login Member member) throws DuplicateSaveException {

        if (locationRepository.existsByStartTimeAndMember(
                locationSaveRequest.getLocationDataList().get(0).getLocalDateTime(), member)
        ) {
            throw new DuplicateSaveException();
        }


        Map<Position, PathTime> locationMap = new LinkedHashMap<Position, PathTime>();



        // 전체 GPS 값을 가지고 오차를 계산한 후, LiskedHashMap에 값을 넣는다.
        // 순차적으로 값을 넣되, 지정한 오차와 차이가 없는 경우에는 시간만을 업데이트 한다.
        for (LocationData locationData : locationSaveRequest.getLocationDataList()) {

            // 소수점 4자리 까지 남기고, 그 밑에는 절삭
            // 일반적으로 0.0001은 11.1m를 의미한다고 함.
            // 따라서 10미터 정도의 값이 같다면, 같은 지점에 있다고 설정 하기 위해 소수점 자리를 제한
            BigDecimal originLatitude = locationData.getLatitude().setScale(4, RoundingMode.DOWN);
            BigDecimal originLongitude = locationData.getLongitude().setScale(4, RoundingMode.DOWN);
            BigDecimal lossValue = new BigDecimal("0.0001");

            List<BigDecimal> latitudeLossList = new ArrayList<>();
            latitudeLossList.add(originLatitude.add(lossValue));
            latitudeLossList.add(originLatitude.subtract(lossValue));
            latitudeLossList.add(originLatitude);


            List<BigDecimal> longitudeLossList = new ArrayList<>();
            longitudeLossList.add(originLongitude.add(lossValue));
            longitudeLossList.add(originLongitude.subtract(lossValue));
            longitudeLossList.add(originLongitude);


            boolean addCheck = false;

            addCheck = duplicateCheck(locationMap, locationData, latitudeLossList, longitudeLossList, addCheck);

            if(!addCheck){
                Position position = Position.builder()
                        .latitude(originLatitude)
                        .longitude(originLongitude)
                        .build();

                PathTime pathTime = PathTime.builder()
                        .startTime(locationData.getLocalDateTime())
                        .endTime(locationData.getLocalDateTime())
                        .build();

                locationMap.put(position, pathTime);
            }
        }

        locationService.saveLocation(locationSaveRequest.getTitle(), locationMap, member);


        return new BaseResponse();
    }

    private static boolean duplicateCheck(Map<Position, PathTime> locationMap, LocationData locationData, List<BigDecimal> latitudeLossList, List<BigDecimal> longitudeLossList, boolean addCheck) {
        for (BigDecimal longitudeLoss : longitudeLossList) {

            if(addCheck) break;

            for (BigDecimal latitudeLoss : latitudeLossList) {
                Position position = Position.builder()
                        .latitude(latitudeLoss)
                        .longitude(longitudeLoss)
                        .build();

                if(addCheck) break;

                if (locationMap.containsKey(position)) {
                    PathTime pathTime = locationMap.get(position);
                    pathTime.updateTime(locationData.getLocalDateTime());

                    locationMap.put(position, pathTime);
                    addCheck = true;
                }
            }
        }
        return addCheck;
    }

}
