package lawproject.LawProject.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authz -> authz
                // 허용
                .antMatchers("/", "/main", "/user/register", "/user/login", "/consultboard/list", "/CSS/**", "/JS/**", "/images/**").permitAll()
                // 로그인시 허용
                .antMatchers("/consultboard/view/**", "/consultboard/write", "/consultboard/edit/**").authenticated()
                // 그 외 요청은 인증 필요
                .anyRequest().authenticated())
            .formLogin(formLogin -> formLogin
                .loginPage("/user/login")
                // 로그인 성공시 리다이렉트
                .defaultSuccessUrl("/consultboard/list", true))
            .logout(logout -> logout.permitAll());
        return http.build();
    }

}
