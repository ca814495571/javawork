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
    <title>福彩测试页面</title>
    <!-- jQuery文件 -->
    <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap 核心 JavaScript 文件 -->
    <script src="http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
	<script>
		$(function(){
			$('.glyphicon').hide();
			$('.container').delegate('#transcode', 'blur', validateTranscode)
						.delegate('#msg', 'blur', validateMsg)
						.delegate('#partnerid', 'blur', validatePartnerid);
		});
		
		var validateTranscode = function(){
			var transcode = $('#transcode').val();
			if(!transcode){
			   $('.transcode').addClass('has-error').removeClass('has-success');
			   $('.transcode-remove').show();
			   $('.transcode-ok').hide();
			}
			else{
			   $('.transcode').addClass('has-success').removeClass('has-error');
			   $('.transcode-ok').show();
			   $('.transcode-remove').hide();
			}
		}
		
		var validateMsg = function(){
			var msg = $('#msg').val();
			if(!msg){
			   $('.msg').addClass('has-error').removeClass('has-success');
			   $('.msg-remove').show();
			   $('.msg-ok').hide();
			}
			else{
			   $('.msg').addClass('has-success').removeClass('has-error');
			   $('.msg-ok').show();
			   $('.msg-remove').hide();
			}
		}
		
		var validatePartnerid = function(){
			var partnerid = $('#partnerid').val();
			if(!partnerid){
			   $('.partnerid').addClass('has-error').removeClass('has-success');
			   $('.partnerid-remove').show();
			   $('.partnerid-ok').hide();
			}
			else{
			   $('.partnerid').addClass('has-success').removeClass('has-error');
			   $('.partnerid-ok').show();
			   $('.partnerid-remove').hide();
			}
		}

	</script>
  </head>
  <body>
   <div class="container">
   <form action="<%=path %>/test/saveP" method="post" class="form-horizontal" role="form"  style="margin-top:50px">
   	  <input type="hidden" name="server" value="<%=serverName %>"/>
	   <div class="form-group transcode has-feedback">
		<label for="transcode" class="col-sm-2 control-label">transcode:</label>
		<div class="col-sm-4">
			<input type="text" name="transcode" id="transcode" class="form-control"  placeholder="transcode">
			 <span class="glyphicon glyphicon-ok form-control-feedback transcode-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback transcode-remove"></span>
		</div>
      </div>
	  <div class="form-group msg has-feedback">
		<label for="msg" class="col-sm-2 control-label">msg:</label>
		<div class="col-sm-10">
			<textarea name="msg" id="msg" cols="26" rows="14" class="form-control"></textarea>
			 <span class="glyphicon glyphicon-ok form-control-feedback msg-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback msg-remove"></span>
		</div>
      </div>
	  <div class="form-group partnerid has-feedback">
		<label for="partnerid" class="col-sm-2 control-label">partnerid:</label>
		<div class="col-sm-10">
			<input type="text"  name="partnerid" id="partnerid"  placeholder="partnerid" class="form-control">
			<span class="glyphicon glyphicon-ok form-control-feedback partnerid-ok"></span>
			<span class="glyphicon glyphicon-remove form-control-feedback partnerid-remove"></span>
		</div>
      </div>
   	<input type="submit" value="submit" class="btn btn-primary btn-lg" style="margin-left:160px;margin-top:20px;"> 	
   	
   </form>
   </div>
  </body>
</html>
