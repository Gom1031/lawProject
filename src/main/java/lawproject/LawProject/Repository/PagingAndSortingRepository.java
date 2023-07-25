package lawproject.LawProject.Repository;

import lawproject.LawProject.Entity.consultboardEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//consultboard 테이블을 페이징하는 메서드
@Repository
public interface PagingAndSortingRepository extends JpaRepository<consultboardEntity, Long> {
    Page<consultboardEntity> findAll(Pageable pageable);
}
