package life.majiang.community.community.controller;

import life.majiang.community.community.dto.CommentCreateDTO;
import life.majiang.community.community.dto.ResultDTO;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.model.Comment;
import life.majiang.community.community.model.User;
import life.majiang.community.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method= RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_NOT_EXISTS);
        }
        Comment comment = new Comment();
        comment.setContent(commentCreateDTO.getContent());
        comment.setCommentator(user.getId());
        comment.setCreatetime(System.currentTimeMillis());
        comment.setModifytime(comment.getCreatetime());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setType(commentCreateDTO.getType());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
}
