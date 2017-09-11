<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Facebook JavaScript SDK 取得 AccessToken 範例</title>
    <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.7.1.min.js" type="text/javascript"></script>
    <script src="http://connect.facebook.net/zh_TW/all.js"></script>
</head>
<body onLoad="login();">
    <div id="fb-root">
    </div>
    <p>
        <span id="uid"></span>
        <br>
        <span id="accessToken"></span>
    </p>
    <script>
FB.init({ appId: '508973512456925',
    status: true,
    cookie: true,
    xfbml: true,
    oauth: true
});

function login() {
    FB.login(function (response) {
        FB.getLoginStatus(function (response) {
            if (response.status === 'connected') {  // 程式有連結到 Facebook 帳號
                var uid = response.authResponse.userID; // 取得 UID
                var accessToken = response.authResponse.accessToken; // 取得 accessToken
                $("#uid").html("UID：" + uid);
                $("#accessToken").html("accessToken：" + accessToken);
                
            // The rest of this code assumes you are not using a library.
            // It can be made less wordy if you use one.
            var form = document.createElement("form");
            form.setAttribute("method", "post");
            form.setAttribute("action", "GetUserTimeNoteServlet");
            
            
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", "webUserId");
            hiddenField.setAttribute("value", uid);
            
            form.appendChild(hiddenField);
            document.body.appendChild(form);    // Not entirely sure if this is necessary
            form.submit();
            
            } else if (response.status === 'not_authorized') {  // 帳號沒有連結到 Facebook 程式
                alert("請允許授權！");
            } else {    // 帳號沒有登入
                // 在本例子中，此段永遠不會進入...XD
            }
        });
    }, { scope: "email" });
    
};
    </script>
</body>
</html>
