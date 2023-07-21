package lawproject.LawProject.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lawproject.LawProject.Entity.consultboardEntity;
import lawproject.LawProject.Repository.consultboardRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class consultboardService {

    private final consultboardRepository consultboardRepository;

    public consultboardService(consultboardRepository consultboardRepository) {
        this.consultboardRepository = consultboardRepository;
    }

    @Transactional
    public consultboardEntity save(consultboardEntity consultboard) {
        return consultboardRepository.save(consultboard);
    }

    @Transactional(readOnly = true)
    public List<consultboardEntity> findAll() {
        return consultboardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<consultboardEntity> findById(Long id) {
        return consultboardRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public consultboardEntity findOne(Long id) {
        return consultboardRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        consultboardRepository.deleteById(id);
    }
    
    @Transactional
    public void delete(consultboardEntity consultboard) {
        consultboardRepository.delete(consultboard);
    }

    public String formatDateToKorean(LocalDateTime dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
    return dateTime.format(formatter);
    }

}
