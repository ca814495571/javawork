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
			$('.container').delegate('#lotteryID', 'blur', validateLotteryID)
					.delegate('#matchType', 'blur', validateMatchType);
						
		});
		
		var validateLotteryID = function(){
			var lotteryID = $('#lotteryID').val();
			if(!lotteryID){
			   $('.lotteryID').addClass('has-error').removeClass('has-success');
			   $('.lotteryID-remove').show();
			   $('.lotteryID-ok').hide();
			}
			else{
			   $('.lotteryID').addClass('has-success').removeClass('has-error');
			   $('.lotteryID-ok').show();
			   $('.lotteryID-remove').hide();
			}
		}

		var validateMatchType = function(){
			var matchType = $('#matchType').val();
			if(!matchType){
			   $('.matchType').addClass('has-error').removeClass('has-success');
			   $('.matchType-remove').show();
			   $('.matchType-ok').hide();
			}
			else{
			   $('.matchType').addClass('has-success').removeClass('has-error');
			   $('.matchType-ok').show();
			   $('.matchType-remove').hide();
			}
		}			

	</script>
  </head>
  <body>
   <div class="container">
   <form action="<%=path %>/test/request202" method="post" class="form-horizontal" role="form"  style="margin-top:50px">
   	  <input type="hidden" name="server" value="<%=serverName %>"/>
	   <div class="form-group lotteryID has-feedback">
		<label for="lotteryID" class="col-sm-2 control-label">LotteryID:</label>
		<div class="col-sm-4">
			<input type="text" name="lotteryID" id="lotteryID" class="form-control"  placeholder="lotteryID">
			 <span class="glyphicon glyphicon-ok form-control-feedback lotteryID-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback lotteryID-remove"></span>
		</div>
      </div>
      <div class="form-group matchType has-feedback">
		<label for="matchType" class="col-sm-2 control-label">MatchType:</label>
		<div class="col-sm-4">
			<input type="text" name="matchType" id="matchType" class="form-control"  placeholder="matchType">
			 <span class="glyphicon glyphicon-ok form-control-feedback matchType-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback matchType-remove"></span>
		</div>
      </div>  
   	<input type="submit" value="submit" class="btn btn-primary btn-lg" style="margin-left:160px;margin-top:20px;"> 	
   	
   </form>
   </div>
  </body>
</html>
