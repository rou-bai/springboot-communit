package life.majiang.community.community.mapper;

import life.majiang.community.community.model.Comment;
import org.apache.ibatis.annotations.Param;

public interface CommentExtMapper {
    int incLikeCount(@Param("record") Comment record);
    int incCommentCount(@Param("record") Comment record);
}
