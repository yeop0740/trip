package com.example.trip.domain.member;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.CreateMemberRequest;
import com.example.trip.domain.member.exception.DuplicateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberControllerTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    /**
     * - 닉네임, id 중복 체크
     * - password 변환 체크
     */
    @Test
    @DisplayName("맴버 생성 테스트")
    void createMember() throws DuplicateException {

        int testSize = 10;

        List<CreateMemberRequest> requestList = new ArrayList<>();

        for(int i = 1; i <= testSize; i++){
            CreateMemberRequest request = CreateMemberRequest.builder()
                    .userId("testId" + i)
                    .nickname("testNick" + i)
                    .password("testPassword" + i)
                    .imgUrl("test_img")
                    .build();

            memberService.createMember(request);
            requestList.add(request);
        }

        // 중복 체크
        Assertions.assertThrows(DuplicateException.class,
                ()->{
                    memberService.createMember(requestList.get(0));
                });

        // 비밀번호 변환 체크
        Member member = memberRepository.findByUserId(requestList.get(0).getUserId());
        org.assertj.core.api.Assertions.assertThat(member.getPassword()).isNotEqualTo(requestList.get(0).getPassword());

    }


}