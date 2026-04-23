<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Titre etape3a</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"></head>
<body><div class="app-layout"><jsp:include page="/WEB-INF/views/layout/navbar.jsp" /><main class="main-content">
<div class="stepper"><c:forEach begin="1" end="7" var="i"><div class="stepper-item ${i == etapeActuelle ? 'active' : (i < etapeActuelle ? 'done' : 'future')}"><div><div class="stepper-node">${i}</div></div><c:if test="${i < 7}"><div class="stepper-track"></div></c:if></div></c:forEach></div>
<h1 class="page-title">Pieces communes</h1>
<section class="card"><form method="post" action="${pageContext.request.contextPath}/demande/autre/etape3a">
<c:choose><c:when test="${typeDemande eq 'duplicata'}"><p>Type de visa : ${typeVisaLibelle}</p><input type="hidden" name="typeVisaId" value="${typeVisaId}"/></c:when><c:otherwise><div class="form-row"><label>Type visa</label><select name="typeVisaId"><option value="">--</option><c:forEach var="tv" items="${typesVisa}"><option value="${tv.id}" ${tv.id == typeVisaId ? 'selected' : ''}>${tv.libelle}</option></c:forEach></select></div></c:otherwise></c:choose>
<div class="form-row"><label>Date demande</label><input type="date" name="dateDemande" value="${dateDemande}"></div>
<div class="checkbox-grid"><c:forEach var="piece" items="${piecesCommunes}"><label class="checkbox-item"><input type="checkbox" name="pieceCommune_${piece.id}" ${piecesChecked[piece.id] ? 'checked' : ''}/>${piece.libelle}</label></c:forEach></div>
<c:if test="${not empty erreurs}"><div class="erreur">Veuillez corriger les erreurs.</div></c:if>
<div class="actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/autre/etape2"><- Precedent</a><button class="btn btn-primary" type="submit">Suivant -></button></div>
</form></section></main></div></body></html>
