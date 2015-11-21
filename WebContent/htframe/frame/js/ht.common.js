//获取IP和上下文根路径
function getAbsUrl() {
	var contextPath=window.location.pathname;
	var index=contextPath.substr(1).indexOf("/");
	contextPath=contextPath.substr(0,index+1);
	contextPath=contextPath.indexOf("/")>-1?contextPath:("/"+contextPath);
	var port=window.location.port==""?"":":"+window.location.port
	console.log("http://"+window.location.hostname+port+contextPath+"/");
}