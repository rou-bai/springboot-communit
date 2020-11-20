package life.majiang.community.community.mapper;

import life.majiang.community.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name, account_id, token, ctime, mtime) values (#{name}, #{accountId}, #{token}, #{createTime}, #{modifyTime})")
    public void insert(User user);
}
