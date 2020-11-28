package life.majiang.community.community.mapper;

import java.util.List;
import life.majiang.community.community.model.Question;
import life.majiang.community.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface QuestionExtMapper {
    int incView(@Param("record") Question record);
    int incComment(@Param("record") Question record);
}
