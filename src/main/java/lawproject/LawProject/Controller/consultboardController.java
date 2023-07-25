package lawproject.LawProject.Controller;

import lawproject.LawProject.DTO.consultboardDTO;
import lawproject.LawProject.Entity.consultboardEntity;
import lawproject.LawProject.Mapper.consultboardMapper;
import lawproject.LawProject.Service.consultboardService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultboard")
public class consultboardController {

    // 서비스 및 매퍼 클래스에 대한 의존성 주입
    @Autowired
    private consultboardService consultboardService;
    @Autowired
    private consultboardMapper consultboardMapper;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNumber,
                    @RequestParam(defaultValue = "10") int pageSize,
                    Model model) {
    
        // pageNumber가 1보다 작다면 1로 설정
        pageNumber = pageNumber < 1 ? 1 : pageNumber;
    
        // pageNumber와 pageSize를 전달하여 페이지네이션 적용하여 게시글 조회
        Page<consultboardEntity> consultboardPage = consultboardService.getConsultboardPage(pageNumber - 1, pageSize);
    
        // 모든 게시글에 대해 한국 시간 포맷으로 날짜를 변경
        consultboardPage.forEach(consultboard -> {
            String formattedDate = consultboardService.formatDateToKorean(consultboard.getDate());
            consultboard.setFormattedDate(formattedDate);
        });
    
        // 조회된 페이지를 모델에 추가
        model.addAttribute("consultboardPage", consultboardPage);
    
        // 상담 게시판 리스트 페이지를 반환
        return "main_consultboard";
    }
    


    // 상담 글 작성 페이지로 이동하는 메소드. 새로운 DTO를 모델에 추가하고 페이지를 반환합니다.
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("consultboard", new consultboardDTO());
        return "main_consultwrite";
    }
    
    // 상담 글을 작성하고 저장하는 메소드. DTO를 엔티티로 변환 후 저장하고, 상담 게시판 리스트 페이지로 리다이렉트합니다.
    @PostMapping("/post")
    public String postConsult(@Valid @ModelAttribute("consultboard") consultboardDTO consultboardDto, BindingResult result, Model model, HttpServletRequest request) {
        // 유효성 검사를 통과하지 못하면 작성 페이지로 돌아갑니다.
        if (result.hasErrors()) {
            return "main_consultwrite";
        }

        // 현재 인증된 사용자의 이름을 작성자로 설정합니다.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : null;
        if (username != null) {
            consultboardDto.setWriter(username);
        } else {
            return "redirect:/user/login"; // 로그인되지 않은 사용자를 로그인 페이지로 리다이렉트
        }
        
        // DTO를 엔티티로 변환 후 저장
        consultboardEntity consultboard = consultboardMapper.dtoToEntity(consultboardDto);
        consultboardService.save(consultboard);
        
        // 상담 게시판 리스트 페이지로 리다이렉트
        return "redirect:/consultboard/list";
    }

    // 특정 상담 게시글을 조회하는 메소드. 게시글이 존재하면 해당 게시글을 모델에 추가하고 페이지를 반환합니다.
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        // 특정 게시글을 조회
        consultboardEntity consultboard = consultboardService.findOne(id);

        // 게시글이 존재하지 않으면 오류 메시지를 추가하고 리스트 페이지로 리다이렉트
        if (consultboard == null) {
            redirectAttributes.addFlashAttribute("error", "The requested post does not exist.");
            return "redirect:/consultboard/list";
        }

        // 현재 인증된 사용자 정보 조회
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없으면 오류 메시지를 추가하고 로그인 페이지로 리다이렉트
        if (auth == null) {
            redirectAttributes.addFlashAttribute("error", "You need to be logged in to view this post.");
            return "redirect:/user/login";
        }

        // 현재 사용자가 게시글의 작성자가 아니라면 오류 메시지를 추가하고 리스트 페이지로 리다이렉트
        String currentUsername = auth.getName();
        if (currentUsername == null || !currentUsername.equals(consultboard.getWriter())) {
            redirectAttributes.addFlashAttribute("error", "You are not the author of this post.");
            return "redirect:/consultboard/list";
        }

        // 게시글을 DTO로 변환 후 모델에 추가
        model.addAttribute("consultboard", consultboardMapper.entityToDto(consultboard));

        // 상담 게시글 조회 페이지를 반환
        return "main_consultview";
    }
    
    // 상담 게시글 편집 페이지로 이동하는 메소드. 특정 게시글을 DTO로 변환 후 모델에 추가하고 페이지를 반환합니다.
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        // 특정 게시글을 조회
        consultboardEntity consultboard = consultboardService.findOne(id);
        
        // 게시글을 DTO로 변환 후 모델에 추가
        model.addAttribute("consultboard", consultboardMapper.entityToDto(consultboard));

        // 상담 게시글 편집 페이지를 반환
        return "main_consultedit";
    }

    // 편집된 상담 게시글을 저장하는 메소드. DTO를 엔티티로 변환 후 저장하고, 조회 페이지로 리다이렉트합니다.
    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") Long id, @ModelAttribute consultboardDTO consultboardDto) {
        // 원래의 게시글을 조회
        consultboardEntity originalConsultboard = consultboardService.findOne(id);

        // 원래의 게시글이 없다면 리스트 페이지로 리다이렉트
        if(originalConsultboard == null) {
            return "redirect:/consultboard/list";
        }

        // DTO를 엔티티로 변환
        consultboardEntity consultboard = consultboardMapper.dtoToEntity(consultboardDto);

        // 원래의 게시글 정보를 유지
        consultboard.setId(id);
        consultboard.setDate(originalConsultboard.getDate());
        consultboard.setWriter(originalConsultboard.getWriter());

        // 게시글을 저장
        consultboardService.save(consultboard);

        // 상담 게시글 조회 페이지로 리다이렉트
        return "redirect:/consultboard/view/" + id;
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
            return "main_searchresult"; // assuming you have a corresponding HTML template
        } else {
            model.addAttribute("consultboards", searchResults);
            return "main_searchresult";
        }
    }   
}

