package life.majiang.community.community.mapper;

import life.majiang.community.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title, tag, description, createtime, modifytime, creator) values(#{title}, #{tag}, #{description}, #{createTime}, #{modifyTime}, #{creator})")
    void create(Question question);

    @Select("select * from question limit #{limit} offset #{offset}")
    List<Question> findAll(@Param(value="offset") Integer offset, @Param(value="limit") Integer limit);

    @Select("select count(1) from question")
    Integer countAll();

    @Select("select * from question where creator=#{userId} limit #{limit} offset #{offset}")
    List<Question> findByUserId(@Param(value="userId") Integer userId, @Param(value="offset") Integer offset, @Param(value="limit") Integer limit);

    @Select("select * from question where id=#{id}")
    Question findById(@Param(value="id") Integer id);

    @Update("update question set title=#{title}, tag=#{tag}, description=#{description}, modifytime=#{modifyTime} where id=#{id}")
    void update(Question question);

}
