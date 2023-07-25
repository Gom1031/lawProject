package lawproject.LawProject.Model;

import lawproject.LawProject.Entity.Role;
import lawproject.LawProject.Entity.userEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class User implements UserDetails {

    // 필드 선언
    private long userid;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDateTime registerDate;
    private LocalDateTime lastEditDate;
    private Role role;

    // userEntity로부터 User 객체를 생성하는 생성자
    public User(userEntity userEntity) {
        this.userid = userEntity.getUserid();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.email = userEntity.getEmail();
        this.phoneNumber = userEntity.getPhone_number();
        this.registerDate = userEntity.getRegister_date();
        this.lastEditDate = userEntity.getLast_edit_date();
        this.role = userEntity.getRole();
    }

    // 사용자의 권한을 반환하는 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
    
        if (this.role != null) {
            switch (this.role) {
                case USER:
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    break;
                case ADMIN:
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    break;
            }
        }
        
        return authorities;
    }

    // 사용자의 비밀번호를 반환하는 메소드
    @Override
    public String getPassword() {
        return this.password;
    }

    // 사용자의 이름을 반환하는 메소드
    @Override
    public String getUsername() {
        return this.username;
    }

    // 계정이 만료되지 않았는지 확인하는 메소드
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않은지 확인하는 메소드
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격이 만료되지 않았는지 확인하는 메소드
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화되어 있는지 확인하는 메소드
    @Override
    public boolean isEnabled() {
        return true;
    }
}