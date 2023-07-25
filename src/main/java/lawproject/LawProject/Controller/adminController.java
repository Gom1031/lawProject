package lawproject.LawProject.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lawproject.LawProject.DTO.consultboardDTO;
import lawproject.LawProject.Entity.consultboardEntity;
import lawproject.LawProject.Mapper.consultboardMapper;
import lawproject.LawProject.Service.consultboardService;

@Controller
@RequestMapping("/admin")
public class adminController {
    @Autowired
    private consultboardMapper consultboardMapper;

    @Autowired
    private consultboardService consultboardService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<consultboardEntity> consultboards = consultboardService.findAll();
        
        consultboards.forEach(consultboard -> {
            String formattedDate = consultboardService.formatDateToKorean(consultboard.getDate());
            consultboard.setFormattedDate(formattedDate);
        });
    
        model.addAttribute("consultboards", consultboards);
        return "admin/admin_dashboard";
    }
    

    @GetMapping("/view/{id}")
    public String adminView(@PathVariable("id") Long id, Model model) {
        consultboardEntity consultboard = consultboardService.findOne(id);
        if (consultboard != null) {
            model.addAttribute("consultboard", consultboard);
            return "admin/admin_consultview";
        } else {
            return "redirect:/admin/dashboard";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        consultboardEntity consultboard = consultboardService.findOne(id);
        model.addAttribute("consultboard", consultboardMapper.entityToDto(consultboard));
        return "admin/admin_edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") Long id, @ModelAttribute consultboardDTO consultboardDto) {
        consultboardEntity originalConsultboard = consultboardService.findOne(id);
        if(originalConsultboard == null) {
            return "redirect:/admin/dashboard";
        }
        consultboardEntity consultboard = consultboardMapper.dtoToEntity(consultboardDto);
        consultboard.setId(id);
        consultboard.setDate(originalConsultboard.getDate());
        consultboard.setWriter(originalConsultboard.getWriter());
        consultboardService.save(consultboard);
        return "redirect:/admin/view/" + id;
    }

    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("consultboard", new consultboardDTO());
        return "admin/admin_write";
    }

    @PostMapping("/write")
    public String writeSubmit(@ModelAttribute consultboardDTO consultboardDto) {
        consultboardEntity consultboard = consultboardMapper.dtoToEntity(consultboardDto);
        consultboardService.save(consultboard);
        return "redirect:/admin/dashboard";
    }

}
