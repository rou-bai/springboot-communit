package life.majiang.community.community.dto;

import life.majiang.community.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long notifier;
    private Long reciver;
    private Long outerId;
    private Integer type;
    private Long createtime;
    private Integer status;
    private String notifiername;
    private String outertitle;
    private Long questionid;

}
