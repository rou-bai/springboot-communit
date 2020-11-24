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
        User newUser = userMapper.findByAccountId(user.getAccountId());
        if(newUser == null){
            user.setModifyTime(System.currentTimeMillis());
            user.setCreateTime(user.getModifyTime());
            userMapper.insert(user);
        }else{
            newUser.setModifyTime(System.currentTimeMillis());
            newUser.setCreateTime(newUser.getModifyTime());
            newUser.setAvatarUrl(user.getAvatarUrl());
            newUser.setName(user.getName());
            newUser.setToken(user.getToken());
            userMapper.update(newUser);
        }
    }
}
