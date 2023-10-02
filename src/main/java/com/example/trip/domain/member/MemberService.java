package com.example.trip.domain.member;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.CreateMemberRequest;
import com.example.trip.domain.member.dto.LoginMemberRequest;
import com.example.trip.domain.member.exception.DuplicateException;
import com.example.trip.domain.member.exception.EmptyUserException;
import com.example.trip.domain.member.exception.PasswordMissException;
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

    /**
     * 유저 아이디, 이메일을 체크해서 중복이 있으면 예외를 날리는 메소드
     *
     * @param request
     * @throws DuplicateException
     */
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


    /**
     * 로그인 수행을 위해 해당 계정이 존재하는 계정인지 체크
     *
     * @param loginMemberRequest
     * @return
     */
    public Member loginMember(LoginMemberRequest loginMemberRequest) throws PasswordMissException, EmptyUserException {

        Member member = memberRepository.findByUserId(loginMemberRequest.getUserId());

        // 해당 id가 존재하지 않을 경우 체크
        if(member == null){
            throw new EmptyUserException(new HashMap<String, String>().put("userId", loginMemberRequest.getUserId()));
        }

        // 비밀번호가 일치하지 않을 경우 체크
        boolean matches = passwordEncoder.matches(loginMemberRequest.getPassword(), member.getPassword());

        if(!matches){
            throw new PasswordMissException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }
}
