<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String serverName = request.getServerName();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="shortcut icon" href="http://www.cwl.gov.cn/favicon.ico" >
    <!--  Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <title>彩票出票系统测试页面</title>
    <!-- jQuery文件 -->
    <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap 核心 JavaScript 文件 -->
    <script src="http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
	<script>
		$(function(){
			$('.glyphicon').hide();
			$('.container').delegate('#matchID', 'blur', validateLotteryID);
						
		});
		
		var validateLotteryID = function(){
			var matchID = $('#matchID').val();
			if(!matchID){
			   $('.matchID').addClass('has-error').removeClass('has-success');
			   $('.matchID-remove').show();
			   $('.matchID-ok').hide();
			}
			else{
			   $('.matchID').addClass('has-success').removeClass('has-error');
			   $('.matchID-ok').show();
			   $('.matchID-remove').hide();
			}
		}	

	</script>
  </head>
  <body>
   <div class="container">
   <form action="<%=path %>/test/requestjcjg" method="post" class="form-horizontal" role="form"  style="margin-top:50px">
   	  <input type="hidden" name="server" value="<%=serverName %>"/>
	   <div class="form-group matchID has-feedback">
		<label for="matchID" class="col-sm-2 control-label">matchID:</label>
		<div class="col-sm-4">
			<input type="text" name="matchID" id="matchID" class="form-control"  placeholder="matchID">
			 <span class="glyphicon glyphicon-ok form-control-feedback matchID-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback matchID-remove"></span>
		</div>
      </div>
   	<input type="submit" value="submit" class="btn btn-primary btn-lg" style="margin-left:160px;margin-top:20px;"> 	
   	
   </form>
   </div>
  </body>
</html>
