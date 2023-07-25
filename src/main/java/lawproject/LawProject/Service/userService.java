package lawproject.LawProject.Service;

import lawproject.LawProject.DTO.userDTO;
import lawproject.LawProject.Entity.userEntity;
import lawproject.LawProject.Repository.userRepository;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service // 이 클래스가 서비스 레이어의 컴포넌트임을 나타냄
public class userService {

    @Autowired // userRepository 의존성 주입
    private userRepository userRepository;

    // BCryptPasswordEncoder 객체 생성, 비밀번호 암호화를 위해 사용됨
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 사용자 등록 메서드
    public void registerUser(userDTO userDto) {
        // 사용자 이름이 이미 존재하는 경우 RuntimeException 발생
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
    
        // userEntity 객체 생성 및 초기화
        userEntity user = new userEntity();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // 비밀번호 암호화 후 설정
        user.setEmail(userDto.getEmail());
        user.setPhone_number(userDto.getPhone_number());
    
        // userDto로부터 Role 설정
        user.setRole(userDto.getRole());
    
        // 등록 날짜와 마지막 수정 날짜 설정
        user.setRegister_date(LocalDateTime.now());
        user.setLast_edit_date(LocalDateTime.now());
        userRepository.save(user); // 저장소에 user 저장
    }

    // 사용자 로그인 메서드
    public boolean loginUser(userDTO userDto) {
        // 사용자 이름으로 userEntity 검색
        userEntity user = userRepository.findByUsername(userDto.getUsername());
        // 사용자가 존재하며 비밀번호가 일치하면 true 반환, 그렇지 않으면 false 반환
        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    // 사용자 이름으로 사용자 검색 메서드
    public userEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
