/**
 * Created by sunjianhui on 2017/6/9.
 */
$(function () {
    leftNav($(".navBar ul li")[2]);
    $('.navBar li').hover(function(){
        leftNav(this);
    },function(){
        leftNav($(".navBar ul li")[2]);
    })
    getMonitorList();
});

// 获取我的监测列表内容
function getMonitorList() {
    var pageNo= $("#uniquePageNo").val();
    $.ajax({
        url: "/monitors/taskConfiguration/taskTable",
        type: "post",
        async:false,
        data: {"pageNo": pageNo},
        dataType : "html",
        success: function (html) {
            $(".monitorContent").html(html);
            // $(".leftNav").css("height",$(".comment").height()+120);
        }
    });
    $("#uniquePageNo").val(pageNo);
}

//点击启用按钮 变化
function enable(ev){
    var ids = ev.parentNode.parentNode.children[0].innerHTML;
    $.ajax({
        url: "/monitors/taskConfiguration/updateTaskStatus",
        type: "post",
        async:false,
        data: {"id": ids},
        dataType : "json",
        success: function (result) {
            if( result == 0){
                ev.innerHTML = "停用";
                $(ev.parentNode).prev().html("跟踪中");
                $(ev.parentNode).prev().addClass("inTracking");
            }else if(result == 1){
                    ev.innerHTML = "启用";
                    $(ev.parentNode).prev().html("已停止");
                    $(ev.parentNode).prev().removeClass("inTracking");
            }else{
                $.MsgBox.Alert("温馨提示","操作失败，请联系客服！");
                return false;
            }

        }
    });
}


//删除一条数据
function removeData(ev){
    var ids = ev.parentNode.parentNode.children[0].innerHTML;
    $.ajax({
        url: "/monitors/taskConfiguration/deleteTask",
        type: "post",
        async:false,
        data: {"id": ids},
        dataType : "json",
        success: function (result) {
            if( result ){
                getMonitorList();
            }
        }
    });

}

// 修改任务信息
function reviseData(ev) {
    var id = ev.parentNode.parentNode.children[0].innerHTML;
    window.location.href="/monitors/taskConfiguration/updateTask/"+id;
}
