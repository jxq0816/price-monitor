/**
 * Created by dujun on 2017/6/8.
//  */
$(function () {
    leftNav($(".navBar ul li")[2]);
    $('.navBar li').hover(function(){
        leftNav(this);
    },function(){
        leftNav($(".navBar ul li")[2]);
    })
});

// 提示信息的显示与隐藏
function prompt(){
    $("#prompt").show();
}

function promptes(){
    $("#prompt").hide();
}

// 点击筛选任务信息框“×”时，隐藏该元素
function cancel(){
    $(".leftNav").css("height",$(".addnew_monitor").height()+180);
    $("#taskinfo").hide();
    $(".addnew_monitor").show();
}


//任务名称验证
function checkTaskName(ids) {
    var usernameVal = $("#taskName");
    var usertipval = $("#taskTips");
    var flag = true;

    if($.trim(usernameVal.val())==""){
        usertipval.html("");
        usernameVal.css("border-color","#ccc");
        flag =  false;
    }
    if (!$.trim(usernameVal.val()).match( /^[\u4e00-\u9fa5|a-zA-Z0-9]*$/)) {
        usertipval.html("必须由汉字、英文字母和数字组成");
        usernameVal.css("border-color","red");
        flag =  false;
    } else if(chEnWordCount($.trim(usernameVal.val())) < 6){
        usertipval.html("小于6个字符！");
        usernameVal.css("border-color","red");
        flag =  false;
    } else if(chEnWordCount($.trim(usernameVal.val())) > 20){
        usertipval.html("大于20个字符！");
        usernameVal.css("border-color","red");
        flag =  false;
    }
    else{
        usertipval.html("");
        usernameVal.css("border-color","#ccc");
        flag =  true;
    }
   $.ajax({
        url: "/monitors/getAttribute/isTaskNameExists",
        type: "post",
        async : false,
        data: {"taskName": $.trim(usernameVal.val()),"taskId": ids},
        dataType: "json",
        success: function (data) {
            if(data){
                usertipval.html("任务名称不能重复！");
                usernameVal.css("border-color","red");
                flag =  false;
            }
        }
    });
    return flag;
}


// 中英文统计(一个中文算两个字符)
function chEnWordCount(str){
    var count = str.replace(/[^\x00-\xff]/g,"**").length;
    return count;
}


//邮箱格式验证
function taskEmail(){
    var taskemail = $("#taskemail");
    var emailTips = $("#emailTips");
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    if($.trim(taskemail.val())==""){
        emailTips.html("");
        taskemail.css("border-color","#ccc");
        return false;
    }

    if(  reg.test($.trim(taskemail.val()))){
        emailTips.html("");
        taskemail.css("border-color","#ccc");
        return true;
    } else{
        emailTips.html("邮箱格式不正确！");
        taskemail.css("border-color","red");
        return false;
    }
}

// 点击下一步时，显示筛选到的任务信息
function get_task(){
    var conditionName = $("#formsearch").serialize();
    conditionName = decodeURIComponent(conditionName,true);
    var conditionArr =  conditionName.split("&");
    // 除任务名称和邮箱，剩余的筛选条件
    var conditionObj=new Object();
    // 全部筛选条件
    var conditionName=new Object();
    var platForm=new Array();
    // 任务名称
    var firstName = conditionArr[0].split("=");
    if(firstName[1]){
        var taskCheckName = checkTaskName();
        if( !taskCheckName ){
            return false;
        }
        var names = firstName[0];
        conditionName[names] = firstName[1];
    } else{
        $.MsgBox.Alert("温馨提示","任务名称不能为空！");
        return false;
    }
    if($("#category").val()==""){
        $.MsgBox.Alert("温馨提示","添加品类名称！");
        return false;
    }
    // 其他筛选条件
    for( var i=1;i< conditionArr.length-1;i++){
        var singleName = conditionArr[i].split("=");
        var names = singleName[0];
        if( singleName[1]&&singleName[1]!="on"){
            conditionObj[names] = singleName[1];
            conditionName[names] = singleName[1];
        } else if(singleName[1]=="on"){
            platForm.unshift(singleName[0]);
        } else{
            $.MsgBox.Alert("温馨提示","筛选条件不能为空！");
            return false;
        }
        conditionObj.platform = platForm.join(",");
        conditionName.platform = platForm.join(",");

    }
    if(conditionObj.platform=="" ){
        $.MsgBox.Alert("温馨提示","监测电商平台不能为空！");
        return false;
    }
    // 邮箱
    var lastName = conditionArr[conditionArr.length-1].split("=");
    if(lastName[1]){
        var taskEmails = taskEmail();
        if( !taskEmails ){
            return false;
        }
        var names = lastName[0];
        conditionName[names] = lastName[1];
    }else{
        $.MsgBox.Alert("温馨提示","邮箱不能为空！");
        return false;
    }

    $.ajax({
            url: "/monitors/taskConfiguration/userTaskNum",
            type: "post",
            async: false,
            dataType: "json",
            success: function (result) {
                // 为true已超出限制   false未超出限制
                if (result) {
                    $.MsgBox.Alert("温馨提示", "超出限制任务条数，请购买使用！");
                    return false;
                } else {
                    // 判断筛选条件是否已存在
                    $.ajax({
                        url: "/monitors/getAttribute/isTaskConditionExists",
                        type: "post",
                        async: false,
                        data: {"condition": JSON.stringify(conditionObj)},
                        // dataType : "html",
                        success: function (result) {
                            if (result == "true") {
                                $.MsgBox.Alert("温馨提示", "筛选条件已存在！");
                                return false;
                            } else {
                                $.ajax({
                                    url: "/monitors/getAttribute/getStickPrices",
                                    type: "post",
                                    // async:false,
                                    data: {"condition": JSON.stringify(conditionObj)},
                                    dataType: "html",
                                    success: function (results) {
                                        $("#formTable").html(results);
                                        $(".addnew_monitor").hide();
                                        $("#taskinfo").show();
                                        $(".leftNav").css("height", $(".comment").height() + 120);
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });
}




// 点击筛选条件时，判断其上的筛选条件是否没有选择
function categoryIsnull(ev) {
    var nextName = $(ev.parentNode).prev();
    if( !$(nextName.find("select")[0]).val() && ev.name!="brand" ){
        $(ev).attr("disabled","disabled");
        $.MsgBox.Alert("温馨提示","前面的筛选条件没有选择！");
    }
    $(ev).removeAttr("disabled");
}
// 任务品类条件
function categoryName(ev) {
    var conditionNames = $("#category").val();
    $.ajax({
        url: "/monitors/getAttribute",
        type: "post",
        async:false,
        data: {"categoryName": conditionNames},
        dataType : "html",
        success: function (data) {
            $("#task_condition").html(data);
        }
    });
    categoryNames(ev);
}

// 筛选条件级联
function categoryNames(ev) {
    var nestAllName = $(ev.parentNode).nextAll();
    if(ev.name!="category_name"){
        for(var i=0;i<nestAllName.length;i++){
            if($(nestAllName[i]).find("select")[0]){
                $($(nestAllName[i]).find("select")).html("<option value>请选择</option>");
            }else{
                var supplierName = $(".supplierName");
                if(supplierName){
                    $($(nestAllName[i]).find(supplierName)).html("");
                }
            }
        }

    }
    var conditionName = $("#formsearch").serialize();
    conditionName = decodeURIComponent(conditionName,true);
    var conditionArr =  conditionName.split("&");
    var conditionObj=new Object();

    for( var i=1;i< conditionArr.length-1;i++){
        var singleName = conditionArr[i].split("=");
        if( singleName[1] && singleName[1]!="on"){
            var names = singleName[0];
            conditionObj[names] = singleName[1];
        }
    }

    var nextName = $(ev.parentNode).next().find("select");

    if( ev.localName =="select"){
        if(nextName.length){
            conditionObj.distinctValue = nextName[0].name;
        }else{
            var supplierName = $(".supplierName");
            // console.log($(ev.parentNode).next().find(supplierName))
            conditionObj.distinctValue = $(ev.parentNode).next().find(supplierName)[0].id
        }
        $.ajax({
            url: "/monitors/getAttribute/value",
            type: "post",
            data: {"condition": JSON.stringify(conditionObj)},
            // dataType : "json",
            success: function (result) {
                var resultArr = result.split(",");
                if(ev.name == "seller_name"){
                    var html ='';
                    for( var i=0;i<resultArr.length-1;i++){
                        html +='<input type="checkbox"  name="'+ resultArr[i]+'">' +resultArr[i];
                    }
                    $(".supplierName").html(html);
                }else{
                    var html ='<option value>请选择</option>';
                    for( var i=0;i<resultArr.length-1;i++){
                        html +='<option value="'+resultArr[i] +'">'+resultArr[i]+'</option>'
                    }
                    $($(nextName[0])).html(html);
                }

            }
        });
    }

}


// 筛选出来任务信息，全选
function selectTaskAll() {
    if ($("#task_all").prop("checked")) {
        $("#formInfo table tbody input[type=checkbox]").prop("checked", true);
    } else {
        $("#formInfo table tbody input[type=checkbox]").prop("checked", false);
    }


}

// 提交筛选出来的信息
function submitTaskInfo(event) {
    $(event).attr("disabled","disabled");
    var conditionName = $("#formsearch").serialize();
    conditionName = decodeURIComponent(conditionName,true);
    var conditionArr =  conditionName.split("&");
    // 全部筛选条件
    var conditionName=new Object();
    var platForm=new Array();
    var taskInfoArr=new Array();
    // 其他筛选条件
    for( var i=0;i< conditionArr.length;i++){
        var singleName = conditionArr[i].split("=");
        var names = singleName[0];
        if( singleName[1]&&singleName[1]!="on"){
            conditionName[names] = singleName[1];
        } else if(singleName[1]=="on"){
            platForm.unshift(singleName[0]);
        }
    }
    conditionName.platform = platForm.join(",");
    var flag = true;
    var trLen = $("#formTable table tbody tr");
    for(var i=0;i<trLen.length;i++){
        var selectInfo = $(trLen[i]).find("input[type=checkbox]").prop("checked");
        var selectText = $(trLen[i]).find("input[type=text]")[0];
        var warningPrice = $(selectText).val();
        var currentPrice = $(selectText).parent().prev()[0].innerHTML;

        if(parseFloat(warningPrice)>=parseFloat(currentPrice)){
            $(selectText).css("borderColor","red");
            $(event).removeAttr("disabled");
            $.MsgBox.Alert("温馨提示","预警价格必须小于当前成交价！");
            flag = false;
        }else{
            $(selectText).css("borderColor","#666");
        }
        var selectInfoTd = $(trLen[i]).find("td");
        if( selectInfo){
            var taskCondition = new Object();
            for(var j=0;j<selectInfoTd.length;j++){
                if(j==0){
                    taskCondition.page_url=selectInfoTd[j].innerHTML;
                }else if( j==2){
                    taskCondition.product_description=selectInfoTd[j].innerHTML;
                }else if( j==3){
                    taskCondition.seller_name=selectInfoTd[j].innerHTML;
                }else if( j==5){
                    taskCondition.warning_price=$(selectInfoTd[j]).find("input").val();
                }
            }
            taskInfoArr.push(taskCondition);
        }
    }
    if(taskInfoArr.length&&flag){
        $.ajax({
            url: "/monitors/getAttribute/saveTask",
            type: "post",
            async:false,
            data: {"condition": JSON.stringify(conditionName),"taskInfo":JSON.stringify(taskInfoArr)},
            dataType : "json",
            success: function (result) {
                // 如果添加数据成功，则跳到我的监测列表界面
                if( result){
                    window.location.href="/monitors/taskConfiguration";
                }
            }
        });
    }else{
        $(event).removeAttr("disabled");
        $.MsgBox.Alert("温馨提示","请选择一条任务！");
        return false;
    }
}

// 修改任务信息
function revise_task(taskid) {
   var flag=true;
    if($("#taskName").val()){
        var taskCheckNames = checkTaskName(taskid);
        if( !taskCheckNames ){
            flag= false;
        }
    } else{
        $.MsgBox.Alert("温馨提示","任务名称不能为空！");
        flag= false;
    }
    if($("#taskemail").val()){
        var taskEmails = taskEmail();
        if( !taskEmails ){
            flag= false;
        }
    }else{
        $.MsgBox.Alert("温馨提示","邮箱不能为空！");
        flag= false;
    }
    if(flag){
        $.ajax({
            url: "/monitors/taskConfiguration/getTaskZb",
            type: "post",
            // async:false,
            data: {"taskConfigurationId": taskid},
            dataType : "html",
            success: function (results) {
                $("#formTable").html(results);
                $(".addnew_monitor").hide();
                $("#taskinfo").show();
                $(".leftNav").css("height",$(".comment").height()+120);
            }
        });
    }


}


// 提交修改的信息
function submitReviseInfo(ids) {
    var coniditionTotal = new Object();
    var coniditionInfo = new Object();
    var updateTaskArr = new Array();
    var deleteTaskArr = new Array();
    coniditionInfo.id = ids;
    coniditionInfo.task_name = $("#taskName").val();
    coniditionInfo.email = $("#taskemail").val();
    var flag = true;
    var trLen = $("#formTable table tbody tr");
    for(var i=0;i<trLen.length;i++){
        var selectInfo = $(trLen[i]).find("input[type=checkbox]").prop("checked");
        var selectText = $(trLen[i]).find("input[type=text]")[0];

        var warningPrice = $(selectText).val();
        var currentPrice = $(selectText).parent().prev()[0].innerHTML;
        if(parseFloat(warningPrice)>=parseFloat(currentPrice)){
            $(selectText).css("borderColor","red");
            $.MsgBox.Alert("温馨提示","预警价格必须小于当前成交价！");
            flag = false;
        }else{
            $(selectText).css("borderColor","#666");
            var selectInfoTd = $(trLen[i]).find("td");
            if( selectInfo ){
                var taskCondition = new Object();
                for(var j=0;j<selectInfoTd.length;j++){
                    if(j==0){
                        taskCondition.id=selectInfoTd[j].innerHTML;
                    }else if( j==5){
                        taskCondition.warning_price=$(selectInfoTd[j]).find("input").val();
                    }
                }
                updateTaskArr.push(taskCondition);
            }else {
                for(var j=0;j<selectInfoTd.length;j++){
                    if(j==0){
                        deleteTaskArr.push(selectInfoTd[j].innerHTML);
                    }
                }
            }
        }
    }
    // 父任务信息
    coniditionTotal.updateTaskConfiguration = JSON.stringify(coniditionInfo);
    // 子任务添加信息
    coniditionTotal.updateTaskConfigurationZbs = JSON.stringify(updateTaskArr);
    // 子任务删除id
    coniditionTotal.deleteTaskZbs = JSON.stringify(deleteTaskArr);
    if(updateTaskArr.length && flag){
        $.ajax({
            url: "/monitors/getAttribute/updateTask",
            type: "post",
            async:false,
            data: coniditionTotal,
            dataType : "json",
            success: function (result) {
                // 如果添加数据成功，则跳到我的监测列表界面
                if( result){
                    window.location.href="/monitors/taskConfiguration";
                }
            }
        });
    }else{
        $.MsgBox.Alert("温馨提示","请选择一条任务！");
        return false;
    }

}


// 子任务选择时，全选状态改变
function selAllStatus() {
    var taskSelect = $("#formInfo table tbody input[type=checkbox]");
    var status = true;
    for(var i=0;i< taskSelect.length;i++){
        /*console.log($(taskSelect[i]).prop("checked"));*/
        if(!$(taskSelect[i]).prop("checked")){
            status = $(taskSelect[i]).prop("checked");
            break;
        }
    }
    if(!status){
        $("#task_all").prop("checked",false);
    } else{
        $("#task_all").prop("checked",true);
    }
}


