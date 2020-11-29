package life.majiang.community.community.dto;

import life.majiang.community.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private String content;
    private Long parentId;
    private Integer type;
    private Long id;
    private Long commentator;
    private Long modifytime;
    private Long createtime;
    private Long likeCount;
    private User user;
}
