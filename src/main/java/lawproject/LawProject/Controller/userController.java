package lawproject.LawProject.Controller;

import lawproject.LawProject.DTO.userDTO;
import lawproject.LawProject.Service.userService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class userController {

    @Autowired
    private userService userService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new userDTO());
        return "main_register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute userDTO userDto, Model model) {
        try {
            userService.registerUser(userDto);
            return "redirect:/user/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "user/register";
        }
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new userDTO());
        return "main_login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute userDTO userDto, HttpSession session, Model model) {
        if (userService.loginUser(userDto)) {
            session.setAttribute("user", userDto);
            return "redirect:/consultboard";
        } else {
            model.addAttribute("error", "Login failed.");
            return "user/login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}
