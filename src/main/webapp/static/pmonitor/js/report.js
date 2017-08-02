$(function () {
    // datatextt($("#letter"));
     leftNav($(".navBar ul li")[1]);
    $('.navBar li').hover(function(){
        leftNav(this);
    },function(){
        leftNav($(".navBar ul li")[1]);
    })
    $(document).on("click", function () {//对document绑定一个影藏Div方法
        $('#category .category_left').addClass('none');
        $('#category .category_right').addClass('none');

    });

    var isClick = false;
    $('#selectdiv').click(function (event) {
        show_scenic(event);
        $('#hoverli').html('');
        $('#category .category_left').toggleClass('none');
        $('#category .category_right').addClass('none');
        downlist();
    });

    $('body #taskname p').on('click', 'span', function (event) {
        show_scenic(event);
        isClick = true;
        var id = $(this).attr('data-num');
        var textli = $(this).attr('data-text');
        $('#datanum').val(id);
        $('#datatextli').val(textli);
        /*$('#taskId').val(id);
         $('#categoryName1').val(categoryName);*/
        var thistext = $(this).text();
        $('#selectdiv .left').text(thistext);
       /* $('#category .category_right').addClass('none');*/
        $('#formId3').submit();

    });


    getEntityTable();
    if ($('#pageNo').val() == "" || $('#pageNo').val() == undefined) {
        $('#pageNo').val(1)
    }

    /*点击查看*/
    $('body').on('click', '.looked', function () {
        var datataskId = $(this).parents('tr').attr('data-taskId');  //品类id
        var datacategoryname = $(this).parents('tr').attr('data-categoryname');  //品类名称
        var datachildtaskid = $(this).parents('tr').attr('data-childtaskid');  //子任务id
        $('#datachildtaskid').val(datachildtaskid);
        $('#datacategoryname').val(datacategoryname);
        $('#datataskId').val(datataskId);
        $('#formId0').submit();
    })
});
function downlist() {
    $.ajax({
        type: "post",
        url: '/realtime/category/list',
        dataType: "json",
        success: function (data) {
            //console.log(JSON.stringify(data));
            var nn = data.categoryList;
            for (var i = 0; i < nn.length; i++) {
                $('#hoverli').append('<li data-index="' + i + '">' + nn[i].categoryName + '</li>');
                $('#taskname p').html('');
            }
            ;

            $('#hoverli li').hover(function () {
                $('#category .category_right').removeClass('none');
                $('#taskname p').html('');
                var litext = $(this).text();
                var thisidex = $(this).attr('data-index');
                $(this).addClass('currentli').siblings().removeClass('currentli');
                var html = "";
                if (nn[thisidex].taskList.length == 0) {
                    html += '<p style="text-align: center">该品类没有对应的任务名称</p>';
                }
                for (var j = 0; j < nn[thisidex].taskList.length; j++) {
                    var taskName = $('#taskName').val();
                    if (nn[thisidex].taskList[j].taskName != "" && nn[thisidex].taskList[j].taskName == taskName) {
                        html += '<span class="red" data-num="' + nn[thisidex].taskList[j].taskId + '" data-text="' + litext + '">' + nn[thisidex].taskList[j].taskName + '</span>';
                    } else {
                        html += '<span data-num="' + nn[thisidex].taskList[j].taskId + '" data-text="' + litext + '">' + nn[thisidex].taskList[j].taskName + '</span>';
                    }
                    ;
                }
                $('#taskname p').append(html);
            });
            $('#hoverli li').click(function (event) {
                show_scenic(event);
            });

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $.MsgBox.Alert("温馨提示", "访问网络失败！" + errorThrown);
        }
    });
};

//添加刪除类名
function toggclass(ele, le) {
    $(ele).addClass('red').siblings(le).removeClass('red');
};
function datatextt(ele) {
    for (var i = 0; i < 25; i++) {
        $(ele).append('<i>' + String.fromCharCode((65 + i)).toUpperCase() + '</i>');
    }
}

//分页代码
function getEntityTable() {
    // debugger;
    // 调用分页
    var categoryName = $('#categoryName').val();
    var currenttaskId = $('#currenttaskId').val();
    var pageNo = $("#uniquePageNo").val();
    $('#pageNo').val(pageNo);
    $.ajax({
        url: "/realtime/task/child",
        async: false,
        type: "post",
        data: {"pageNo": pageNo, "taskId": currenttaskId, "categoryName": categoryName},
        dataType: "html",
        success: function (data) {
            $("#task_child_list").html(data);
            // $(".leftNav").css("height",$(".comment").height()+120);
        }
    });
};
function sortfn(orderParam) {
    var categoryName = $('#categoryName').val();
    var currenttaskId = $('#currenttaskId').val();
    $('#orderParam').val(orderParam);
    $.ajax({
        type: "post",
        data: {"taskId": currenttaskId, "categoryName": categoryName, "orderParam": orderParam},
        url: '/realtime/task/child',
        dataType: "html",
        success: function (data) {
            $("#task_child_list").html(data);
            // $(".leftNav").css("height",$(".comment").height()+120);
        }
    })
}