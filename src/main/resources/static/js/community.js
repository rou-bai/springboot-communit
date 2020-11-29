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
                location.reload();
            }else if(response.code == 4001){
                var isAccept = confirm("当前用户未登陆，请确认是否跳转登陆");
                if(isAccept){
                    window.open("https://github.com/login/oauth/authorize?client_id=cbdb758ac2a1e2702a19&redirect_uri=http://localhost:8888/callback&scope=user&state=1");
                    window.localStorage.setItem("closeable", true);
                }
            }else{
                alert("回复失败");
            }
            $(".question-textarea").val("");
        },
        dataType: "json"
    });
}