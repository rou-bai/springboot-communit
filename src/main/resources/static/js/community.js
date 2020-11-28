function question_post(){
    var questionId = $("#question_hidden").val();
    var comment = $(".question-textarea").val();
    data = {
        "parentId": questionId,
        "type": 1,
        "content": comment,
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        data:JSON.stringify(data),
        contentType: "application/json",
        success: function (response){
            if(response.code == 200){
                alert("回复成功");
                $(".question-textarea").val("");
            }
        },
        dataType: "json"
    });
}