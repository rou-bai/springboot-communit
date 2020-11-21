package life.majiang.community.community.mapper;

import life.majiang.community.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title, tag, description, ctime, mtime, creator) values(#{title}, #{tag}, #{description}, #{createTime}, #{modifyTime}, #{creator})")
    void create(Question question);
}
