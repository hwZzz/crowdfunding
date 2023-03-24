<%--
  Created by IntelliJ IDEA.
  User: 47991
  Date: 2022/1/18
  Time: 16:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <script src="https://s3.pstatp.com/cdn/expire-1-M/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function (){

            $("#btn04").click(function (){
                layer.msg("layer的弹框");
            });

            $("#btn03").click(function () {
                // 准备要发送的数据
                var student = {
                    "stuId" : 5,
                    "stuName": "tom",
                    "address":{
                        "province": "广东",
                        "city": "深证",
                        "street": "后端"
                    },
                    "subjectList":[
                        {
                            "subName": "JavaSE",
                            "subScore": 100
                        },
                        {
                            "subName": "c++",
                            "subScore": 98
                        }
                    ],
                    "map":{
                        "key1": "value1",
                        "key2": "value2"
                    }
                };
                // 将JSON对象转换为JSON字符串
                var requestBody = JSON.stringify(student);
                // 发送Ajax请求
                $.ajax({
                    url: "send/compose/object.json",
                    type: "post",
                    data: requestBody,
                    contentType: "application/json;character=UTF-8",
                    dataType: "json",
                    success: function (resp){
                        console.log(resp);
                    },
                    error: function (resp) {
                        console.log(resp)
                    }
                })

            });

            $("#btn01").click(function (){
                $.ajax({
                    "url": "send/array.html",
                    "type": "post",
                    "data": {
                        "array":[5,8,12]
                    },
                    "dataType": "text",
                    "success": function (response){
                        alert(response);
                    },
                    "error": function (response){
                        alert(response);
                    }
                });
            });


            $("#btn02").click(function (){
                // 准备好要发送到服务端的数组
                var array = [5,8,12];

                // 将JSON数组转换为JSON字符串
                var requestBody = JSON.stringify(array);
                $.ajax({
                    "url": "send/array/three.html",
                    "type": "post",
                    "data": requestBody,
                    contentType: "application/json;character=UTF-8",
                    "dataType": "text",
                    "success": function (response){
                        alert(response);
                    },
                    "error": function (response){
                        alert(response);
                    }
                });
            });
        })
    </script>
</head>
<body>
<a href="test/ssm.html">测试</a>
<button id="btn01">Send Text</button>
<button id="btn02">Send Three</button>
<button id="btn03">Send ComposeObject</button>
<button id="btn04">layer</button>
</body>
</html>
