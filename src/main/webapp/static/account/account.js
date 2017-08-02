//打开登录页面
function gotoLogin(){
    var url = window.location.href;
    window.location.href="/account/login?originalUrl=" + encodeURIComponent(url);
};

function gotoRegister(){
    var url = window.location.href;
    window.location.href="/account/register?originalUrl=" + encodeURIComponent(url);
};


function loginOut(){
    $.ajax({
       url : "/account/logout",
       type : "post",
       async:false,
       dataType : "json",
       success : function(){
           window.location.reload();
       }
   });
};


//判断是否登录
function casLogin(){
    var url = window.location.href;
    $.ajax({
       url : "/account/autoLogin",
       type : "post",
       data:{"originalUrl":url},
       async:true,
       dataType : "text",
       success : function(ticket){
           if(ticket && $.trim(ticket) != ""){
               window.location.href="/account/callback?ticket=" + ticket;
           }
       }
   });
};