package lawproject.LawProject.Service;

import lawproject.LawProject.Model.User;
import lawproject.LawProject.Repository.userRepository;
import lawproject.LawProject.Entity.userEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // 이 클래스가 서비스 레이어의 컴포넌트임을 나타냄
public class customUserDetailsService implements UserDetailsService {

    @Autowired
    private userRepository userRepository; // userRepository 의존성 주입

    @Override // UserDetailsService 인터페이스의 메소드를 오버라이드 함
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름으로 사용자 엔티티를 찾음
        userEntity userEntity = userRepository.findByUsername(username);
        // 사용자 엔티티가 null 이라면 UsernameNotFoundException 예외 발생
        if (userEntity == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        // userEntity를 User로 변환 후 반환
        return userEntityToUser(userEntity);
    }
    
    // userEntity를 User 객체로 변환하는 private 메소드
    private User userEntityToUser(userEntity userEntity) {
        return new User(userEntity);
    }  
}


