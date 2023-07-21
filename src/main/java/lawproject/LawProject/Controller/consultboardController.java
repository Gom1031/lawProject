package lawproject.LawProject.Controller;

import lawproject.LawProject.DTO.consultboardDTO;
import lawproject.LawProject.Entity.consultboardEntity;
import lawproject.LawProject.Mapper.consultboardMapper;
import lawproject.LawProject.Service.consultboardService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/consultboard")
public class consultboardController {

    @Autowired
    private consultboardService consultboardService;

    @Autowired
    private consultboardMapper consultboardMapper;

    @GetMapping("/list")
    public String list(Model model) {
        List<consultboardEntity> consultboards = consultboardService.findAll();
        consultboards.forEach(consultboard -> {
            String formattedDate = consultboardService.formatDateToKorean(consultboard.getDate());
            consultboard.setFormattedDate(formattedDate);
        });
        model.addAttribute("consultboards", consultboards);
        return "main_consultboard";
    }

    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("consultboard", new consultboardDTO());
        return "main_consultwrite";
    }

    @PostMapping("/post")
    public String writeSubmit(@ModelAttribute consultboardDTO consultboardDto) {
        consultboardEntity consultboard = consultboardMapper.dtoToEntity(consultboardDto);
        LocalDateTime now = LocalDateTime.now();
        consultboard.setDate(now); // set the current date and time
        consultboardService.save(consultboard);
        return "redirect:/consultboard/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        consultboardEntity consultboard = consultboardService.findOne(id);
        if(consultboard == null) {
            return "redirect:/consultboard/list";
        }
        model.addAttribute("consultboard", consultboardMapper.entityToDto(consultboard));
        return "main_consultview";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        consultboardEntity consultboard = consultboardService.findOne(id);
        if(consultboard == null) {
            return "redirect:/consultboard/list";
        }
        model.addAttribute("consultboard", consultboardMapper.entityToDto(consultboard));
        return "main_consultedit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") Long id, @ModelAttribute consultboardDTO consultboardDto) {
        // Find the original entity
        consultboardEntity originalConsultboard = consultboardService.findOne(id);
        if(originalConsultboard == null) {
            return "redirect:/consultboard/list";
        }
        // Map the DTO to a new entity
        consultboardEntity consultboard = consultboardMapper.dtoToEntity(consultboardDto);
        // Set the id of the new entity to the id of the original
        consultboard.setId(id);
        // Preserve the original date
        consultboard.setDate(originalConsultboard.getDate());
        // Preserve the original writer
        consultboard.setWriter(originalConsultboard.getWriter());
        // Save the new entity
        consultboardService.save(consultboard);
        return "redirect:/consultboard/view/" + id;
    }

}
