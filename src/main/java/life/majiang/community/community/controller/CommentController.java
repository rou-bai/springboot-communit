package life.majiang.community.community.controller;

import life.majiang.community.community.dto.CommentCreateDTO;
import life.majiang.community.community.dto.CommentDTO;
import life.majiang.community.community.dto.ResultDTO;
import life.majiang.community.community.enmus.CommentTypeEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.model.Comment;
import life.majiang.community.community.model.User;
import life.majiang.community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_CONTENT_IS_NULL);
        }
        Comment comment = new Comment();
        comment.setContent(commentCreateDTO.getContent());
        comment.setCommentator(user.getId());
        comment.setCreatetime(System.currentTimeMillis());
        comment.setModifytime(comment.getCreatetime());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setType(commentCreateDTO.getType());
        comment.setLikeCount(0);
        comment.setCommentCount(0);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/like/{id}", method = RequestMethod.GET)
    public Object like(@PathVariable(name="id") Long id){
        if (id == null){
           return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_ID_EMPTY);
        }
        commentService.incLikeCount(id);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method=RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> getComment(@PathVariable(name="id") Long id,
                             Model model){
        if(id == null){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_ID_EMPTY);
        }
        List<CommentDTO> comments = commentService.listByParentId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(comments);

    }
}
