<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/HelloTag_ImpByTag" prefix="helloP"%>
<%@ taglib uri="/HelloTag_ExtendsByTagSupport" prefix="hello1"%>
<%@ taglib uri="/hello_ExtendsByBodyTag" prefix="helloBBT"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>


<helloBBT:helloByBodyTag counts="5"> 
现在的时间是： <%=new java.util.Date()%><BR>
</helloBBT:helloByBodyTag>

<script type="text/javascript" src="/js/jquery-1.11.2.js"></script>
<script type="text/javascript" src="/js/grid.js"></script>
<script type="text/javascript">
$(function(){
	alert(12);
});
</script>
</body>
</html>