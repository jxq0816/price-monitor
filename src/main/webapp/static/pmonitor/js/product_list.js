$(function(){
    var moreType = $('#moreType').val();
    if(moreType==0){
        $('#chooseul li:first').addClass('chooseli').siblings('li').removeClass('chooseli');
    }else{
        $('#chooseul li:last').addClass('chooseli').siblings('li').removeClass('chooseli');
    }
    // tab栏切换
     $('#chooseul li').click(function(){
      var datamoreType = $(this).attr('data-moreType');
      $('#moreType').val(datamoreType)
      $(this).addClass('chooseli').siblings().removeClass('chooseli');
      $("#table_f").html('');
      getTableData();
     });
    getTableData();
});
//分页代码
function getTableData(){
    var moreType = $('#moreType').val();
    var pageNo = $("#uniquePageNo").val();
    var childTaskId = $('#childTaskId').val();
    $.ajax({
        url : "/realtime/task/child/detail/more/page",
        async: false,
        type : "post",
        data:{"pageNo":pageNo,"moreType":moreType,"childTaskId":childTaskId},
        dataType : "html",
        success : function(data){
            $("#table_f").html(data);
        }
    });
};