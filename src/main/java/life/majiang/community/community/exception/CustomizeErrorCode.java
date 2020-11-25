package life.majiang.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
//    QUESTION_UPDATE_FAILED("修改问题失败");
    QUESTION_NOT_EXISTS("问题不存在");

    @Override
    public String getMessage(){
        return message;
    }

    private String message;
    CustomizeErrorCode(String message){
        this.message = message;
    }
}
