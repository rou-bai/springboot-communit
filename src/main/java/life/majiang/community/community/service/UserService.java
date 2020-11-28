package life.majiang.community.community.service;

import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.User;
import life.majiang.community.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void updateOrAddUser(User user){
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());

        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0){
            //新增
            user.setModifytime(System.currentTimeMillis());
            user.setCreatetime(user.getModifytime());
            userMapper.insert(user);
        }else{
            //修改
            User dbUser = users.get(0);
            User newUser = new User();
            newUser.setModifytime(System.currentTimeMillis());
            newUser.setCreatetime(newUser.getModifytime());
            newUser.setAvatarUrl(user.getAvatarUrl());
            newUser.setName(user.getName());
            newUser.setToken(user.getToken());

            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(newUser, example);
        }
    }

    public void deleteUser(Long id){
        UserExample userExample = new UserExample();
        userMapper.deleteByPrimaryKey(id);
    }
}
