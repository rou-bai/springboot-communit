package life.majiang.community.community.enmus;

public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论");

    private Integer type;
    private String name;

    public String getName(){
        return name;
    }
    public Integer getType(){
        return type;
    }
    NotificationTypeEnum(Integer type, String name){
        this.name = name;
        this.type = type;
    }

    public static String nameOfType(Integer type){
        for(NotificationTypeEnum NoticeEnum: NotificationTypeEnum.values()){
            if(NoticeEnum.getType() == type){
                return NoticeEnum.getName();
            }
        }
        return "";
    }
}
