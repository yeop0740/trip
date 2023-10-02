package com.example.trip.domain.member;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.CreateMemberRequest;
import com.example.trip.domain.member.dto.LoginMemberRequest;
import com.example.trip.domain.member.exception.DuplicateException;
import com.example.trip.domain.member.exception.EmptyUserException;
import com.example.trip.domain.member.exception.PasswordMissException;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.ErrorMapCreator;
import com.example.trip.global.SessionConst;
import com.example.trip.global.exception.RequestFieldException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    /**
     * 회원 등록 컨트롤러 메소드
     *
     * 회원 등록과 동시에 로그인 처리 즉, 세션 연결이 되어야 함.
     * 세션에 (LOGIN_MEMBER, 회원 번호(id))을 저장
     *
     * @param createMemberRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/create")
    public BaseResponse createMember(@Validated @RequestBody CreateMemberRequest createMemberRequest,
                                     BindingResult bindingResult,
                                     HttpServletRequest request) throws DuplicateException, RequestFieldException {

        // 필드 오류 체크
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = ErrorMapCreator.fieldErrorToMap(bindingResult.getFieldErrors());
            throw new RequestFieldException(errorMap);
        }

        // 서비스 로직
        Member createdMember = memberService.createMember(createMemberRequest);

        // 회원 생성 성공
        // 세션 생성 + 세션에 id값 넣기
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, createdMember.getId());

        return BaseResponse.builder()
                .status(OK)
                .message("성공")
                .build();
    }


    /**
     * 로그인 컨트롤러 메소드
     *
     * 아이디와 비밀번호 확인 후, 로그인 처리
     * 즉, 세션 연결
     *
     * @param loginMemberRequest
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse loginMember(@Validated @RequestBody LoginMemberRequest loginMemberRequest,
                                    BindingResult bindingResult,
                                    HttpServletRequest httpServletRequest) throws RequestFieldException, EmptyUserException, PasswordMissException {

        // 필드 오류 체크
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = ErrorMapCreator.fieldErrorToMap(bindingResult.getFieldErrors());
            throw new RequestFieldException(errorMap);
        }

        // 아이디, 비밀번호를 서비스 단에서 확인
        Member member = memberService.loginMember(loginMemberRequest);

        // 세션 생성
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member.getId());

        return BaseResponse.builder()
                .status(OK)
                .message("성공")
                .build();
    }




}
