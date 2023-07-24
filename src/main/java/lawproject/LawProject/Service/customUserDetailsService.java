package lawproject.LawProject.Service;

import lawproject.LawProject.Model.User;
import lawproject.LawProject.Repository.userRepository;
import lawproject.LawProject.Entity.userEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class customUserDetailsService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return userEntityToUser(userEntity);
    }
    
    private User userEntityToUser(userEntity userEntity) {
        return new User(userEntity);
    }
    
}

