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
			$('.container').delegate('#lotteryID', 'blur', validateLotteryID)			.delegate('#wareID', 'blur', validateWareID)
						.delegate('#wareIssue', 'blur', validateWareIssue)
						.delegate('#batchID', 'blur', validateBatchID)
						.delegate('#betAmount', 'blur', validateBetAmount)
						.delegate('#betContent', 'blur', validateBetContent)
						.delegate('#realName', 'blur', validateRealName)
						.delegate('#iDCard', 'blur', validateIDCard)
						.delegate('#phone', 'blur', validatePhone);
						
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
		
		var validateWareID = function(){
			var wareID = $('#wareID').val();
			if(!wareID){
			   $('.wareID').addClass('has-error').removeClass('has-success');
			   $('.wareID-remove').show();
			   $('.wareID-ok').hide();
			}
			else{
			   $('.wareID').addClass('has-success').removeClass('has-error');
			   $('.wareID-ok').show();
			   $('.wareID-remove').hide();
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
		
		var validateBatchID = function(){
			var batchID = $('#batchID').val();
			if(!batchID){
			   $('.batchID').addClass('has-error').removeClass('has-success');
			   $('.batchID-remove').show();
			   $('.batchID-ok').hide();
			}
			else{
			   $('.batchID').addClass('has-success').removeClass('has-error');
			   $('.batchID-ok').show();
			   $('.batchID-remove').hide();
			}
		}

		var validateBetAmount = function(){
			var betAmount = $('#betAmount').val();
			if(!betAmount){
			   $('.betAmount').addClass('has-error').removeClass('has-success');
			   $('.betAmount-remove').show();
			   $('.betAmount-ok').hide();
			}
			else{
			   $('.betAmount').addClass('has-success').removeClass('has-error');
			   $('.betAmount-ok').show();
			   $('.betAmount-remove').hide();
			}
		}

		var validateBetContent = function(){
			var betContent = $('#betContent').val();
			if(!betContent){
			   $('.betContent').addClass('has-error').removeClass('has-success');
			   $('.betContent-remove').show();
			   $('.betContent-ok').hide();
			}
			else{
			   $('.betContent').addClass('has-success').removeClass('has-error');
			   $('.betContent-ok').show();
			   $('.betContent-remove').hide();
			}
		}

		var validateRealName = function(){
			var realName = $('#realName').val();
			if(!realName){
			   $('.realName').addClass('has-error').removeClass('has-success');
			   $('.realName-remove').show();
			   $('.realName-ok').hide();
			}
			else{
			   $('.realName').addClass('has-success').removeClass('has-error');
			   $('.realName-ok').show();
			   $('.realName-remove').hide();
			}
		}

		var validateIDCard = function(){
			var iDCard = $('#iDCard').val();
			if(!iDCard){
			   $('.iDCard').addClass('has-error').removeClass('has-success');
			   $('.iDCard-remove').show();
			   $('.iDCard-ok').hide();
			}
			else{
			   $('.iDCard').addClass('has-success').removeClass('has-error');
			   $('.iDCard-ok').show();
			   $('.iDCard-remove').hide();
			}
		}

		var validatePhone = function(){
			var phone = $('#phone').val();
			if(!phone){
			   $('.phone').addClass('has-error').removeClass('has-success');
			   $('.phone-remove').show();
			   $('.phone-ok').hide();
			}
			else{
			   $('.phone').addClass('has-success').removeClass('has-error');
			   $('.phone-ok').show();
			   $('.phone-remove').hide();
			}
		}

	</script>
  </head>
  <body>
   <div class="container">
   <form action="<%=path %>/test/request101" method="post" class="form-horizontal" role="form"  style="margin-top:20px">
   	  <input type="hidden" name="server" value="<%=serverName %>"/>
	   <div class="form-group lotteryID has-feedback">
		<label for="lotteryID" class="col-sm-2 control-label">LotteryID:</label>
		<div class="col-sm-10">
			<input type="text" name="lotteryID" id="lotteryID" class="form-control"  placeholder="lotteryID">
			 <span class="glyphicon glyphicon-ok form-control-feedback lotteryID-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback lotteryID-remove"></span>
		</div>
      </div>
      <div class="form-group wareID has-feedback">
		<label for="wareID" class="col-sm-2 control-label">WareID:</label>
		<div class="col-sm-10">
			<input type="text" name="wareID" id="wareID" class="form-control"  placeholder="wareID">
			 <span class="glyphicon glyphicon-ok form-control-feedback wareID-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback wareID-remove"></span>
		</div>
      </div>
      <div class="form-group wareIssue has-feedback">
		<label for="wareIssue" class="col-sm-2 control-label">WareIssue:</label>
		<div class="col-sm-10">
			<input type="text" name="wareIssue" id="wareIssue" class="form-control"  placeholder="wareIssue">
			 <span class="glyphicon glyphicon-ok form-control-feedback wareIssue-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback wareIssue-remove"></span>
		</div>
      </div>      
	  <div class="form-group batchID has-feedback">
		<label for="batchID" class="col-sm-2 control-label">BatchID:</label>
		<div class="col-sm-10">
			<input type="text"  name="batchID" id="batchID"  placeholder="batchID" class="form-control">
			<span class="glyphicon glyphicon-ok form-control-feedback batchID-ok"></span>
			<span class="glyphicon glyphicon-remove form-control-feedback batchID-remove"></span>
		</div>
      </div>
      <div class="form-group addFlag has-feedback">
		<label for="addFlag" class="col-sm-2 control-label">AddFlag:</label>
		<div class="col-sm-10">
			<select class="form-control" name="addFlag" id="addFlag">
			  <option value="0" selected="selected">0</option>
			  <option value="1">1</option>
			</select>
			<span class="glyphicon glyphicon-ok form-control-feedback addFlag-ok"></span>
			<span class="glyphicon glyphicon-remove form-control-feedback addFlag-remove"></span>
		</div>
      </div>
      <div class="form-group betAmount has-feedback">
		<label for="betAmount" class="col-sm-2 control-label">BetAmount:</label>
		<div class="col-sm-10">
			<input type="text"  name="betAmount" id="betAmount"  placeholder="betAmount" class="form-control">
			<span class="glyphicon glyphicon-ok form-control-feedback betAmount-ok"></span>
			<span class="glyphicon glyphicon-remove form-control-feedback betAmount-remove"></span>
		</div>
      </div>    
      <div class="form-group betContent has-feedback">
		<label for="betContent" class="col-sm-2 control-label">BetContent:</label>
		<div class="col-sm-10">
			<textarea name="betContent" id="betContent" cols="26" rows="9" class="form-control"></textarea>
			<span class="glyphicon glyphicon-ok form-control-feedback betContent-ok"></span>
			<span class="glyphicon glyphicon-remove form-control-feedback betContent-remove"></span>
		</div>
      </div>       
      <div class="form-group realName has-feedback">
		<label for="realName" class="col-sm-2 control-label">RealName:</label>
		<div class="col-sm-10">
			<input type="text"  name="realName" id="realName"  placeholder="realName" class="form-control">
			<span class="glyphicon glyphicon-ok form-control-feedback realName-ok"></span>
			<span class="glyphicon glyphicon-remove form-control-feedback realName-remove"></span>
		</div>
      </div>     
      <div class="form-group iDCard has-feedback">
		<label for="iDCard" class="col-sm-2 control-label">IDCard:</label>
		<div class="col-sm-10">
			<input type="text"  name="iDCard" id="iDCard"  placeholder="iDCard" class="form-control">
			<span class="glyphicon glyphicon-ok form-control-feedback iDCard-ok"></span>
			<span class="glyphicon glyphicon-remove form-control-feedback iDCard-remove"></span>
		</div>
      </div>       
     <div class="form-group phone has-feedback">
		<label for="phone" class="col-sm-2 control-label">Phone:</label>
		<div class="col-sm-10">
			<input type="text"  name="phone" id="phone"  placeholder="phone" class="form-control">
			<span class="glyphicon glyphicon-ok form-control-feedback phone-ok"></span>
			<span class="glyphicon glyphicon-remove form-control-feedback phone-remove"></span>
		</div>
      </div>        
   	<input type="submit" value="submit" class="btn btn-primary btn-lg" style="margin-left:160px;margin-top:20px;"> 	
   	
   </form>
   </div>
  </body>
</html>
