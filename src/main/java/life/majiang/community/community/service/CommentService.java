package life.majiang.community.community.service;

import life.majiang.community.community.enmus.CommentTypeEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.CommentMapper;
import life.majiang.community.community.mapper.QuestionExtMapper;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.model.Comment;
import life.majiang.community.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

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
}
