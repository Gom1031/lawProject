package lawproject.LawProject.Config;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    // 인증이 실패했을 때 호출되는 메서드
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        
        // 클라이언트가 JSON 형식의 응답을 요구한 경우 (요청 헤더의 Content-Type이 application/json인 경우)
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            
            // 응답 상태를 UNAUTHORIZED(401)로 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            
            // 응답의 Content-Type을 application/json으로 설정
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            
            // 응답 본문에 인증 실패 이유를 작성
            response.getWriter().write(exception.getMessage());
        } else {
            // JSON 형식의 응답을 요구하지 않은 경우, "/login?error"로 리다이렉트
            response.sendRedirect("/login?error");
        }
    }
}
