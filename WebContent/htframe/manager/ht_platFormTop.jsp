<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="ht_platFormTop_div">
	<ul class="ht_topUl">
		<li class="id">DDDDDD</li>
		<li>AAAAA</li>
		<li>GGGGG</li>
		<li>NNNNNN</li>
	</ul>
	<div class="clear"></div>
</div>
<script>
$('.id').bind('click',function(){
	$('.ht_platFormMain').load('ht_platFormMain.jsp');
});
</script>