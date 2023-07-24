package lawproject.LawProject.Controller;

import lawproject.LawProject.DTO.consultboardDTO;
import lawproject.LawProject.Entity.consultboardEntity;
import lawproject.LawProject.Mapper.consultboardMapper;
import lawproject.LawProject.Service.consultboardService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String postConsult(@Valid @ModelAttribute("consultboard") consultboardDTO consultboardDto, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "main_consultwrite";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : null;
        if (username != null) {
            consultboardDto.setWriter(username);
        } else {
            return "redirect:/user/login"; // user not logged in, redirect to login page
        }
        
        // Convert DTO to Entity
        consultboardEntity consultboard = consultboardMapper.dtoToEntity(consultboardDto);
        
        // Save the entity
        consultboardService.save(consultboard);
        return "redirect:/consultboard/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        consultboardEntity consultboard = consultboardService.findOne(id);
        if (consultboard == null) {
            redirectAttributes.addFlashAttribute("error", "The requested post does not exist.");
            return "redirect:/consultboard/list";
        }
    
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    
        // Add a check for null Authentication
        if (auth == null) {
            redirectAttributes.addFlashAttribute("error", "You need to be logged in to view this post.");
            return "redirect:/user/login";
        }
    
        String currentUsername = auth.getName();
        if (currentUsername == null || !currentUsername.equals(consultboard.getWriter())) {
            redirectAttributes.addFlashAttribute("error", "You are not the author of this post.");
            return "redirect:/consultboard/list";
        }
    
        model.addAttribute("consultboard", consultboardMapper.entityToDto(consultboard));
        return "main_consultview";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        consultboardEntity consultboard = consultboardService.findOne(id);
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
