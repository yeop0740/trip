package com.example.trip.global;

import com.example.trip.global.exception.LoginInterceptorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 로그인 전에 체크하는 인터셉터
 *
 * 로그인을 체크하는 설정 정보는 WebConfig 파일에 저장되어 있다.
 * 세션이 생성되어 있는지 체크한 후, 생성된게 없으면 미인증 사용자라는 메시지와 함께 예외 호출
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        log.info("로그인 인터셉터={}", requestURI);

        // 세션 가져오기
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
            throw new LoginInterceptorException("미인증 사용자");
        }

        return true;
    }
}
