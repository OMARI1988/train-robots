<%
com.trainrobots.web.services.ServiceContext.get().userService().signOut(session);
response.sendRedirect("/");
%>