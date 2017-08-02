$(function(){
   leftNav($(".navBar ul li")[1]);
    $('.navBar li').hover(function(){
        leftNav(this);
    },function(){
        leftNav($(".navBar ul li")[1]);
    })
  /*  // 预警及活动列表页面
    $('#nenu li').click(function(){
       var text = $(this).find('a').text();
        $('#dropdownMenu1 span.left').html(text);
    });
    // tab栏切换
    $('#chooseul li').click(function(){
       $(this).addClass('chooseli').siblings().removeClass('chooseli');
        var thisindex = $(this).index();
        $('#table_f table').eq(thisindex).removeClass('none').siblings('table').addClass('none');
    });*/
    $("#scrollDiv").Scroll({line:1,speed:500,timer:3000});//可修改line值，speed值，timer值



});

//滚动插件
(function($){
    $.fn.extend({
        Scroll:function(opt,callback){
            //参数初始化
            if(!opt) var opt={};
            var _this=this.eq(0).find("ul:first");
            var        lineH=_this.find("li:first").height(), //获取行高
                line=opt.line?parseInt(opt.line,10):parseInt(this.height()/lineH,10), //每次滚动的行数，默认为一屏，即父容器高度
                speed=opt.speed?parseInt(opt.speed,10):500, //卷动速度，数值越大，速度越慢（毫秒）
                timer=opt.timer?parseInt(opt.timer,10):3000; //滚动的时间间隔（毫秒）
            if(line==0) line=1;
            var upHeight=0-line*lineH;
            //滚动函数
            scrollUp=function(){
                _this.animate({
                    marginTop:upHeight
                },speed,function(){
                    for(i=1;i<=line;i++){
                        _this.find("li:first").appendTo(_this);
                    }
                    _this.css({marginTop:0});
                });
            }
            //鼠标事件绑定
            _this.hover(function(){
                clearInterval(timerID);
            },function(){
                timerID=setInterval("scrollUp()",timer);
            }).mouseout();
        }
    })
})(jQuery);


//首页点击跳转页面
function linkurl(url) {
    window.open(url);
}

