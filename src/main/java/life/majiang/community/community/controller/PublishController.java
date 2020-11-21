package life.majiang.community.community.controller;

import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.Question;
import life.majiang.community.community.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired(required=false)
    private QuestionMapper questionMapper;
    @Autowired(required=false)
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            HttpServletRequest request,
                            Model model){

        //判断参数必传
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if(title == null || title == ""){
            model.addAttribute("error", "请填写标题");
            return "publish";
        }else if(description == null || description == ""){
            model.addAttribute("error", "请填写问题补充");
            return "publish";
        }else if(tag == null || tag == ""){
            model.addAttribute("error", "请填写标签");
            return "publish";
        }

        User user = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if (cookie.getName().equals("token")){
                user = userMapper.findByToken(cookie.getValue());
                if (user != null){
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }

        if (user == null){
            model.addAttribute("error", "用户未登陆");
            return "publish";
        }

        Question question = new Question();
        question.setCreator(user.getId());
        question.setCreateTime(System.currentTimeMillis());
        question.setModifyTime(question.getCreateTime());
        question.setTag(tag);
        question.setTitle(title);
        question.setDescription(description);
        questionMapper.create(question);
        return "redirect:/";
    }
}
