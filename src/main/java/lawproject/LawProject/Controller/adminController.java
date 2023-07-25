package lawproject.LawProject.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lawproject.LawProject.DTO.consultboardDTO;
import lawproject.LawProject.Entity.consultboardEntity;
import lawproject.LawProject.Mapper.consultboardMapper;
import lawproject.LawProject.Service.consultboardService;

@Controller
@RequestMapping("/admin")
public class adminController {
    // 서비스 및 매퍼 클래스에 대한 의존성 주입
    @Autowired
    private consultboardMapper consultboardMapper;
    @Autowired
    private consultboardService consultboardService;

    // 관리자 대시보드 페이지로 이동하는 메소드. 모든 컨설팅 게시글을 조회하고, 모델에 추가한 후 페이지를 반환합니다.
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // 모든 게시글을 조회
        List<consultboardEntity> consultboards = consultboardService.findAll();

        // 모든 게시글에 대해 한국 시간 포맷으로 날짜를 변경
        consultboards.forEach(consultboard -> {
            String formattedDate = consultboardService.formatDateToKorean(consultboard.getDate());
            consultboard.setFormattedDate(formattedDate);
        });

        // 조회된 게시글 리스트를 모델에 추가
        model.addAttribute("consultboards", consultboards);
        
        // 관리자 대시보드 페이지를 반환
        return "admin/admin_dashboard";
    }

    // 특정 게시글을 조회하는 메소드. 조회된 게시글을 모델에 추가하고 페이지를 반환합니다.
    @GetMapping("/view/{id}")
    public String adminView(@PathVariable("id") Long id, Model model) {
        // 특정 게시글을 조회
        consultboardEntity consultboard = consultboardService.findOne(id);

        // 게시글이 존재하면 모델에 추가하고 페이지 반환, 존재하지 않으면 대시보드로 리다이렉트
        if (consultboard != null) {
            model.addAttribute("consultboard", consultboard);
            return "admin/admin_consultview";
        } else {
            return "redirect:/admin/dashboard";
        }
    }

    // 게시글 편집 페이지로 이동하는 메소드. 특정 게시글을 DTO로 변환 후 모델에 추가하고 페이지를 반환합니다.
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        // 특정 게시글을 조회
        consultboardEntity consultboard = consultboardService.findOne(id);
        
        // 게시글을 DTO로 변환 후 모델에 추가
        model.addAttribute("consultboard", consultboardMapper.entityToDto(consultboard));
        
        // 게시글 편집 페이지를 반환
        return "admin/admin_edit";
    }

    // 편집된 게시글을 저장하는 메소드. DTO를 엔티티로 변환 후 저장하고, 조회 페이지로 리다이렉트합니다.
    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") Long id, @ModelAttribute consultboardDTO consultboardDto) {
        // 특정 게시글을 조회
        consultboardEntity originalConsultboard = consultboardService.findOne(id);
        
        // 게시글이 존재하지 않으면 대시보드로 리다이렉트
        if(originalConsultboard == null) {
            return "redirect:/admin/dashboard";
        }

        // DTO를 엔티티로 변환 후 저장
        consultboardEntity consultboard = consultboardMapper.dtoToEntity(consultboardDto);
        consultboard.setId(id);
        consultboard.setDate(originalConsultboard.getDate());
        consultboard.setWriter(originalConsultboard.getWriter());
        consultboardService.save(consultboard);

        // 편집된 게시글 조회 페이지로 리다이렉트
        return "redirect:/admin/view/" + id;
    }

    // 게시글 작성 페이지로 이동하는 메소드. 새로운 DTO를 모델에 추가하고 페이지를 반환합니다.
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("consultboard", new consultboardDTO());
        return "admin/admin_write";
    }

    // 작성된 게시글을 저장하는 메소드. DTO를 엔티티로 변환 후 저장하고, 대시보드로 리다이렉트합니다.
    @PostMapping("/write")
    public String writeSubmit(@ModelAttribute consultboardDTO consultboardDto) {
        consultboardEntity consultboard = consultboardMapper.dtoToEntity(consultboardDto);
        consultboardService.save(consultboard);
        return "redirect:/admin/dashboard";
    }
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam("searchType") String searchType, @RequestParam("keyword") String keyword, Model model) {
        List<consultboardEntity> searchResults;
        
        if ("title".equals(searchType)) {
            searchResults = consultboardService.searchByTitle(keyword);
        } else if ("writer".equals(searchType)) {
            searchResults = consultboardService.searchByWriter(keyword);
        } else { // content
            searchResults = consultboardService.searchByContent(keyword);
        }
        
        // 검색 결과에 대해 한국 시간 포맷으로 날짜를 변경
        searchResults.forEach(consultboard -> {
            String formattedDate = consultboardService.formatDateToKorean(consultboard.getDate());
            consultboard.setFormattedDate(formattedDate);
        });
    
        if (searchResults.isEmpty()) {
            model.addAttribute("message", "No results found for your search");
            return "admin/admin_searchresult"; // assuming you have a corresponding HTML template
        } else {
            model.addAttribute("consultboards", searchResults);
            return "admin/admin_searchresult";
        }
    } 
}

