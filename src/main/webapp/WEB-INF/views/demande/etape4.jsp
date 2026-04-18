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
        <div class="stepper">
            <div class="stepper-item done">
                <div>
                    <div class="stepper-node">✓</div>
                    <div class="stepper-label">Informations personnelles</div>
                </div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item done">
                <div>
                    <div class="stepper-node">✓</div>
                    <div class="stepper-label">Passeport</div>
                </div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item done">
                <div>
                    <div class="stepper-node">✓</div>
                    <div class="stepper-label">Demande</div>
                </div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item active">
                <div>
                    <div class="stepper-node">4</div>
                    <div class="stepper-label">Resume</div>
                </div>
            </div>
        </div>
        <c:if test="${not empty erreurVisa}">
            <div class="erreur" style="margin-bottom: 1rem; border: 1px solid #c0392b; background: #fdecea; padding: 0.75rem; border-radius: 8px;">
                ${erreurVisa}
                <div style="margin-top: 0.5rem;">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/nouveau/etape2">&larr; Corriger le visa transformable</a>
                </div>
            </div>
        </c:if>
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
                <div class="resume-label">Numero de reference visa</div><div class="resume-value">${numeroReferenceVisa}</div>
                <div class="resume-label">Date d'expiration visa</div><div class="resume-value">${dateExpirationVisa}</div>
            </div>
        </section>

        <section class="card">
            <h3 style="margin-top: 0;">Demande</h3>
            <div class="resume-grid">
                <div class="resume-label">Type de visa</div><div class="resume-value">${typeVisa.libelle}</div>
                <div class="resume-label">Date de la demande</div><div class="resume-value">${dateDemande}</div>
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
