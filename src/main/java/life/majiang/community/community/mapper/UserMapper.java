package life.majiang.community.community.mapper;

import life.majiang.community.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name, account_id, token, createtime, modifytime, avatar_url) values (#{name}, #{accountId}, #{token}, #{createTime}, #{modifyTime}, #{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id=#{id}")
    User findById(@Param("id") Integer id);

    @Select("select * from user where account_id=#{account_id}")
    User findByAccountId(@Param("account_id") String account_id);

    @Update("update user set name=#{name}, token=#{token}, modifytime=#{modifyTime}, avatar_url=#{avatarUrl} where id=#{id}")
    void update(User user);
}
