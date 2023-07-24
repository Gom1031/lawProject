package lawproject.LawProject.Repository;

import lawproject.LawProject.Entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<userEntity, Long> {
    userEntity findByUsername(String username);
    userEntity findByEmail(String email);
}
