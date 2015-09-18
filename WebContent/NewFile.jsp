<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<form action="<%=request.getContextPath() %>/LoginServlet"  method="post">
姓名1<input name="name" value="12"><p><p><p><p>
密码1<input name="psw" value="WWW">
</form>
<div id="submit">submit</div>
<script type="text/javascript" src="jquery-1.11.2.js"></script>
<script type="text/javascript">
$("#submit").click(function(){
	$("form").submit();
});
</script>
</body>
</html>