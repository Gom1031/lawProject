package lawproject.LawProject.Repository;

import lawproject.LawProject.Entity.consultboardEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface consultboardRepository extends JpaRepository<consultboardEntity, Long> {
    Page<consultboardEntity> findAll(Pageable pageable);
    List<consultboardEntity> findByTitleContaining(String title);
    List<consultboardEntity> findByWriterContaining(String writer);
    List<consultboardEntity> findByContentContaining(String content);
    List<consultboardEntity> findByTitleContainingOrWriterContainingOrContentContaining(String title, String writer, String content);
}
