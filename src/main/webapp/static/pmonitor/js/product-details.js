$(function () {
    //点击更多
    $('.morebtn').click(function () {
        var thisnum = $(this).attr('data-num');
        $('#moreType').val(thisnum);
        $('#formId1').submit();
    })


    var chartsdataId = $('#chartsdataId').val();
    var arrParse = JSON.parse(chartsdataId);
    // var arrParse =  eval('(' + chartsdataId + ')');
    var pricedata = [];
    var mydate = [];
    var activity_content = [];
    // console.log(arrParse.length);
    for (var i = 0; i < arrParse.length; i++) {
        pricedata.push(arrParse[i].price);
        mydate.push(arrParse[i].date);
        activity_content.push(arrParse[i].info);
    }
    var obj = new Object();
    obj.datas = pricedata;
    obj.dateTime = mydate;
    // 产品详情图表
    echartes("line", obj, activity_content);
});

