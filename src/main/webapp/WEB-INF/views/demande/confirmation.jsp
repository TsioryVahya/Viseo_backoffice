<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande confirmee</title>
    <jsp:include page="/WEB-INF/views/common/styles.jspf" />
</head>
<body>
<div class="container">
    <h2>Demande confirmee</h2>
    <p>Votre dossier a bien ete enregistre.</p>
    <p>Numero de dossier: <strong>${numeroDossier}</strong></p>
    <p><a class="btn" href="${pageContext.request.contextPath}/demande/nouveau/etape1">Faire une nouvelle demande</a></p>
</div>
</body>
</html>
