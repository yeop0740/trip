package com.example.trip.domain.member;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.CreateMemberRequest;
import com.example.trip.domain.member.dto.GetMemberInfoResponse;
import com.example.trip.domain.member.dto.LoginMemberRequest;
import com.example.trip.domain.member.dto.UpdateMemberRequest;
import com.example.trip.domain.member.exception.DuplicateException;
import com.example.trip.domain.member.exception.EmptyUserException;
import com.example.trip.domain.member.exception.FireMemberException;
import com.example.trip.domain.member.exception.PasswordMissException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
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
        duplicateIdAndNicknameCheck(request.getUserId(), request.getNickname());

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
     * @throws DuplicateException
     */
    private void duplicateIdAndNicknameCheck(String userId, String nickname) throws DuplicateException {
        Map<String, String> errorMap = new HashMap<>();

        // id 중복 체크
        if(memberRepository.findByUserId(userId) != null){
            errorMap.put("id", userId);
        }

        // nickname 중복 체크
        if(memberRepository.findByNickname(nickname) != null){
            errorMap.put("nickname", nickname);
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
    public Member loginMember(LoginMemberRequest loginMemberRequest) throws PasswordMissException, EmptyUserException, FireMemberException {

        Member member = memberRepository.findByUserId(loginMemberRequest.getUserId());

        // 해당 id가 존재하지 않을 경우 체크
        if(member == null){
            throw new EmptyUserException(new HashMap<String, String>().put("userId", loginMemberRequest.getUserId()));
        }

        // 해당 id가 탈퇴되었는지 체크
        System.out.println("member = " + member.getStatus());
        if(member.getStatus()){
            throw new FireMemberException("탈퇴된 회원 정보입니다.");
        }


        // 비밀번호가 일치하지 않을 경우 체크
        boolean matches = passwordEncoder.matches(loginMemberRequest.getPassword(), member.getPassword());

        if(!matches){
            throw new PasswordMissException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    /**
     * 회원 상세 정보 가져오기
     *
     * scrap, post 개수를 셀 때, 아예 아무 것도 안한 경우를 대비해 따로 query를 날린다.
     *
     * @param member
     * @return
     */
    public GetMemberInfoResponse getMemberInfo(Member member) {

        Integer scrapCnt = memberRepository.findScrapCnt(member);
        Integer postCnt = memberRepository.findPostCnt(member);

        return GetMemberInfoResponse.builder()
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .imgUrl(member.getImageUrl())
                .postCnt(postCnt)
                .scrapCnt(scrapCnt)
                .build();


    }

    /**
     * 회원 삭제 메소드
     *
     * @param member
     */
    public void deleteMember(Member member) {

        member.deleteMember();
        memberRepository.save(member);
    }


    /**
     * 회원 수정 메소드
     *
     * @param member
     * @param request
     */
    public void updateMember(Member member, UpdateMemberRequest request) throws DuplicateException {

        // id, nickname 체크
        duplicateIdAndNicknameCheck(request.getUserId(), request.getNickname());

        // 비밀번호 암호화
        String password = passwordEncoder.encode(request.getPassword());

        // 수정
        member.updateMember(request.getUserId(),
                            password,
                            request.getNickname(),
                            request.getImgUrl());

        memberRepository.save(member);
    }
}
