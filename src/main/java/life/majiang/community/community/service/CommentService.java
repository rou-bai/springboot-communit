package life.majiang.community.community.service;

import life.majiang.community.community.dto.CommentDTO;
import life.majiang.community.community.enmus.CommentTypeEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.CommentMapper;
import life.majiang.community.community.mapper.QuestionExtMapper;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
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

    @Transactional
    //增加spring自带的事物，中途异常全部回滚
    public void insert(Comment comment) {
        //参数检查
        if (comment.getParentId() == null && comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_PARENTID_WRONG);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //新建评论
            Comment addComment = commentMapper.selectByPrimaryKey(comment.getId());
            if (addComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_EXISTS);
            }
            commentMapper.insert(comment);
        } else {
            //新建问题回复
            Question addQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (addQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_EXISTS);
            }
            commentMapper.insert(comment);

            addQuestion.setCommentCount(1);
            int upRes = questionExtMapper.incComment(addQuestion);
            if(upRes != 1){
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_BIND_QUESTION_FAILED);
            }
        }
    }

    public List<CommentDTO> listByQuestionId(Long id){
        //查询提问关联的评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
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
}
