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

    private long userid;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDateTime registerDate;
    private LocalDateTime lastEditDate;
    private Role role;

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

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
