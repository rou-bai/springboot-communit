package life.majiang.community.community.controller;

import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {
//    @ResponseBody

    @Autowired()
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if (user != null){
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }
        return "index";
    }
//    public String hello(@RequestParam(name = "name") String name, Model model){
//        model.addAttribute("name", name);
//        return "index";
//    }
}
