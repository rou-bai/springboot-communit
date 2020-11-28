package life.majiang.community.community.dto;

import lombok.Data;
import org.omg.PortableInterceptor.INACTIVE;

@Data
public class CommentDTO {
    private String content;
    private Long parentId;
    private Integer type;
}
