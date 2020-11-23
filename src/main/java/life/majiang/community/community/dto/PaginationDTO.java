package life.majiang.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstpage;
    private boolean showNextpage;
    private boolean showEngPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPagination(Integer totalPage, Integer page, Integer size){
        this.page = page;
        this.totalPage = totalPage;

        // 计算页面显示的页码
        pages.add(page);
        for(int i=1; i<=3; i++){
            if (page - i > 0){
                pages.add(0, page - i);
            }
            if(page + i <= totalPage){
                pages.add(page + i);
            }
        }

        //计算是否显示上一页
        if(page != 1){
            showPrevious = true;
        }else{
            showPrevious = false;
        }

        //计算是否显示下一页
        if(page != totalPage){
            showNextpage = true;
        }else{
            showNextpage = false;
        }

        //计算是否显示第一页，这个有点疑问，先运行看
        if(pages.contains(1)){
            showFirstpage = false;
        }else{
            showFirstpage = true;
        }
        //计算是否显示最后一页
        if (pages.contains(totalPage)) {
            showEngPage = false;
        }else{
            showEngPage = true;
        }

    }
}
