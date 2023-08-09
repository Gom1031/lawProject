package lawproject.LawProject.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainController {
    
    @GetMapping("")
    public String mainForm() {
        return "homepage";
    }
    
    @GetMapping("/teams")
    public String teamsForm() {
        return "teams";
    }
    
    @GetMapping("/win_cases")
    public String winCasesForm() {
        return "win_cases";
    }

    @GetMapping("/about")
    public String aboutForm() {
        return "aboutpage";
    }
}
