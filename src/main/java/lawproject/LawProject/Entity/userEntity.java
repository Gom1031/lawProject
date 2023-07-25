package lawproject.LawProject.Entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user")
@Getter
@Setter
public class userEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userid;
    private String username;
    private String password;
    private String email;
    private String phone_number;
    private LocalDateTime register_date;
    private LocalDateTime last_edit_date;

    @Enumerated(EnumType.STRING)
    private Role role; // Here you need to import your custom Role enum

    @Transient  // DB에 없음을 알려줌
    private String formatted_register_Date;
    @Transient  // DB에 없음을 알려줌
    private String formatted_last_edit_date;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.register_date = now;
        this.last_edit_date = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.last_edit_date = LocalDateTime.now();
    }
}
