package life.majiang.community.community.controller;

import life.majiang.community.community.dto.QuestionDTO;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.listById(id);
        model.addAttribute("question", questionDTO);

        //阅读数增加
        questionService.updateViewCount(id);
        return "question";
    }
}
