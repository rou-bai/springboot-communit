package life.majiang.community.community.controller;

import life.majiang.community.community.dto.PaginationDTO;
import life.majiang.community.community.model.Notification;
import life.majiang.community.community.model.User;
import life.majiang.community.community.service.NotificationService;
import life.majiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action,
                          @RequestParam(name="page", defaultValue = "1") Integer page,
                          @RequestParam(name="size", defaultValue = "3") Integer size,
                          Model model,
                          HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return "index";
        }

        if("questions".equals(action)){
            model.addAttribute("section", action);
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginations = questionService.listByUserId(user.getId(), page, size);
            model.addAttribute("pagination", paginations);
        }
        if("replies".equals(action)){
            model.addAttribute("section", action);
            model.addAttribute("sectionName", "最新回复");
            PaginationDTO paginations = notificationService.listByReciverId(user.getId(), page, size);
            model.addAttribute("pagination", paginations);
        }
        return "profile";
    }
}
