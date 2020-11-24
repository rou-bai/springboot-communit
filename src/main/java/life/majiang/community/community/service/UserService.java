package life.majiang.community.community.service;

import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void updateOrAddUser(User user){
        User newuser = userMapper.findByAccountId(user.getAccountId());
        if(newuser == null){
            user.setModifyTime(System.currentTimeMillis());
            user.setCreateTime(user.getModifyTime());
            userMapper.insert(user);
        }else{
            newuser.setModifyTime(System.currentTimeMillis());
            newuser.setCreateTime(newuser.getModifyTime());
            newuser.setAvatarUrl(user.getAvatarUrl());
            newuser.setName(user.getName());
            newuser.setToken(user.getToken());
            userMapper.update(newuser);
        }
    }
}
