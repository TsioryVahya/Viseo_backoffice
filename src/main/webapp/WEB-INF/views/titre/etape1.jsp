<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>Titre etape1</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"></head>
<body><div class="app-layout"><jsp:include page="/WEB-INF/views/layout/navbar.jsp" /><main class="main-content">
<div class="stepper"><c:forEach begin="1" end="7" var="i"><div class="stepper-item ${i == etapeActuelle ? 'active' : (i < etapeActuelle ? 'done' : 'future')}"><div><div class="stepper-node">${i}</div></div><c:if test="${i < 7}"><div class="stepper-track"></div></c:if></div></c:forEach></div>
<h1 class="page-title">Informations personnelles</h1>
<section class="card"><form method="post" action="${pageContext.request.contextPath}/demande/autre/etape1">
<div class="form-grid-2"><div class="form-row"><label>Nom</label><input type="text" name="nom" value="${demandeur.nom}"><c:if test="${not empty erreurs.nom}"><div class="erreur">${erreurs.nom}</div></c:if></div>
<div class="form-row"><label>Prenom</label><input type="text" name="prenom" value="${demandeur.prenom}"><c:if test="${not empty erreurs.prenom}"><div class="erreur">${erreurs.prenom}</div></c:if></div></div>
<div class="form-grid-2"><div class="form-row"><label>Date naissance</label><input type="date" name="dateNaissance" value="${demandeur.dateNaissance}"></div>
<div class="form-row"><label>Lieu naissance</label><input type="text" name="lieuNaissance" value="${demandeur.lieuNaissance}"></div></div>
<div class="form-grid-2"><div class="form-row"><label>Telephone</label><input type="text" name="telephone" value="${demandeur.telephone}"></div>
<div class="form-row"><label>Email</label><input type="email" name="email" value="${demandeur.email}"></div></div>
<div class="form-row"><label>Adresse</label><textarea name="adresse">${demandeur.adresse}</textarea></div>
<div class="form-grid-2"><div class="form-row"><label>Nationalite</label><select name="idNationalite"><option value="">--</option><c:forEach var="n" items="${nationalites}"><option value="${n.id}">${n.libelle}</option></c:forEach></select></div>
<div class="form-row"><label>Situation familiale</label><select name="idSituationFamiliale"><option value="">--</option><c:forEach var="s" items="${situationsFamiliales}"><option value="${s.id}">${s.libelle}</option></c:forEach></select></div></div>
<div class="actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/autre/recherche?type=${typeDemande}"><- Retour</a><button class="btn btn-primary" type="submit">Suivant -></button></div>
</form></section></main></div></body></html>
