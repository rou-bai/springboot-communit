package life.majiang.community.community.exception;

import java.util.HashMap;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
//枚举值写法，用逗号隔开，最后用分号
    QUESTION_UPDATE_FAILED(2001, "修改问题失败"),
    QUESTION_NOT_EXISTS(2002,"问题不存在"),
    COMMENT_PARENTID_WRONG(3001, "错误的父类ID"),
    COMMENT_TYPE_WRONG(3002, "错误的评论类型"),
    COMMENT_NOT_EXISTS(3003, "评论不存在"),
    COMMENT_BIND_QUESTION_FAILED(3004, "评论问题失败"),
    USER_NOT_EXISTS(4001, "用户不存在"),
    SYSTEM_ERROR(5001, "系统错误");
    ;

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public  Integer getCode(){return code;}

    private String message;
    private Integer code;
    CustomizeErrorCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
