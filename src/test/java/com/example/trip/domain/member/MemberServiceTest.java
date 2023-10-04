package com.example.trip.domain.member;

import com.example.trip.domain.interaction.InteractionRepository;
import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.interaction.domain.InteractionType;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.CreateMemberRequest;
import com.example.trip.domain.member.dto.GetMemberInfoResponse;
import com.example.trip.domain.member.dto.LoginMemberRequest;
import com.example.trip.domain.member.dto.UpdateMemberRequest;
import com.example.trip.domain.member.exception.DuplicateException;
import com.example.trip.domain.member.exception.EmptyUserException;
import com.example.trip.domain.member.exception.FireMemberException;
import com.example.trip.domain.member.exception.PasswordMissException;
import com.example.trip.domain.post.PostRepository;
import com.example.trip.domain.post.domain.Post;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    InteractionRepository interactionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * member 10개 임시 생성
     * @throws DuplicateException
     */
    @BeforeEach
    void setMember() throws DuplicateException {
        int testSize = 10;

        List<Member> memberList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        List<Interaction> interactionList = new ArrayList<>();

        for(int i = 1; i <= testSize; i++){


            Member member = Member.builder()
                    .userId("testId" + i)
                    .nickname("testNick" + i)
                    .password(passwordEncoder.encode("testPassword" + i))
                    .imageUrl("test_img")
                    .build();

            memberList.add(member);
        }
        memberRepository.saveAll(memberList);


        for(int i = 1; i <= testSize; i++){
            Post post = Post.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .member(memberList.get(i - 1))
                    .build();

            postList.add(post);
        }
        postRepository.saveAll(postList);


        for(int i = 1; i <= testSize; i++){
            Interaction interaction = Interaction.builder()
                    .type(InteractionType.SCRAP)
                    .member(memberList.get(i - 1))
                    .post(postList.get(i - 1))
                    .build();

            interactionList.add(interaction);
        }
        interactionRepository.saveAll(interactionList);

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
    void loginMember() throws EmptyUserException, PasswordMissException, FireMemberException {

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


    /**
     * - scrap, post 개수 체크
     */
    @Test
    @DisplayName("맴버 정보 가져오기 테스트")
    void getMemberInfo(){

        // 아무 조작 안했을 때, 1개씩 찍힌다.
        List<Member> all = memberRepository.findAll();
        for (Member member : all) {
            GetMemberInfoResponse memberInfo = memberService.getMemberInfo(member);
            org.assertj.core.api.Assertions.assertThat(memberInfo.getScrapCnt()).isEqualTo(1);
            org.assertj.core.api.Assertions.assertThat(memberInfo.getPostCnt()).isEqualTo(1);
        }

        // 첫 번째 member에 post와 scrap 3개 추가
        Member m = all.get(0);

        for(int i = 1; i <= 3; i++){
            Post p = Post.builder()
                    .member(m)
                    .content("content999" + i)
                    .title("title999" + i)
                    .build();

            postRepository.save(p);

            Interaction ic = Interaction.builder()
                    .post(p)
                    .member(m)
                    .type(InteractionType.SCRAP)
                    .build();
            interactionRepository.save(ic);
        }

        GetMemberInfoResponse memberInfo = memberService.getMemberInfo(m);
        org.assertj.core.api.Assertions.assertThat(memberInfo.getScrapCnt()).isEqualTo(4);
        org.assertj.core.api.Assertions.assertThat(memberInfo.getPostCnt()).isEqualTo(4);
    }


    @Test
    @DisplayName("회원 삭제 테스트")
    void delMember() throws EmptyUserException, FireMemberException, PasswordMissException {

        // 특정 회원 삭제
        List<Member> all = memberRepository.findAll();
        Member member = all.get(0);

        memberService.deleteMember(member);


        // 로그인에서 예외가 발생하는지 확인
        LoginMemberRequest request = LoginMemberRequest.builder()
                .userId("testId1")
                .password("testPassword1")
                .build();

        Assertions.assertThrows(FireMemberException.class, ()->{
            memberService.loginMember(request);
        });
    }

    @Test
    @DisplayName("회원 수정 테스트")
    void updateTest() throws DuplicateException {


        // 특정 회원 가져오기
        List<Member> all = memberRepository.findAll();
        Member member = all.get(0);


        // id 중복 체크 수행 로직
        UpdateMemberRequest duRequest = UpdateMemberRequest.builder()
                .userId("testId2")
                .nickname("testNick2")
                .password("testPassword2")
                .imgUrl("test_img")
                .build();

        Assertions.assertThrows(DuplicateException.class, ()->{
            memberService.updateMember(member, duRequest);
        });



        // 정상 수행 로직
        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .userId("testId999")
                .nickname("testNick999")
                .password("testPassword999")
                .imgUrl("test_img")
                .build();

        memberService.updateMember(member, request);
    }


    @Test
    @DisplayName("회원 검색 테스트")
    void searchMember(){

        org.assertj.core.api.Assertions.assertThat(memberService.searchMember("test").isEmpty()).isFalse();


    }


}