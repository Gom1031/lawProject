package lawproject.LawProject.Controller;

import lawproject.LawProject.DTO.ErrorDTO;
import lawproject.LawProject.DTO.userDTO;
import lawproject.LawProject.Service.userService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
public class userRestController {

    // UserService와 AuthenticationManager에 대한 의존성을 주입
    @Autowired
    private userService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 사용자 로그인 API. UsernamePasswordAuthenticationToken을 사용하여 인증을 시도하고 성공하면 사용자 이름을 세션에 저장
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody userDTO userDto, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute("username", userDto.getUsername());
            // 성공하면 상태 코드 200과 메시지를 반환
            return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
        } catch (Exception e) {
            // 인증 실패하면 상태 코드 401과 오류 메시지를 반환
            ErrorDTO error = new ErrorDTO("Login failed. Invalid username or password.");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }

    // 사용자 등록 API. userService를 사용하여 사용자를 등록
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody userDTO userDto) {
        try {
            userService.registerUser(userDto);
            // 등록 성공하면 상태 코드 201과 메시지를 반환
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            // 등록 실패하면 상태 코드 400과 오류 메시지를 반환
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

