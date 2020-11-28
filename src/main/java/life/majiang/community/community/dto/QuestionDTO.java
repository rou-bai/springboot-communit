package life.majiang.community.community.dto;

import life.majiang.community.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String tag;
    private String description;
    private Long createtime;
    private Long modifytime;
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
    private Long creator;
    private User user;
}
