<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Titre etape6</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"></head>
<body><div class="app-layout"><jsp:include page="/WEB-INF/views/layout/navbar.jsp" /><main class="main-content">
<div class="stepper"><c:forEach begin="1" end="7" var="i"><div class="stepper-item ${i == etapeActuelle ? 'active' : (i < etapeActuelle ? 'done' : 'future')}"><div><div class="stepper-node">${i}</div></div><c:if test="${i < 7}"><div class="stepper-track"></div></c:if></div></c:forEach></div>
<h1 class="page-title">Resume final</h1>
<c:if test="${not empty erreur}"><div class="alert-erreur">${erreur}</div></c:if>
<section class="card"><h2>Informations personnelles</h2><div class="resume-grid"><div class="resume-label">Nom</div><div class="resume-value">${demande.demandeur.nom} ${demande.demandeur.prenom}</div><div class="resume-label">Type demande</div><div class="resume-value">${typeDemande}</div><div class="resume-label">Type visa</div><div class="resume-value">${demande.typeVisa.libelle}</div></div></section>
<section class="card"><h2>Pieces communes</h2><c:forEach var="piece" items="${piecesCommunes}"><div><c:choose><c:when test="${not empty uploadsCommunes[piece.id]}">OK</c:when><c:otherwise>KO</c:otherwise></c:choose> - ${piece.typePieceCommune.libelle}<c:if test="${not empty uploadsCommunes[piece.id]}"> (${uploadsCommunes[piece.id].nomFichierOriginal})</c:if></div></c:forEach></section>
<section class="card"><h2>Pieces specifiques</h2><c:forEach var="piece" items="${piecesSpecifiques}"><div><c:choose><c:when test="${not empty uploadsSpecifiques[piece.id]}">OK</c:when><c:otherwise>KO</c:otherwise></c:choose> - ${piece.typePiece.libelle}<c:if test="${not empty uploadsSpecifiques[piece.id]}"> (${uploadsSpecifiques[piece.id].nomFichierOriginal})</c:if></div></c:forEach></section>
<section class="card"><h2>Titre a creer</h2><div class="resume-grid"><div class="resume-label">Reference</div><div class="resume-value">${reference}</div><div class="resume-label">Date debut</div><div class="resume-value">${dateDebut}</div><div class="resume-label">Date fin</div><div class="resume-value">${dateFin}</div></div>
<form method="post" action="${pageContext.request.contextPath}/demande/autre/etape6"><div class="actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/autre/etape5"><- Modifier</a><button class="btn btn-primary" type="submit">Confirmer et soumettre</button></div></form>
</section></main></div></body></html>
