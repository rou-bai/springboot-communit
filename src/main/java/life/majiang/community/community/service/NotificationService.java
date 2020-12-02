package life.majiang.community.community.service;

import life.majiang.community.community.dto.NotificationDTO;
import life.majiang.community.community.dto.PaginationDTO;
import life.majiang.community.community.dto.QuestionDTO;
import life.majiang.community.community.enmus.NotificationStatusEnum;
import life.majiang.community.community.enmus.NotificationTypeEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.CommentMapper;
import life.majiang.community.community.mapper.NotificationMapper;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    //获取通知数据量
    public Integer unreadCount(Long id){
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReciverEqualTo(id)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        Integer count = (int) notificationMapper.countByExample(notificationExample);
        return count;
    }

    //获取通知数据信息
    public PaginationDTO listByReciverId(Long userId, Integer page, Integer size){
        Integer offset = size * (page - 1);
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andNotifierEqualTo(userId);
        example.setOrderByClause("createtime desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        if(notifications.size() == 0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationList = new ArrayList<>();

        for (Notification notification:notifications){
            NotificationDTO notificationDTO = new NotificationDTO();

//             这部分取得数据直接存数据库
//            User user = userMapper.selectByPrimaryKey(notification.getNotifier());
//            notificationDTO.setNoticeUser(user);
//
//            if(notification.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()){
//                //回答提问
//                Question question = questionMapper.selectByPrimaryKey(notification.getOuterid());
//                if(question != null){
//                    notificationDTO.setOuterTitle(question.getTitle());
//                    notificationDTO.setQuestionId(question.getId());
//                }
//            }else{
//                //回答评论
//                Comment comment = commentMapper.selectByPrimaryKey(notification.getOuterid());
//                if(comment != null){
//                    notificationDTO.setOuterTitle(comment.getContent());
//                    //获取二级评论所在提问
//                    Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
//                    if(parentComment != null){
//                        notificationDTO.setQuestionId(parentComment.getParentId());
//                    }
//                }
//            }

            BeanUtils.copyProperties(notification, notificationDTO);
            notificationList.add(notificationDTO);
        }

        paginationDTO.setData(notificationList);
        Integer totalCount = (int) notificationMapper.countByExample(example);
        Integer totalPage;
        // 计算总页数
        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page, size);
        return paginationDTO;
    }

    public NotificationDTO read(Long id, User user){
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTICE_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReciver(), user.getId())){
            throw new CustomizeException(CustomizeErrorCode.NOTICE_READ_FAILED);
        }

        //根据主键修改
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        return notificationDTO;
    }

//    public void updateStatus(Long id){
//        Notification notification = notificationMapper.selectByPrimaryKey(id);
//        if(notification == null){
//            throw new CustomizeException(CustomizeErrorCode.NOTICE_NOT_FOUND);
//        }
//
//        notification.setStatus(NotificationStatusEnum.READ.getStatus());
//        NotificationExample notificationExample = new NotificationExample();
//        notificationExample.createCriteria()
//                .andIdEqualTo(id);
//        int upRes = notificationMapper.updateByExampleSelective(notification, notificationExample);
//        if (upRes != 1){
//            throw new CustomizeException(CustomizeErrorCode.QUESTION_UPDATE_FAILED);
//        }
//
//    }
}
