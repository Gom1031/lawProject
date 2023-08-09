package lawproject.LawProject.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Spring Security에서 사용자 상세 정보를 제공하는 인터페이스
    @Autowired
    private UserDetailsService userDetailsService;

    // 인증 실패 시 핸들링하는 빈
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    // CustomAuthenticationFailureHandler 객체를 빈으로 등록
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    // 비밀번호 암호화를 위한 BCryptPasswordEncoder 객체를 빈으로 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 인증 메니저 빈을 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 인증 관련 설정 (인증 매니저 빌더를 사용하여 UserDetailsService와 PasswordEncoder를 설정)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // 웹 보안 설정 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests() // 요청에 대한 보안 검사
                // '/admin/'로 시작하는 요청은 'ADMIN' 권한을 가진 사용자만 접근 가능
                .antMatchers("/admin/**").hasRole("ADMIN")
                // 모든 사용자가 접근 가능한 경로   
                .antMatchers("/", "/main/**", "/user/register", "/user/login", "/consultboard/list", "/CSS/**", "/JS/**", "/images/**").permitAll()
                // 로그인한 사용자만 접근 가능한 경로
                .antMatchers("/consultboard/view/**", "/consultboard/write", "/consultboard/edit/**", "/search/**").authenticated()
                .anyRequest().authenticated() // 나머지 요청은 모두 인증된 사용자만 접근 가능
            .and()
            .formLogin() // 폼 기반 로그인 설정
                .loginPage("/user/login") // 로그인 페이지 경로
                .failureHandler(authenticationFailureHandler) // 로그인 실패 처리 핸들러
                .defaultSuccessUrl("/consultboard/list", true) // 로그인 성공 후 리다이렉트할 페이지
            .and()
            .logout().permitAll(); // 로그아웃 허용
    }
}
