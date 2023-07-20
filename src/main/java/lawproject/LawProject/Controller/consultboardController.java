package lawproject.LawProject.Controller;

import lawproject.LawProject.DTO.consultboardDTO;
import lawproject.LawProject.Entity.consultboardEntity;
import lawproject.LawProject.Mapper.consultboardMapper;
import lawproject.LawProject.Service.consultboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/consultboard")
public class consultboardController {

    @Autowired
    private consultboardService consultboardService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("consultboards", consultboardService.findAll());
        return "main_consultboard";
    }

    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("consultboard", new consultboardDTO());
        return "main_consultwrite";
    }

    @PostMapping("/post")
    public String writeSubmit(@ModelAttribute consultboardDTO consultboardDto) {
        consultboardEntity consultboard = consultboardMapper.INSTANCE.dtoToEntity(consultboardDto);
        consultboardService.save(consultboard);
        return "redirect:/consultboard/list";
    }
}
