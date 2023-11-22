package com.example.trip.domain.member;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.*;
import com.example.trip.domain.member.exception.*;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.ErrorMapCreator;
import com.example.trip.global.SessionConst;
import com.example.trip.global.annotation.Login;
import com.example.trip.global.exception.RequestFieldException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;


    /**
     * 회원 등록 메소드
     *
     * 회원 등록과 동시에 로그인 처리 즉, 세션 연결이 되어야 함.
     * 세션에 (LOGIN_MEMBER, 회원 번호(id))을 저장
     *
     * @param createMemberRequest
     * @param bindingResult
     * @return
     */
    @Operation(summary = "회원 생성", description = "회원 정보를 가지고 회원을 생성하고, 세션 연결을 합니다.")
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
        session.setAttribute(SessionConst.LOGIN_MEMBER, createdMember);

        return new BaseResponse();
    }


    /**
     * 로그인 메소드
     *
     * 아이디와 비밀번호 확인 후, 로그인 처리 => 세션 연결
     * 탈퇴 되었는지 역시 확인 해야 된다.
     *
     *
     * @param loginMemberRequest
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @Operation(summary = "로그인", description = "id, password 정보를 가지고 로그인을 합니다. (세션 연결)")
    @PostMapping("/login")
    public BaseResponse loginMember(@Validated @RequestBody LoginMemberRequest loginMemberRequest,
                                    BindingResult bindingResult,
                                    HttpServletRequest httpServletRequest) throws RequestFieldException, EmptyUserException, PasswordMissException, FireMemberException {

        // 필드 오류 체크
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = ErrorMapCreator.fieldErrorToMap(bindingResult.getFieldErrors());
            throw new RequestFieldException(errorMap);
        }

        // 아이디, 비밀번호를 서비스 단에서 확인
        Member member = memberService.loginMember(loginMemberRequest);

        // 세션 생성
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return new BaseResponse();
    }


    /**
     * 회원 상세 정보 가져오기 메서드
     *
     * 스크랩 개수 및 게시물 개수도 가져와야 한다.
     *
     * @param member
     * @return
     */
    @Operation(summary = "회원 상세 정보 가져오기", description = "해당 회원의 스크랩 개수 및 게시물 개수등을 가져옵니다.")
    @GetMapping("/info")
    public BaseResponse<GetMemberInfoResponse> getMemberInfo(@Parameter(hidden = true) @Login Member member){

        GetMemberInfoResponse getMemberInfoResponse = memberService.getMemberInfo(member);

        return new BaseResponse<>(getMemberInfoResponse);
    }


    /**
     * 회원 삭제 메소드
     *
     * 삭제 시 탈퇴 여부에 체크한다.
     * 이후, 로그인이 안되게끔 처리
     *
     * @param member
     * @param request
     * @return
     */
    @Operation(summary = "회원 삭제", description = "특정 회원을 삭제처리 합니다.")
    @DeleteMapping("/delete")
    public BaseResponse deleteMember(
            @Parameter(hidden = true) @Login Member member,
            HttpServletRequest request){

        // 탈퇴 로직 호출
        memberService.deleteMember(member);

        // 세션 삭제
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }
        
        return new BaseResponse();
    }


    /**
     * 회원 수정 메서드
     *
     * 이름, 이미지 url, id, password를 입력받아 수정
     */
    @Operation(summary = "회원 수정", description = "회원 정보를 가지고 정보를 수정합니다.")
    @PostMapping("/update")
    public BaseResponse updateMember(
            @Parameter(hidden = true) @Login Member member,
            @Validated @RequestBody UpdateMemberRequest updateMemberRequest,
            BindingResult bindingResult) throws RequestFieldException, DuplicateException {

        // 필드 오류 체크
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = ErrorMapCreator.fieldErrorToMap(bindingResult.getFieldErrors());
            throw new RequestFieldException(errorMap);
        }

        memberService.updateMember(member, updateMemberRequest);

        return new BaseResponse();
    }


    /**
     * 로그아웃 메서드
     *
     * 세션 해제
     */
    @Operation(summary = "로그아웃", description = "로그아웃 처리를 합니다.")
    @GetMapping("/logout")
    public BaseResponse logout(
            @Parameter(hidden = true) @Login Member member,
            HttpServletRequest httpServletRequest){

        HttpSession session = httpServletRequest.getSession(false);


        session.invalidate();

        return new BaseResponse();
    }




}
