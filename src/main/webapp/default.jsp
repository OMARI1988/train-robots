<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@page import="static com.trainrobots.web.services.ServiceContext.get"%>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>

<page:applyDecorator name="/sitemesh/template.jsp" encoding="utf-8">
	<html>
	<body>
	<p>Welcome to Train Robots!</p>
	<p>
		Image service says: [<%=get().imageService().status()%>]
	</p>
	</body>
	</html>
</page:applyDecorator>