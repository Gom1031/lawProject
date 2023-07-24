package lawproject.LawProject.Service;

import lawproject.LawProject.DTO.userDTO;
import lawproject.LawProject.Entity.userEntity;
import lawproject.LawProject.Repository.userRepository;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService {

    @Autowired
    private userRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerUser(userDTO userDto) throws Exception {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new Exception("Username already exists.");
        }
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new Exception("Email already exists.");
        }
        userEntity user = new userEntity();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setPhone_number(userDto.getPhone_number());
        user.setRegister_date(LocalDateTime.now());
        user.setLast_edit_date(LocalDateTime.now());
        userRepository.save(user);
    }

    public boolean loginUser(userDTO userDto) {
        userEntity user = userRepository.findByUsername(userDto.getUsername());
        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }
}
