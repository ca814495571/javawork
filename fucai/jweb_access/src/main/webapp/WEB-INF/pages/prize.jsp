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
    <title>福彩算奖测试页面</title>
    <!-- jQuery文件 -->
    <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
    <!-- Bootstrap 核心 JavaScript 文件 -->
    <script src="http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
	<script>
		$(function(){
			$('.glyphicon').hide();
			$('#responseResult').hide();
			$('.container').delegate('#lotteryId', 'blur', validateLotteryId)
						.delegate('#issueNo', 'blur', validateIssueNo)
						.delegate('#playType', 'blur', validatePlayType)
						.delegate('#ballContent', 'blur', validateBallContent);



		});

		function ajaxPost(){

			var lotteryId = $.trim($('#lotteryId').val());
			var issueNo = $.trim($('#issueNo').val());
			var playType = $.trim($('#playType').val());
			var ballContent = $.trim($('#ballContent').val());
			var server = $('#server').val();
			var url = "/jweb_access/lottery/prize";

			var param = [];
			param.push('lotteryId='+lotteryId);
			param.push('issueNo='+issueNo);
			param.push('playType='+playType);
			param.push('ballContent='+ballContent);
			param.push('server='+server);

			$.ajax({
			  type: 'post',
			  url: url,
			  data: param.join('&'),
			  dataType: 'json',
			  success: function(data){
				    $('#responseResult').show();
					if(data){
						$('#ballCount').text(data.ballCount);
						$('#detail').text(data.detail);
						$('#detailCount').text(data.checkDetailCount);
						$('#amount').text(data.amount);
						$('#result').text(data.winningBallContent);
						$('#prize').text(data.prize);
					}
			  }
			});
		}
		
		var validateLotteryId = function(){
			var lotteryId = $('#lotteryId').val();
			if(!lotteryId){
			   $('.lotteryId').addClass('has-error').removeClass('has-success');
			   $('.lotteryId-remove').show();
			   $('.lotteryId-ok').hide();
			}
			else{
			   $('.lotteryId').addClass('has-success').removeClass('has-error');
			   $('.lotteryId-ok').show();
			   $('.lotteryId-remove').hide();
			}
		}
		
		var validateIssueNo = function(){
			var issueNo = $('#issueNo').val();
			if(!issueNo){
			   $('.issueNo').addClass('has-error').removeClass('has-success');
			   $('.issueNo-remove').show();
			   $('.issueNo-ok').hide();
			}
			else{
			   $('.issueNo').addClass('has-success').removeClass('has-error');
			   $('.issueNo-ok').show();
			   $('.issueNo-remove').hide();
			}
		}
		

		var validatePlayType = function(){
			var playType = $('#playType').val();
			if(!playType){
			   $('.playType').addClass('has-error').removeClass('has-success');
			   $('.playType-remove').show();
			   $('.playType-ok').hide();
			}
			else{
			   $('.playType').addClass('has-success').removeClass('has-error');
			   $('.playType-ok').show();
			   $('.playType-remove').hide();
			}
		}
		
		var validateBallContent = function(){
			var ballContent = $('#ballContent').val();
			if(!ballContent){
			   $('.ballContent').addClass('has-error').removeClass('has-success');
			   $('.ballContent-remove').show();
			   $('.ballContent-ok').hide();
			}
			else{
			   $('.ballContent').addClass('has-success').removeClass('has-error');
			   $('.ballContent-ok').show();
			   $('.ballContent-remove').hide();
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
	   <div class="form-group lotteryId has-feedback">
		<label for="lotteryId" class="col-sm-2 control-label">lotteryId:</label>
		<div class="col-sm-6">
			 <select class="form-control" name="lotteryId" id="lotteryId">
			  <option value="DLT" selected="selected">DLT</option>
			  <option value="QXC">QXC</option>
			  <option value="PLS">PL3</option>
			  <option value="PLW">PL5</option>
			  <option value="ZJSYXW">11X5</option>
			  <option value="ZCSF">ZCSF</option>
			  <option value="ZCR9">ZCR9</option>
			  <option value="ZC4JQS">ZC4JQS</option>
			  <option value="ZC6BQC">ZC6BQC</option>
			  <option value="SSQ">SSQ</option>
			  <option value="QLC">QLC</option>
			  <option value="D3">FC3D</option>
			  <option value="CQKL10">XYNC</option>
			  <option value="CQSSC">SSC</option>
			</select>
			 <!--<input type="text" name="lotteryId" id="lotteryId" class="form-control"  placeholder="lotteryId">-->
			 <span class="glyphicon glyphicon-ok form-control-feedback lotteryId-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback lotteryId-remove"></span>
		</div>
      </div>
	  <div class="form-group issueNo has-feedback">
		<label for="issueNo" class="col-sm-2 control-label">issueNo:</label>
		<div class="col-sm-6">
			<input type="text" name="issueNo" id="issueNo" class="form-control"  placeholder="issueNo">
			 <span class="glyphicon glyphicon-ok form-control-feedback issueNo-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback issueNo-remove"></span>
		</div>
      </div>
      <div class="form-group playType has-feedback">
		<label for="playType" class="col-sm-2 control-label">playType:</label>
		<div class="col-sm-6">
			<input type="text" name="playType" id="playType" class="form-control"  placeholder="playType">
			 <span class="glyphicon glyphicon-ok form-control-feedback playType-ok"></span>
			 <span class="glyphicon glyphicon-remove form-control-feedback playType-remove"></span>
		</div>
      </div>
	  <div class="form-group ballContent has-feedback">
		<label for="ballContent" class="col-sm-2 control-label">ballContent:</label>
		<div class="col-sm-6">
			<input type="text"  name="ballContent" id="ballContent"  placeholder="ballContent" class="form-control">
			<span class="glyphicon glyphicon-ok form-control-feedback ballContent-ok"></span>
			<span class="glyphicon glyphicon-remove form-control-feedback ballContent-remove"></span>
		</div>
      </div>
   	<input type="submit" value="submit" class="btn btn-primary btn-lg" style="margin-left:190px;margin-top:20px;">   	
   </form>
   </div>
   <div id="responseResult" style="margin-left:280px;">
   		<h3 class="text-left">投注注数:<span id="ballCount"></span></h3>
   		<h3 class="text-left">拆注内容:<span id="detail"></span></h3>
   		<h3 class="text-left">拆注注数与投注注数:<span id="detailCount"></span></h3>
   		<h3 class="text-left">中奖金额:<span id="amount"></span></h3>
   		<h3 class="text-left">开奖结果:<span id="result"></span></h3>
   		<h3 class="text-left">开奖奖金:<span id="prize"></span></h3>
   </div>
  </body>
</html>
