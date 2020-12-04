package life.majiang.community.community.controller;

import life.majiang.community.community.dto.PaginationDTO;
import life.majiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name="page", defaultValue = "1") Integer page,
                        @RequestParam(name="size", defaultValue = "3") Integer size,
                        @RequestParam(name="search", required = false) String search){
        PaginationDTO pagination = questionService.list(page, size, search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        return "index";
    }
//    public String hello(@RequestParam(name = "name") String name, Model model){
//        model.addAttribute("name", name);
//        return "index";
//    }
}
