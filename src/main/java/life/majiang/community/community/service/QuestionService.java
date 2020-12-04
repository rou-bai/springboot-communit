package life.majiang.community.community.service;

import life.majiang.community.community.dto.PaginationDTO;
import life.majiang.community.community.dto.QuestionDTO;
import life.majiang.community.community.dto.QuestionQueryDTO;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.QuestionExtMapper;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.Question;
import life.majiang.community.community.model.QuestionExample;
import life.majiang.community.community.model.User;
import life.majiang.community.community.exception.CustomizeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//这个文件处理数据库关联关系
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size, String search){
        Integer offset = size * (page - 1);
//        QuestionExample example = new QuestionExample();
        if(StringUtils.isNotBlank(search)){

            String[] searchs = StringUtils.split(search, " ");
            search = Arrays.stream(searchs).collect(Collectors.joining("|"));
        }

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setOffset(offset);
//        example.setOrderByClause("modifytime desc");
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();

        for (Question question:questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);

        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
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

    public PaginationDTO listByUserId(Long userId, Integer page, Integer size){
        Integer offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        example.setOrderByClause("modifytime desc");
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();

        for (Question question:questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        Integer totalCount = (int) questionMapper.countByExample(example);
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

    public QuestionDTO listById(Long id){
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_EXISTS);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        BeanUtils.copyProperties(question, questionDTO);

        return questionDTO;
    }

    public void updateOrAdd(Question question){
        if(question.getId() != null && !question.getId().equals("")){
            Question updateQuestion = new Question();
            updateQuestion.setModifytime(System.currentTimeMillis());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());

            int upRes = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (upRes != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_UPDATE_FAILED);
            }
        }else{
            question.setCreatetime(System.currentTimeMillis());
            question.setModifytime(question.getCreatetime());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }
    }

    public void updateViewCount(Long id){
        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setViewCount(1);
        int upRes = questionExtMapper.incView(updateQuestion);
        if (upRes != 1){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_UPDATE_FAILED);
        }
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO){
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }

        String[] tags = StringUtils.split(questionDTO.getTag(), "，");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setTag(regexpTag);
        question.setId(questionDTO.getId());
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q->{
            QuestionDTO quesDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, quesDTO);
            return quesDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
