package whyzpotato.gamjacamp.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;

import javax.servlet.http.HttpSession;

/***
 * Argument Resolver : 컨트롤러 메서드의 특정 파라미터를 원하는 객체로 만들어 바인딩한다.
 */

@RequiredArgsConstructor
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    /**
     * @param : 컨트롤러의 파라미터
     * @return : 해당 파라미터를 지원하는지 판단한다.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterAnnotation(LoginMember.class) != null) //파라미터에 @LoginMember 어노테이션이 붙어 있는지 체크
                && SessionMember.class.equals(parameter.getParameterType());  //실제로 로그인 한 유저인지 체크
    }

    /**
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return : 파라미터에 전달할 객체 (바인딩한 객체)
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("member");
    }

}
