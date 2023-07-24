package lawproject.LawProject.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class userDTO {
    private Long userid;
    private String username;
    private String password;
    private String email;
    private String phone_number;
    private LocalDateTime regiester_date;
    private LocalDateTime last_edit_date;

    
}
