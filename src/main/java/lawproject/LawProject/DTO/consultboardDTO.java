package lawproject.LawProject.DTO;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class consultboardDTO {
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 45, message = "Title should not be greater than 45 characters")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    private String writer;
    private LocalDateTime date;
}
