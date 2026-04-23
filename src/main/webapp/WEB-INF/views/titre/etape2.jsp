<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Titre etape2</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"></head>
<body><div class="app-layout"><jsp:include page="/WEB-INF/views/layout/navbar.jsp" /><main class="main-content">
<div class="stepper"><c:forEach begin="1" end="7" var="i"><div class="stepper-item ${i == etapeActuelle ? 'active' : (i < etapeActuelle ? 'done' : 'future')}"><div><div class="stepper-node">${i}</div></div><c:if test="${i < 7}"><div class="stepper-track"></div></c:if></div></c:forEach></div>
<h1 class="page-title">Passeport et visa transformable</h1>
<section class="card"><form method="post" action="${pageContext.request.contextPath}/demande/autre/etape2">
<div class="form-grid-2"><div class="form-row"><label>Numero passeport</label><input type="text" name="numeroPasseport"></div><div class="form-row"><label>Pays delivrance</label><input type="text" name="paysDelivrance"></div></div>
<div class="form-grid-2"><div class="form-row"><label>Date delivrance</label><input type="date" name="dateDelivrance"></div><div class="form-row"><label>Date expiration</label><input type="date" name="dateExpiration"></div></div>
<div class="form-grid-2"><div class="form-row"><label>Numero reference visa</label><input type="text" name="numeroReferenceVisa" value="${numeroReferenceVisa}"></div><div class="form-row"><label>Date expiration visa</label><input type="date" name="dateExpirationVisa" value="${dateExpirationVisa}"></div></div>
<c:if test="${not empty erreurs}"><div class="erreur">Veuillez corriger les champs invalides.</div></c:if>
<div class="actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/autre/etape1"><- Precedent</a><button class="btn btn-primary" type="submit">Suivant -></button></div>
</form></section></main></div></body></html>
