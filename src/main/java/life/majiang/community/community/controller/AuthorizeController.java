package life.majiang.community.community.controller;

import life.majiang.community.community.dto.AccessTokenDTO;
import life.majiang.community.community.dto.GithubUser;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.User;
import life.majiang.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String ClientId;
    @Value("${github.client.secret}")
    private String ClientSecret;
    @Value("${github.direct.uri}")
    private String RedirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(ClientId);
        accessTokenDTO.setClient_secret(ClientSecret);
        accessTokenDTO.setRedirect_uri(RedirectUri);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
//        System.out.println("我的名字：" + user.getName());
        if (user != null){
            // 登陆成功
            System.out.println("登陆成功");

            //插入数据库
            User iuser = new User();
            iuser.setAccountId(String.valueOf(user.getId()));
            String token = UUID.randomUUID().toString();
            iuser.setToken(token);
            iuser.setName(user.getName());
            iuser.setCreateTime(System.currentTimeMillis());
            iuser.setModifyTime(iuser.getCreateTime());
            userMapper.insert(iuser);

            //这个是直接将token写进session里，现调整为数据库存储token,验证token一致的用户才记session
//            request.getSession().setAttribute("user", user);
            response.addCookie(new Cookie("token", token));

            return "redirect:/";
        }else {
            // 登陆失败
            System.out.println("登陆失败");
            return "redirect:/";
        }
    }
}
