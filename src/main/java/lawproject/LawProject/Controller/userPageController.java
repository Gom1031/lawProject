package lawproject.LawProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lawproject.LawProject.Service.userService;
import lawproject.LawProject.DTO.userDTO;
import lawproject.LawProject.Entity.Role;

@Controller
@RequestMapping("/user")
public class userPageController {

    @Autowired
    private userService userService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new userDTO());
        return "main_register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") userDTO userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "main_register";
        }
        try {
            // 기본적으로 모든 사용자는 USER 역할을 가지게 됩니다.
            userDto.setRole(Role.USER);
            userService.registerUser(userDto);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "main_register";
        }
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "main_login";
    }
}
