$(document).ready(function () {
    $(".commodity").children()[0].className = "selcategory";
    shoes($(".commodity").children()[0]);
    // echartes();
    // $("#priceChart").html("暂无数据");
    leftNav($(".navBar ul li")[0]);
    $('.navBar li').hover(function(){
        leftNav(this);
    },function(){
        leftNav($(".navBar ul li")[0]);
    })

});





function shoes(ev) {
    var shoes = $(ev).siblings("a");
    for (var i = 0; i < shoes.length; i++) {
        shoes[i].className = "";
    }
    ev.className = "selcategory";
    var categoryName = ev.innerHTML;
    $.ajax({
        url: "/industryList/getIndustryInfo",
        type: "post",
        async:false,
        data: {"goodsName": categoryName,"selNum": 10},
        dataType : "html",
        success: function (html) {
            $("#industry_content").html(html);
            $(".leftNav").css("height",document.body.scrollHeight);
            var averageTr = $(".averagePrice table tbody tr");
            var dropRankLi = $(".dropRanking table tbody tr");
            var backPrice = ["#f17113","#89b431","#e94775","#d6d6d6"];
            var backDropRank = ["#f17113","#7bafee","#e94775","#89b431","#b5159f","#4472C4","#92D050","#FF8040","#0080FF","#FF44FF  "];
            for(var i=0;i<averageTr.length;i++){
                if(averageTr[i].children[0].children.length){
                    if(i<3){
                        $(averageTr[i].children[0].children).css("background-color",backPrice[i]);
                    }else{
                        $(averageTr[i].children[0].children).css("background-color",backPrice[3]);
                    }
                }
            }
            for(var i=0;i<dropRankLi.length;i++){
                if(dropRankLi[i].children[1]){
                    $(dropRankLi[i].children[1].children).css("background-color",backDropRank[i]);
                }
            }
            $.ajax({
                url: "/industryList/getPriceRatioRank",
                type: "post",
                async:false,
                data: {"goodsName": categoryName,"selNum": 30},
                dataType : "json",
                success: function (data) {
                    var obj = {dateTime:[],datas:[]};
                    for(var i=0;i<data.ratioList.length;i++){
                        obj.datas.unshift(data.ratioList[i]*100);
                    }
                    for(var i=0;i<data.timeList.length;i++){
                        obj.dateTime.unshift(data.timeList[i]);
                    }
                    // 行业榜单图表
                    echartes('priceChart',obj);
                }
            });
        }
    });
}




