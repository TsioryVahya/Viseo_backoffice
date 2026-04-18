<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Viseo Backoffice</title>
    <jsp:include page="/WEB-INF/views/common/styles.jspf" />
</head>
<body>
<div class="container">
    <h1>${message}</h1>
    <p>Le projet Spring Boot avec JSP et PostgreSQL est pret.</p>
    <p><a class="btn" href="${pageContext.request.contextPath}/demande/nouveau/etape1">Demarrer une demande de nouveau titre</a></p>
</div>
</body>
</html>
