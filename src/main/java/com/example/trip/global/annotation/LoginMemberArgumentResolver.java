package com.example.trip.global.annotation;

import com.example.trip.domain.member.MemberRepository;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.global.SessionConst;
import com.example.trip.global.exception.MissmatchIdException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;


public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {


    /**
     * @Login 애노테이션 있고, 해당 애노테이션이 붙은 파라미터의 타입이 Member.class를 상속 구현했다면,
     * 이 ArgumentResolver가 사용된다.
     *
     * @param parameter the method parameter to check
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        // 애노테이션이 사용되었을 때, @Login이 사용되었는지 체크
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        // 애노테이션이 사용되었을 때, 해당 애노테이션이 붙은 파라미터의 type을 체크
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;

    }

    /**
     * 컨트롤러 호출 직전에 호출되어 필요한 파라미터 정보를 생성한다.
     *
     * 여기서는 세션에 있는 로그인 회원 정보인 member 객체룰 찾아서 가져온다.
     *
     * @param parameter the method parameter to resolve. This parameter must
     * have previously been passed to {@link #supportsParameter} which must
     * have returned {@code true}.
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        HttpSession session = request.getSession(false);

        if(session == null){
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);

    }
}
