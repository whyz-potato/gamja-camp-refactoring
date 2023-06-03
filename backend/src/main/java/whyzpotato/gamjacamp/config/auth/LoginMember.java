package whyzpotato.gamjacamp.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 어노테이션이 생성될 수 있는 위치 : 메소드의 파라미터로 선언된 객체에만 허용
@Retention(RetentionPolicy.RUNTIME) // 라이프사이클 : 런타임까지
public @interface LoginMember {
}
