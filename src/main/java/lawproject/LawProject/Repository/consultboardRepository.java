package lawproject.LawProject.Repository;

import lawproject.LawProject.Entity.consultboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface consultboardRepository extends JpaRepository<consultboardEntity, Long> {
    List<consultboardEntity> findByTitleContaining(String title);
    List<consultboardEntity> findByWriterContaining(String writer);
    List<consultboardEntity> findByContentContaining(String content);
    List<consultboardEntity> findByTitleContainingOrWriterContainingOrContentContaining(String title, String writer, String content);
}
