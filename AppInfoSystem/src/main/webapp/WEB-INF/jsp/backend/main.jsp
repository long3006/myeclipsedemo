<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="common/header.jsp" %>
<div class="page-title">
	<div class="title_left">
		<h3>
			<h3>
			欢迎你：${backUserSession.userName }<strong> | 角色：${backUserSession.userTypeName }</strong>
			</h3>
		</h3>
	</div>
</div>
<div class="clearfix"></div>        
<%@include file="common/footer.jsp" %>  
     