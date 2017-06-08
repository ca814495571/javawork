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
			$('.container').delegate('#channelTicketID', 'blur', validateChannelTicketID);
						
		});

		var validateChannelTicketID = function(){
			var channelTicketID = $('#channelTicketID').val();
			if(!channelTicketID){
			   $('.channelTicketID').addClass('has-error').removeClass('has-success');
			   $('.channelTicketID-remove').show();
			   $('.channelTicketID-ok').hide();
			}
			else{
			   $('.channelTicketID').addClass('has-success').removeClass('has-error');
			   $('.channelTicketID-ok').show();
			   $('.channelTicketID-remove').hide();
			}
		}			

	</script>
  </head>
  <body>
   <div class="container">
   <form action="<%=path %>/test/request307" method="post" class="form-horizontal" role="form"  style="margin-top:50px">
      <div class="form-group channelTicketID has-feedback">
		<label for="channelTicketID" class="col-sm-2 control-label">ChannelTicketID:</label>
		<div class="col-sm-4">
			<input type="text" name="channelTicketID" id="channelTicketID" class="form-control"  placeholder="channelTicketID">
			 <span class="glyphicon glyphicon-ok form-control-feedback channelTicketID-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback channelTicketID-remove"></span>
		</div>
      </div>  
   	<input type="submit" value="submit" class="btn btn-primary btn-lg" style="margin-left:160px;margin-top:20px;"> 	
   	
   </form>
   </div>
  </body>
</html>
