package life.majiang.community.community.model;

import lombok.Data;

@Data
public class Question {
    private Integer id;
    private String title;
    private String tag;
    private String description;
    private Long createTime;
    private Long modifyTime;
    private Integer likeCount;
    private Integer viewCount;
    private Integer cimmentCount;
    private Integer creator;
}
