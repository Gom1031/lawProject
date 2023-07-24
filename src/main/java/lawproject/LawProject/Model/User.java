package lawproject.LawProject.Model;

import lawproject.LawProject.Entity.userEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class User implements UserDetails {

    private long userid;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDateTime registerDate;
    private LocalDateTime lastEditDate;

    public User(userEntity userEntity) {
        this.userid = userEntity.getUserid();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.email = userEntity.getEmail();
        this.phoneNumber = userEntity.getPhone_number();
        this.registerDate = userEntity.getRegister_date();
        this.lastEditDate = userEntity.getLast_edit_date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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
