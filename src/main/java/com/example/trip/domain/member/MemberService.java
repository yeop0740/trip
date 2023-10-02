package com.example.trip.domain.member;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.CreateMemberRequest;
import com.example.trip.domain.member.exception.DuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;


    /**
     * 회원 등록 서비스 메소드
     *
     * id 중복 체크
     * nickname 중복 체크
     *
     * @param request
     */
    public Member createMember(CreateMemberRequest request) throws DuplicateException {

        // id, nickname 체크
        duplicateIdAndNicknameCheck(request);

        // 패스워드 암호화
        String password = passwordEncoder.encode(request.getPassword());

        // 멤버 생성
        Member member = Member.builder()
                        .userId(request.getUserId())
                        .nickname(request.getNickname())
                        .password(password)
                        .imageUrl(request.getImgUrl())
                        .build();

        // 멤버 저장
        memberRepository.save(member);

        return member;
    }

    private void duplicateIdAndNicknameCheck(CreateMemberRequest request) throws DuplicateException {
        Map<String, String> errorMap = new HashMap<>();

        // id 중복 체크
        if(memberRepository.findByUserId(request.getUserId()) != null){
            errorMap.put("id", request.getUserId());
        }

        // nickname 중복 체크
        if(memberRepository.findByNickname(request.getNickname()) != null){
            errorMap.put("nickname", request.getNickname());
        }

        if(!errorMap.isEmpty()){
            throw new DuplicateException(errorMap);
        }
    }
}
