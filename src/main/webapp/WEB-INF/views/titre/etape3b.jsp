<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Titre etape3b</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"></head>
<body><div class="app-layout"><jsp:include page="/WEB-INF/views/layout/navbar.jsp" /><main class="main-content">
<div class="stepper"><c:forEach begin="1" end="7" var="i"><div class="stepper-item ${i == etapeActuelle ? 'active' : (i < etapeActuelle ? 'done' : 'future')}"><div><div class="stepper-node">${i}</div></div><c:if test="${i < 7}"><div class="stepper-track"></div></c:if></div></c:forEach></div>
<h1 class="page-title">Pieces specifiques</h1><p class="page-subtitle">Type visa: ${typeVisaLibelle}</p>
<section class="card"><form method="post" action="${pageContext.request.contextPath}/demande/autre/etape3b">
<div class="checkbox-grid"><c:forEach var="piece" items="${piecesSpecifiques}"><label class="checkbox-item"><input type="checkbox" name="pieceSpecifique_${piece.id}" ${piecesChecked[piece.id] ? 'checked' : ''}/>${piece.libelle}</label></c:forEach></div>
<c:if test="${not empty erreurs}"><div class="erreur">${erreurs.piecesSpecifiques}</div></c:if>
<div class="actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/autre/etape3a"><- Precedent</a><button class="btn btn-primary" type="submit">Suivant -></button></div>
</form></section></main></div></body></html>
