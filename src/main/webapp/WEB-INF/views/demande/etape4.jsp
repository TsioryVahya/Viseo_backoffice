<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande - Etape 4</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<c:set var="etapeActuelle" value="${empty etapeActuelle ? 4 : etapeActuelle}" />
<c:set var="activeMenu" value="nouvelle-demande" />

<div class="app-layout">
    <aside class="sidebar">
        <div class="sidebar-logo">Viseo Backoffice</div>
        <div class="sidebar-separator"></div>
        <nav class="sidebar-nav">
            <a class="sidebar-link ${activeMenu == 'nouvelle-demande' ? 'active' : ''}" href="${pageContext.request.contextPath}/demande/nouveau/etape1">Nouvelle demande</a>
            <a class="sidebar-link" href="#">Liste des demandes</a>
            <a class="sidebar-link" href="#">Demandeurs</a>
        </nav>
        <div class="sidebar-footer">v1.0 - Sprint 1</div>
    </aside>

    <main class="main-content">
        <h1 class="page-title">Resume et confirmation</h1>
        <p class="page-subtitle">Verifiez le dossier avant soumission definitive.</p>
        <jsp:include page="/WEB-INF/views/common/stepper.jspf" />
        <c:if test="${not empty erreurs.global}"><div class="erreur" style="margin-bottom: 1rem;">${erreurs.global}</div></c:if>

        <section class="card">
            <h3 style="margin-top: 0;">Informations personnelles</h3>
            <div class="resume-grid">
                <div class="resume-label">Nom</div><div class="resume-value">${demandeur.nom}</div>
                <div class="resume-label">Prenom</div><div class="resume-value">${demandeur.prenom}</div>
                <div class="resume-label">Date de naissance</div><div class="resume-value"><fmt:formatDate value="${dateNaissanceDate}" pattern="dd/MM/yyyy" /></div>
                <div class="resume-label">Lieu de naissance</div><div class="resume-value">${demandeur.lieuNaissance}</div>
                <div class="resume-label">Telephone</div><div class="resume-value">${demandeur.telephone}</div>
                <div class="resume-label">Email</div><div class="resume-value">${demandeur.email}</div>
                <div class="resume-label">Adresse</div><div class="resume-value">${demandeur.adresse}</div>
                <div class="resume-label">Nationalite</div><div class="resume-value">${demandeur.nationalite.libelle}</div>
                <div class="resume-label">Situation familiale</div><div class="resume-value">${demandeur.situationFamiliale.libelle}</div>
            </div>
        </section>

        <section class="card">
            <h3 style="margin-top: 0;">Passeport</h3>
            <div class="resume-grid">
                <div class="resume-label">Numero</div><div class="resume-value">${passeport.numeroPasseport}</div>
                <div class="resume-label">Date delivrance</div><div class="resume-value"><fmt:formatDate value="${dateDelivranceDate}" pattern="dd/MM/yyyy" /></div>
                <div class="resume-label">Date expiration</div><div class="resume-value"><fmt:formatDate value="${dateExpirationDate}" pattern="dd/MM/yyyy" /></div>
                <div class="resume-label">Pays delivrance</div><div class="resume-value">${passeport.paysDelivrance}</div>
            </div>
        </section>

        <section class="card">
            <h3 style="margin-top: 0;">Demande</h3>
            <div class="resume-grid">
                <div class="resume-label">Type de visa</div><div class="resume-value">${typeVisa.libelle}</div>
            </div>
        </section>

        <section class="card">
            <h3 style="margin-top: 0;">Pieces communes</h3>
            <ul style="margin: 0; padding-left: 1.1rem;">
                <c:forEach items="${typesPiecesCommunes}" var="piece">
                    <li style="margin-bottom: 0.4rem;">
                        <c:choose>
                            <c:when test="${piecesCommunesSelection[piece.id]}"><span class="status-ok">✓</span></c:when>
                            <c:otherwise><span class="status-ko">✗</span></c:otherwise>
                        </c:choose>
                        ${piece.libelle}
                    </li>
                </c:forEach>
            </ul>
        </section>

        <section class="card">
            <h3 style="margin-top: 0;">Pieces specifiques</h3>
            <ul style="margin: 0; padding-left: 1.1rem;">
                <c:forEach items="${typesPiecesSpecifiques}" var="piece">
                    <li style="margin-bottom: 0.4rem;">
                        <c:choose>
                            <c:when test="${piecesSpecifiquesSelection[piece.id]}"><span class="status-ok">✓</span></c:when>
                            <c:otherwise><span class="status-ko">✗</span></c:otherwise>
                        </c:choose>
                        ${piece.libelle}
                    </li>
                </c:forEach>
                <c:if test="${empty typesPiecesSpecifiques}"><li>Aucune piece specifique.</li></c:if>
            </ul>
        </section>

        <form method="post" action="${pageContext.request.contextPath}/demande/nouveau/etape4">
            <div class="actions">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/nouveau/etape1">&larr; Modifier</a>
                <button type="submit" class="btn btn-primary">Confirmer et soumettre</button>
            </div>
        </form>
    </main>
</div>
</body>
</html>
