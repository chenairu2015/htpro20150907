<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理中心</title>
<%@include file="/htframe/frame/include/css.inc" %>
<style type="text/css">
.body{
	margin:50px;
}
.ht_platFormTop_div{
	color:red;
	background:pink;
	width:100%;
}
</style>
</head>
<body class="body">
	<div class="ht_platFormTop"></div>

<div class="ht_platFormMain"></div>
<%@include file="ht_platFormBottom.jsp" %>
	
<%@include file="/htframe/frame/include/js.inc" %>
<script type="text/javascript">
$(function(){
	$('.ht_platFormTop').load('ht_platFormTop.jsp');
});
</script>
</body>
</html>