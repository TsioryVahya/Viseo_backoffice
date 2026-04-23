<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Nouveau passeport</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"></head>
<body><div class="app-layout"><jsp:include page="/WEB-INF/views/layout/navbar.jsp" /><main class="main-content"><h1 class="page-title">Transfert de visa - Nouveau passeport</h1>
<section class="card"><form method="post" action="${pageContext.request.contextPath}/demande/autre/nouveau-passeport">
<div class="form-grid-2"><div class="form-row"><label>Numero passeport</label><input type="text" name="numeroPasseport"></div><div class="form-row"><label>Pays delivrance</label><input type="text" name="paysDelivrance"></div></div>
<div class="form-grid-2"><div class="form-row"><label>Date delivrance</label><input type="date" name="dateDelivrance"></div><div class="form-row"><label>Date expiration</label><input type="date" name="dateExpiration"></div></div>
<c:if test="${not empty erreurs}"><div class="erreur">Veuillez corriger les erreurs.</div></c:if>
<div class="actions"><button class="btn btn-primary" type="submit">Suivant -></button></div>
</form></section></main></div></body></html>
