package lawproject.LawProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lawproject.LawProject.Entity.consultboardEntity;

@Repository
public interface consultboardRepository extends JpaRepository<consultboardEntity, Long>{
    
}
