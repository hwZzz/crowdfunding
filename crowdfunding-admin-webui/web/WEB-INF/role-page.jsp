<%--
  Created by IntelliJ IDEA.
  User: 47991
  Date: 2022/3/22
  Time: 17:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css">
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/my-role.js"></script>
<script type="text/javascript">
    $(function (){
        //1.为分页操作准备初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";

        // 2. 调用分页函数，实现分页效果
        generatePage();

        //3.查询
        $("#searchBtn").click(function (){
            window.keyword = $("#keywordInput").val();
            generatePage();
        });
        // 4. 点击新增打开模态框
        $("#showAddModalBtn").click(function (){
            $("#addModal").modal("show");
        });

        // 5.模态框数据保存
        $("#saveRoleBtn").click(function (){
            // 1.获取用户在模态框中输入的角色名，trim去前后空格
            var roleName = $.trim($("#addModal [name=roleName]").val());

            console.log("打印：roleName"+roleName);
            // 发送ajax请求
            $.ajax({
                url: "role/save.json",
                type: "post",
                data: {
                    name: roleName
                },
                dataType: "json",
                success: function (resp){
                    let result = resp.result;
                    if (result === "SUCCESS") {
                        layer.msg("已保存");

                        // 重新加载分页
                        window.pageNum = 999999;
                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！"+resp.message);
                    }
                },
                error: function (resp) {
                    layer.msg(resp.status+""+resp.statusText);
                }
            });

            // 关闭模态框
            $("#addModal").modal("hide");

            // 清理模态框内容
            $("#addModal [name=roleName]").val("");
        });

        //6.给页面上的“铅笔”绑定单击响应函数，目的是打开模态框
        //传统的事件绑定方式只能在第一个页面有效，翻页后失效
        // $(".pencilBtn").click(function (){
        // });
        //使用jQuery对象的on()函数可以解决上面问题
        //①首先找到所有“动态生成”的元素所附着的“静态元素”
        //②on()函数的第一个参数是事件类型，第二个参数是找到真正要绑定事件的元素的选择器，第三个参数是事件的响应函数
        //③
        $("#rolePageBody").on("click",".pencilBtn",function (){
            //打开模态框
            $("#editModal").modal("show");
            //获取表格中当前行中的角色名称
            var roleName = $(this).parent().prev().text();
            //获取当前角色的Id
            //依据：var pencilBtn = "<button id='"+roleId+"'这段代码中我们把roleId设置到id属性了
             window.roleId = this.id;

            //使用roleName的值设置模态框中的文本框
            $("#editModal [name=roleName]").val(roleName);
        });

        //7.给更新模态框中的更新按钮绑定单击响应函数
        $("#updateRoleBtn").click(function (){
            //①从文本框中获取新的角色名称
            var roleName = $("#editModal [name=roleName]").val();
            //②发送Ajax请求执行跟新
            $.ajax({
                url:"role/update.json",
                type:"post",
                data:{
                    id: window.roleId,
                    name: roleName
                },
                dataType: "json",
                success: function (resp){
                    let result = resp.result;
                    if (result === "SUCCESS") {
                        layer.msg("已更新");

                        // 重新加载分页
                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！"+resp.message);
                    }
                },
                error: function (resp) {
                    layer.msg(resp.status+""+resp.statusText);
                }
            });
            //关闭模态框
            $("#editModal").modal("hide");
        });

        //8.点击确认模态框中的确认删除按钮执行删除
        $("#removeRoleBtn").click(function(){
            //从全局变量范围获取roleIdArray,转换为JSON字符串
            var requestBody = JSON.stringify(window.roleIdArray);
            $.ajax({
                url: "role/remove/by/role/id/array.json",
                type: "post",
                data: requestBody,
                contentType: "application/json;charset=UTF-8",
                dataType: "json",
                success: function (resp){
                    let result = resp.result;
                    if (result === "SUCCESS") {
                        layer.msg("已更新");

                        // 重新加载分页
                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！"+resp.message);
                    }
                },
                error: function (resp) {
                    layer.msg(resp.status+""+resp.statusText);
                }
            });
            //关闭模态框
            $("#confirmModal").modal("hide");
        });

        //9.给单挑删除按钮绑定单击响应函数
        $("#rolePageBody").on("click",".removeBtn",function (){

            //从当前按钮出发获取角色名称
            var roleName = $(this).parent().prev().text();
            //创建role对象存入数组
            var roleIdArray = [{
                roleId:this.id,
                roleName:roleName
            }];

            //调用专门的函数打开模态框
            showConfirmModal(roleIdArray);
        });

        //10.给总的checkbox绑定单击响应函数
        $("#summaryBox").click(function (){
           //①获取当前多选框自身的状态
           var currentStatus = this.checked;
           //②用当前多选框的状态设置其他多选框
            $(".itemBox").prop("checked",currentStatus);
        });

        //11.全选、全不选的的的反向操作
        $("#rolePageBody").on("click",".itemBox",function (){
            //获取当前已经选中的.itemBox数量
            var checkedBoxCount = $(".itemBox:checked").length;

            //获取全部.itemBox的数量
            var totalBoxCount = $(".itemBox").length;
            $("#summaryBox").prop("checked",checkedBoxCount == totalBoxCount);
        });

        //12.给批量删除的按钮绑定单击响应函数
        $("#batchRemoveBtn").click(function (){
            //创建一个数组对象用来存放后面获取到的角色对象
            var roleArray = [];
            //遍历当前选中的多选框
            $(".itemBox:checked").each(function (){
                //使用this引用当前遍历得到的多选框
                var roleId = this.id;
                //通过DOM操作获取角色名称
                var roleName = $(this).parent().next().text();

                roleArray.push({
                    roleId:roleId,
                    roleName:roleName
                });
            });
            //检查roleArray长度是否为0
            if(roleArray.length == 0){
                layer.alert("请至少选择一个！")
                return;
            }
            //调用专门的函数去打开模态框
            showConfirmModal(roleArray);
        });

        //13.给分配权限按钮绑定单击响应函数
        $("#rolePageBody").on("click",".checkBtn",function (){
            //把当前角色id存入全局变量
            window.roleId = this.id;
            //打开模态框
            $("#assignModal").modal("show");
            //  在模态框中装载Auth的树形结构数据
            fillAuthTree();
        });
        // 14.给分配权限模态框中的“分配” 按钮绑定单击响应函数
        $("#assignBtn").click(function () {
            // ①收集树形结构的各个节点中被勾选的节点
            // [1]声明一个专门的数组存放 id
            var authIdArray = [];
            // [2]获取 zTreeObj 对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            // [3]获取全部被勾选的节点
            var checkedNodes = zTreeObj.getCheckedNodes();
            // [4]遍历 checkedNodes
            for (var i = 0; i < checkedNodes.length; i++) {
                var checkedNode = checkedNodes[i];
                var authId = checkedNode.id;
                authIdArray.push(authId);
            }
            // ②发送请求执行分配
            var requestBody = {
                "authIdArray": authIdArray,
                // 为了服务器端 handler 方法能够统一使用 List<Integer>方式接收数据， roleId 也存入数组
                "roleId": [window.roleId]
            };
            requestBody = JSON.stringify(requestBody);
            $.ajax({
                url: "assign/do/role/assign/auth.json",
                type: "post",
                data: requestBody,
                contentType: "application/json;charset=UTF-8",
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功！ ");
                    }
                    if
                    (result === "FAILED") {
                        layer.msg("操作失败！ " + response.message);
                    }
                },
                error: function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            $("#assignModal").modal("hide");
        });
    });
</script>

<body>

<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" id="showAddModalBtn" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/modal-role-add.jsp"%>
<%@include file="/WEB-INF/modal-role-edit.jsp"%>
<%@include file="/WEB-INF/modal-role-confirm.jsp"%>
<%@include file="/WEB-INF/modal-role-assign-auth.jsp"%>
</body>
</html>
