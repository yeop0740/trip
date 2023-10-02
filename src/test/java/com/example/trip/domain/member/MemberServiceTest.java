package com.example.trip.domain.member;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.CreateMemberRequest;
import com.example.trip.domain.member.dto.LoginMemberRequest;
import com.example.trip.domain.member.exception.DuplicateException;
import com.example.trip.domain.member.exception.EmptyUserException;
import com.example.trip.domain.member.exception.PasswordMissException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    /**
     * member 10개 임시 생성
     * @throws DuplicateException
     */
    @BeforeEach
    void setMember() throws DuplicateException {
        int testSize = 10;

        for(int i = 1; i <= testSize; i++){
            CreateMemberRequest request = CreateMemberRequest.builder()
                    .userId("testId" + i)
                    .nickname("testNick" + i)
                    .password("testPassword" + i)
                    .imgUrl("test_img")
                    .build();

            memberService.createMember(request);
        }
    }

    /**
     * 한개 테스트가 끝났다면 모든 멤버 삭제
     */
    @AfterEach
    void clearMember(){
        memberRepository.deleteAll();
    }



    /**
     * - 닉네임, id 중복 체크
     * - password 변환 체크
     */
    @Test
    @DisplayName("맴버 생성 테스트")
    void createMember() throws DuplicateException {

        // 중복 체크
        // 이미 있는 member 가져오기
        CreateMemberRequest request = CreateMemberRequest.builder()
                .userId("testId1")
                .nickname("testNick1")
                .password("testPassword1")
                .imgUrl("test_img")
                .build();


        Assertions.assertThrows(DuplicateException.class,
                ()->{
                    memberService.createMember(request);
                });

        // 비밀번호 변환 체크
        Member member = memberRepository.findByUserId(request.getUserId());
        org.assertj.core.api.Assertions.assertThat(member.getPassword()).isNotEqualTo(request.getPassword());
    }


    /**
     *  - 없는 id 체크
     *  - 비밀번호 일치하는지 체크
     */
    @Test
    @DisplayName("로그인 테스트")
    void loginMember() throws EmptyUserException, PasswordMissException {

        // 생성하지 않은 정보의 request 생성
        LoginMemberRequest request = LoginMemberRequest.builder()
                .userId("test999")
                .password("password999")
                .build();

        // 없는 id 인지 체크
        Assertions.assertThrows(EmptyUserException.class, ()->{
           memberService.loginMember(request);
        });


        // 비밀번호가 일치하는지 체크
        LoginMemberRequest psRequest = LoginMemberRequest.builder()
                .userId("testId1")
                .password("testPassword")
                .build();

        Assertions.assertThrows(PasswordMissException.class, ()->{
           memberService.loginMember(psRequest);
        });


        // 정상 동작 체크
        LoginMemberRequest successRequest = LoginMemberRequest.builder()
                .userId("testId1")
                .password("testPassword1")
                .build();
        memberService.loginMember(successRequest);
    }
}