package lawproject.LawProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lawproject.LawProject.Entity.Role;
import lawproject.LawProject.DTO.userDTO;
import lawproject.LawProject.Entity.userEntity;
import lawproject.LawProject.Service.userService;

import java.time.LocalDateTime;

@SpringBootApplication
public class LawprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawprojectApplication.class, args);
    }

    @Autowired
    private userService userService;

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            // 어드민 계정 없으면 생성
            userEntity adminEntity = userService.findByUsername("admin");
            if (adminEntity == null) {
                userDTO newAdmin = new userDTO();
                newAdmin.setUserid(99999L);
                newAdmin.setUsername("admin");
                newAdmin.setPassword("0000");
                newAdmin.setEmail("admin@lawproject.com");
                newAdmin.setRole(Role.ADMIN); 
                newAdmin.setPhone_number("1234567890");
                newAdmin.setRegiester_date(LocalDateTime.now());
                newAdmin.setLast_edit_date(LocalDateTime.now());

                userService.registerUser(newAdmin);
            }
        };
    }
}

