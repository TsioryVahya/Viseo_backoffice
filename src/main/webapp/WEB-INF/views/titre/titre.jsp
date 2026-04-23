<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Creation titre</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"></head>
<body><div class="app-layout"><jsp:include page="/WEB-INF/views/layout/navbar.jsp" /><main class="main-content"><h1 class="page-title">${typeDemande eq 'duplicata' ? 'Creer la carte de resident' : 'Creer le visa'}</h1>
<c:if test="${not empty erreur}"><div class="alert-erreur">${erreur}</div></c:if>
<section class="card"><form method="post" action="${pageContext.request.contextPath}/demande/autre/titre">
<div class="form-row"><label>Reference</label><input type="text" name="reference" value="${reference}"><c:if test="${not empty erreurs.reference}"><div class="erreur">${erreurs.reference}</div></c:if></div>
<div class="form-grid-2"><div class="form-row"><label>Date debut</label><input type="date" name="dateDebut" value="${dateDebut}"><c:if test="${not empty erreurs.dateDebut}"><div class="erreur">${erreurs.dateDebut}</div></c:if></div><div class="form-row"><label>Date fin</label><input type="date" name="dateFin" value="${dateFin}"><c:if test="${not empty erreurs.dateFin}"><div class="erreur">${erreurs.dateFin}</div></c:if></div></div>
<div class="actions"><button class="btn btn-primary" type="submit">Confirmer et delivrer le titre</button></div>
</form></section></main></div></body></html>
