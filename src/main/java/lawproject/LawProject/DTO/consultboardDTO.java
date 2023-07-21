package lawproject.LawProject.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class consultboardDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime date;
}
