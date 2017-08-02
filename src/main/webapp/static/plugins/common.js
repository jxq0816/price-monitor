

// 左侧导航栏

function leftNav(ev){
    $(ev).addClass('selfColor').siblings('li').removeClass('selfColor');
    $(ev).find('img.right_arrow').css('visibility','hidden');
    $(ev).siblings('li').find('img.right_arrow').css('visibility','visible');
}

// function realtime(ev) {
//     var childsibling = $(ev).siblings();
//     for (var i = 0; i < childsibling.length; i++) {
//         childsibling[i].className = "";
//         childsibling[i].className = "realtime_defaults";
//     }
//     ev.className = "realtime";
//     ev.parentNode.parentNode.className = "defaults";
// }


function moreInfo() {
    $(".userinfo").addClass("userinfos");
    $(".menu").toggleClass("menus");
}
function userInfo() {
    $(".menu").addClass("menus");
    $(".userinfo").toggleClass("userinfos");
}

$(document).on("click",function ()
{//对document绑定一个影藏Div方法
    $(".userinfo").addClass("userinfos");
    $(".menu").addClass("menus");
});
function show_scenic(e){
    if (e.stopPropagation)
        e.stopPropagation();
    else
        e.cancelBubble = true;
};


(function () {
    $.MsgBox = {
        Alert: function (title, msg) {
            GenerateHtml("alert", title, msg);
            btnOk();  //alert只是弹出消息，因此没必要用到回调函数callback
            btnNo();
        },
        Confirm: function (title, msg, callback) {
            GenerateHtml("confirm", title, msg);
            btnOk(callback);
            btnNo();
        }
    }
    //生成Html
    var GenerateHtml = function (type, title, msg) {
        var _html = "";
        _html += '<div id="mb_box"></div><div id="mb_con"><span id="mb_tit">' + title + '</span>';
        _html += '<a id="mb_ico">x</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
        if (type == "alert") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        if (type == "confirm") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
            _html += '<input id="mb_btn_no" type="button" value="取消" />';
        }
        _html += '</div></div>';
        //必须先将_html添加到body，再设置Css样式
        $("body").append(_html);
        //生成Css
        GenerateCss();
    }

    //生成Css
    var GenerateCss = function () {
        $("#mb_box").css({ width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
            filter: 'Alpha(opacity=60)', backgroundColor: 'black', top: '0', left: '0', opacity: '0.6'
        });
        $("#mb_con").css({ zIndex: '999999', width: '400px', position: 'fixed',
            backgroundColor: 'White', borderRadius: '15px'
        });
        $("#mb_tit").css({ display: 'block', fontSize: '14px', color: '#444', padding: '10px 15px',
            backgroundColor: '#DDD', borderRadius: '15px 15px 0 0',
            borderBottom: '3px solid #009BFE', fontWeight: 'bold',textAlign:"center"
        });
        $("#mb_msg").css({ padding: '20px', lineHeight: '20px',
            borderBottom: '1px dashed #DDD', fontSize: '13px',textAlign:"center"
        });
        $("#mb_ico").css({ display: 'block', position: 'absolute', right: '10px', top: '9px',
            border: '1px solid Gray', width: '18px', height: '18px', textAlign: 'center',
            lineHeight: '16px', cursor: 'pointer', borderRadius: '12px', fontFamily: '微软雅黑'
        });
        $("#mb_btnbox").css({ margin: '15px 0 10px 0', textAlign: 'center' });
        $("#mb_btn_ok,#mb_btn_no").css({ width: '85px', height: '30px', color: 'white', border: 'none' });
        $("#mb_btn_ok").css({ backgroundColor: '#168bbb' });
        $("#mb_btn_no").css({ backgroundColor: 'gray', marginLeft: '20px' });
        //右上角关闭按钮hover样式
        $("#mb_ico").hover(function () {
            $(this).css({ backgroundColor: '#3789DD', color: 'White' });
        }, function () {
            $(this).css({ backgroundColor: '#DDD', color: 'black' });
        });
        var _widht = document.documentElement.clientWidth;  //屏幕宽
        var _height = document.documentElement.clientHeight; //屏幕高
        var boxWidth = $("#mb_con").width();
        var boxHeight = $("#mb_con").height();
        //让提示框居中
        $("#mb_con").css({ top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px" });
    }
    //确定按钮事件
    var btnOk = function (callback) {
        $("#mb_btn_ok").click(function () {
            $("#mb_box,#mb_con").remove();
            if (typeof (callback) == 'function') {
                callback();
            }
        });
    }
    //取消按钮事件
    var btnNo = function () {
        $("#mb_btn_no,#mb_ico").click(function () {
            $("#mb_box,#mb_con").remove();
        });
    }
})();


// 判断任务条数是否超出限制
function addNewTask() {
    $.ajax({
        url: "/monitors/taskConfiguration/userTaskNum",
        type: "post",
        async:false,
        dataType : "json",
        success: function (result) {
            // 为true已超出限制   false未超出限制
            if(result){
                $.MsgBox.Alert("温馨提示","超出限制任务条数，请购买使用！");
                return false;
            }else{
                window.location.href="/monitors/addmonitor";
            }
        }
    });
}


window.onload=function () {
    $(".leftNav").css("height",$(".comment").height()+120);
}



//图表展示
function echartes(id,data,activity) {
    var lineChart = echarts.init(document.getElementById(id));
    var flag;
    if(activity){
        flag = true;
    }else{
        flag = false;
    }
    var option = {
        title: {
            text: '监测产品价格变化占比',
            x: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'line',
            },
            showDelay: 0,
            hideDelay: 50,
            transitionDuration:0,
            backgroundColor : '#797979',
            borderRadius : 8,
            padding: 10,

            formatter: function (params) {
                if(flag){
                    var paramsvalue = params[0].value;
                    var paramsactivity = activity[params[0].dataIndex];
                    if(params[0].value=="" ||params[0].value==null || params[0].value==undefined){
                        paramsvalue=""
                    }else{
                        paramsvalue=params[0].value+'元'
                    };
                    if(activity[params[0].dataIndex] =="" || activity[params[0].dataIndex] ==null || activity[params[0].dataIndex]==undefined){
                        paramsactivity="无活动"
                    }

                    var res = '日期 :' + params[0].name;
                    res += '<br/> 价格:'+ paramsvalue;
                    res += '<br/> 活动内容：'+paramsactivity;
                }else {
                    var res = '日期 :' + params[0].name;
                    res += '<br/> 百分比：'+ params[0].value +"%";
                }
                return res;
            }
        },
        grid: {
            left: "7%",
            right: "7%",
        },
        toolbox: {
            show: true,
            feature: {
                magicType:{
                    type: ['line', 'bar']
                }

            }
        },
        xAxis:  {
            type: 'category',
            boundaryGap: false,
            data: data.dateTime
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value} %'
            }
        },
        series: [
            {
                name:'价格变化',
                type:'line',
                barWidth:50,
                data:data.datas,
                markPoint: {
                    itemStyle:{
                        normal:{
                            label:{
                                show: true,
                                formatter: function (param) {
                                    return param.value+"%";
                                }
                            }
                        }
                    },
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
            }
        ]
    };

    if(activity){
        option.dataZoom={
            show : true,
                realtime : true,
                start : 40,
                end : 60
        };

        option.yAxis= {
            type: 'value',

        };
        option.title = {
            text: '监测产品价格变化',
            x: 'center'
        },
        option.series[0].markPoint.itemStyle.normal.label={
                show: true,
                formatter: function (param) {
                    return param.value;
                }
        }
    }
    lineChart.setOption(option);
    window.onresize = lineChart.resize;
}

