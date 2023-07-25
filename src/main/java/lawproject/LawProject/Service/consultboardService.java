package lawproject.LawProject.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lawproject.LawProject.Entity.consultboardEntity;
import lawproject.LawProject.Repository.consultboardRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service // 이 클래스가 서비스 레이어의 컴포넌트임을 나타냄
public class consultboardService {

    private final consultboardRepository consultboardRepository;

    // 생성자 주입을 통한 의존성 주입
    @Autowired
    public consultboardService(consultboardRepository consultboardRepository) {
        this.consultboardRepository = consultboardRepository;
    }

    // 트랜잭션 시작 - 데이터 일관성을 유지하기 위한 어노테이션
    @Transactional
    public consultboardEntity save(consultboardEntity consultboard) {
        // 현재시각으로 설정
        consultboard.setDate(LocalDateTime.now());

        return consultboardRepository.save(consultboard); // 데이터 저장
    }

    // 읽기 전용 트랜잭션 - 데이터 변경이 없는 경우 성능 최적화
    @Transactional(readOnly = true)
    public List<consultboardEntity> findAll() {
        return consultboardRepository.findAll(); // 모든 데이터를 가져옴
    }

    // 읽기 전용 트랜잭션
    @Transactional(readOnly = true)
    public Optional<consultboardEntity> findById(Long id) {
        return consultboardRepository.findById(id); // id로 데이터를 찾아 Optional 형태로 반환
    }

    // 읽기 전용 트랜잭션
    @Transactional(readOnly = true)
    public consultboardEntity findOne(Long id) {
        return consultboardRepository.findById(id).orElse(null); // id로 데이터를 찾아 없으면 null 반환
    }

    // 트랜잭션 시작
    @Transactional
    public void deleteById(Long id) {
        consultboardRepository.deleteById(id); // id로 데이터 삭제
    }
    
    // 트랜잭션 시작
    @Transactional
    public void delete(consultboardEntity consultboard) {
        consultboardRepository.delete(consultboard); // 엔티티로 데이터 삭제
    }

    // 날짜를 "yyyy년 MM월 dd일 HH시 mm분 ss초" 형식의 문자열로 변환하는 메소드
    public String formatDateToKorean(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        return dateTime.format(formatter);
    }

    @Transactional(readOnly = true)
    public List<consultboardEntity> searchByTitle(String title) {
        return consultboardRepository.findByTitleContaining(title);
    }

    @Transactional(readOnly = true)
    public List<consultboardEntity> searchByWriter(String writer) {
        return consultboardRepository.findByWriterContaining(writer);
    }
    
    @Transactional(readOnly = true)
    public List<consultboardEntity> searchByContent(String content) {
        return consultboardRepository.findByContentContaining(content);
    }
    

    // 페이지네이션 처리
    int pageNumber = 1; // 페이지 번호
    int pageSize = 10; // 페이지 크기

    // 페이지네이션 처리를 위한 메소드
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public Page<consultboardEntity> getConsultboardPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber , pageSize); // 페이지 번호는 0부터 시작
        return consultboardRepository.findAll(pageable); // 페이지네이션 적용하여 조회
    }
}
