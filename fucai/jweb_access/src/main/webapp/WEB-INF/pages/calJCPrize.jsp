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
    <title>竞彩算奖测试页面</title>
    <!-- jQuery文件 -->
    <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap 核心 JavaScript 文件 -->
    <script src="http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
	<script>
		$(function(){
			$('.glyphicon').hide();
			$('#responseResult').hide();
			$('.container').delegate('#transferId', 'blur', validateTransferId);
		});

		function ajaxPost(){
			var transferId = $.trim($('#transferId').val());
			var server = $('#server').val();
			var url = "/jweb_access/lottery/jcCalPrize";

			var param = [];
			param.push('transferId='+transferId);
			param.push('server='+server);

			$.ajax({
			  type: 'post',
			  url: url,
			  data: param.join('&'),
			  dataType: 'text',
			  success: function(data){
				  $('#responseResult').show();
				  if(data){
					  $('#result').text(data);
				  }
			  }
			});
		}
		
		var validateTransferId = function(){
			var transferId = $('#transferId').val();
			if(!transferId){
			   $('.transferId').addClass('has-error').removeClass('has-success');
			   $('.transferId-remove').show();
			   $('.transferId-ok').hide();
			}
			else{
			   $('.transferId').addClass('has-success').removeClass('has-error');
			   $('.transferId-ok').show();
			   $('.transferId-remove').hide();
			}
		}
	</script>
	<style>
		#responseResult span{
			padding-left: 20px;
			color: #f00;
		}
	</style>
  </head>
  <body>
   <div class="container">
   <form action="javascript:return false;" onsubmit="ajaxPost();return false;" method="post" class="form-horizontal" role="form"  style="margin-top:50px">
   	  <input type="hidden" name="server" value="<%=serverName %>" id="server"/>
   	  <input type="hidden" name="path" value="<%=path %>" id="path"/>
	  <div class="form-group transferId has-feedback">
		<label for="transferId" class="col-sm-2 control-label">transferId:</label>
		<div class="col-sm-6">
			<input type="text" name="transferId" id="transferId" class="form-control"  placeholder="transferId">
			 <span class="glyphicon glyphicon-ok form-control-feedback transferId-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback transferId-remove"></span>
		</div>
      </div>
   	<input type="submit" value="submit" class="btn btn-primary btn-lg" style="margin-left:190px;margin-top:20px;">   	
   </form>
   </div>
  </body>
</html>
