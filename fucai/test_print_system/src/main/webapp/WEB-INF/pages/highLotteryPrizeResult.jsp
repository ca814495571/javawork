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
					.delegate('#date', 'blur', validateDate)
					.delegate('#wareIssue', 'blur', validateWareIssue);
						
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

		var validateDate = function(){
			var date = $('#date').val();
			if(!date){
			   $('.date').addClass('has-error').removeClass('has-success');
			   $('.date-remove').show();
			   $('.date-ok').hide();
			}
			else{
			   $('.date').addClass('has-success').removeClass('has-error');
			   $('.date-ok').show();
			   $('.date-remove').hide();
			}
		}	

		var validateWareIssue = function(){
			var wareIssue = $('#wareIssue').val();
			if(!wareIssue){
			   $('.wareIssue').addClass('has-error').removeClass('has-success');
			   $('.wareIssue-remove').show();
			   $('.wareIssue-ok').hide();
			}
			else{
			   $('.wareIssue').addClass('has-success').removeClass('has-error');
			   $('.wareIssue-ok').show();
			   $('.wareIssue-remove').hide();
			}
		}				

	</script>
  </head>
  <body>
   <div class="container">
   <form action="<%=path %>/test/requestgpjzj" method="post" class="form-horizontal" role="form"  style="margin-top:50px">
   	  <input type="hidden" name="server" value="<%=serverName %>"/>
	   <div class="form-group lotteryID has-feedback">
		<label for="lotteryID" class="col-sm-2 control-label">LotteryID:</label>
		<div class="col-sm-4">
			<input type="text" name="lotteryID" id="lotteryID" class="form-control"  placeholder="lotteryID">
			 <span class="glyphicon glyphicon-ok form-control-feedback lotteryID-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback lotteryID-remove"></span>
		</div>
      </div>
      <div class="form-group date has-feedback">
		<label for="date" class="col-sm-2 control-label">Date:</label>
		<div class="col-sm-4">
			<input type="text" name="date" id="date" class="form-control"  placeholder="date">
			 <span class="glyphicon glyphicon-ok form-control-feedback date-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback date-remove"></span>
		</div>
      </div>  
      <div class="form-group wareIssue has-feedback">
		<label for="wareIssue" class="col-sm-2 control-label">WareIssue:</label>
		<div class="col-sm-4">
			<input type="text" name="wareIssue" id="wareIssue" class="form-control"  placeholder="wareIssue">
			 <span class="glyphicon glyphicon-ok form-control-feedback wareIssue-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback wareIssue-remove"></span>
		</div>
      </div>      
   	<input type="submit" value="submit" class="btn btn-primary btn-lg" style="margin-left:160px;margin-top:20px;"> 	
   	
   </form>
   </div>
  </body>
</html>
