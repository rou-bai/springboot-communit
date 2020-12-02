package life.majiang.community.community.service;

import life.majiang.community.community.dto.CommentDTO;
import life.majiang.community.community.enmus.CommentTypeEnum;
import life.majiang.community.community.enmus.NotificationStatusEnum;
import life.majiang.community.community.enmus.NotificationTypeEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.*;
import life.majiang.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    //增加spring自带的事物，中途异常全部回滚
    public void insert(Comment comment, String name) {
        //参数检查
        if (comment.getParentId() == null && comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_PARENTID_WRONG);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //新建评论
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (parentComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_EXISTS);
            }
            commentMapper.insert(comment);

            //评论的评论数+1
            parentComment.setCommentCount(1);
            int upRes = commentExtMapper.incCommentCount(parentComment);
            if(upRes != 1){
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_COMMENT_FAILED);
            }

            Question question = questionMapper.selectByPrimaryKey(parentComment.getParentId());

            //创建通知
            createNotify(comment, parentComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT, name,
                         parentComment.getContent(), question.getId());

        } else {
            //新建问题回复
            Question parentQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (parentQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_EXISTS);
            }
            commentMapper.insert(comment);

            // 问题评论数+1
            parentQuestion.setCommentCount(1);
            int upRes = questionExtMapper.incComment(parentQuestion);
            if(upRes != 1){
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_BIND_QUESTION_FAILED);
            }

            //创建通知
            createNotify(comment, parentQuestion.getCreator(), NotificationTypeEnum.REPLY_QUESTION, name,
                         parentQuestion.getTitle(), parentQuestion.getId());
        }
    }

    public void createNotify(Comment comment, Long reciver, NotificationTypeEnum type, String name, String title, Long questionId){
        Notification notification = new Notification();
        notification.setCreatetime(System.currentTimeMillis());
        notification.setNotifier(comment.getCommentator());
        notification.setOuterid(comment.getParentId());
        notification.setType(type.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReciver(reciver);
        notification.setNotifiername(name);
        notification.setOutertitle(title);
        notification.setQuestionid(questionId);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByParentId(Long id, CommentTypeEnum type){
        //查询提问关联的评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("modifytime desc");
        List<Comment> questionCommentList = commentMapper.selectByExample(commentExample);

        if(questionCommentList == null){
            return new ArrayList<>();
        }

        //获取去重评论人
        Set<Long> commentators = questionCommentList.stream().map(comment->comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        Map<Long, User> userMap = null;
        if (userIds.size() != 0) {
            //查询全部评论人信息转化为map
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andIdIn(userIds);
            List<User> users = userMapper.selectByExample(userExample);
            if (users != null) {
                userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
            }
        }
        //遍历
        Map<Long, User> finalUserMap = userMap;
        List<CommentDTO> commentDTOS = questionCommentList.stream().map(comment->{
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(finalUserMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }

    public void incLikeCount(Long id){
        Comment comment = commentMapper.selectByPrimaryKey(id);
        if(comment == null){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_EXISTS);
        }
        comment.setLikeCount(1);
        int upRes = commentExtMapper.incLikeCount(comment);
        if(upRes != 1){
            throw  new CustomizeException(CustomizeErrorCode.COMMENT_LIKE_FAILED);
        }
    }
}
