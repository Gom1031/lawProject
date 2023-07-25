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

    // 서비스 클래스에 대한 의존성 주입
    @Autowired
    private userService userService;

    // 사용자 등록 페이지로 이동하는 메소드. 새로운 DTO를 모델에 추가하고 페이지를 반환합니다.
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new userDTO());
        return "main_register";
    }

    // 사용자를 등록하는 메소드. DTO를 사용하여 사용자를 등록하고, 등록이 성공하면 로그인 페이지로 리다이렉트합니다.
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") userDTO userDto, BindingResult result, Model model) {
        // 유효성 검사를 통과하지 못하면 등록 페이지로 돌아갑니다.
        if (result.hasErrors()) {
            return "main_register";
        }
        try {
            // 기본적으로 모든 사용자는 USER 역할을 가지게 됩니다.
            userDto.setRole(Role.USER);
            userService.registerUser(userDto);
        } catch (Exception e) {
            // 오류가 발생하면 오류 메시지를 모델에 추가하고 등록 페이지로 돌아갑니다.
            model.addAttribute("error", e.getMessage());
            return "main_register";
        }
        // 로그인 페이지로 리다이렉트합니다.
        return "redirect:/user/login";
    }

    // 로그인 페이지로 이동하는 메소드.
    @GetMapping("/login")
    public String loginForm() {
        return "main_login";
    }
}

