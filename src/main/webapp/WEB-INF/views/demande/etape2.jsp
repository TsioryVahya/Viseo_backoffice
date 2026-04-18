<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande - Etape 2</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<c:set var="etapeActuelle" value="${empty etapeActuelle ? 2 : etapeActuelle}" />
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
        <h1 class="page-title">Nouvelle demande</h1>
        <p class="page-subtitle">Renseignez les informations du passeport.</p>
        <jsp:include page="/WEB-INF/views/common/stepper.jspf" />

        <section class="card">
            <h2 style="margin-top: 0;">Passeport</h2>

            <form method="post" action="${pageContext.request.contextPath}/demande/nouveau/etape2">
                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="numeroPasseport">Numero passeport</label>
                        <input type="text" id="numeroPasseport" name="numeroPasseport" value="${form.numeroPasseport}">
                        <c:if test="${not empty erreurs.numeroPasseport}"><div class="erreur">${erreurs.numeroPasseport}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="paysDelivrance">Pays de delivrance</label>
                        <input type="text" id="paysDelivrance" name="paysDelivrance" value="${form.paysDelivrance}">
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="dateDelivrance">Date delivrance</label>
                        <input type="date" id="dateDelivrance" name="dateDelivrance" value="${form.dateDelivrance}">
                        <c:if test="${not empty erreurs.dateDelivrance}"><div class="erreur">${erreurs.dateDelivrance}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="dateExpiration">Date expiration</label>
                        <input type="date" id="dateExpiration" name="dateExpiration" value="${form.dateExpiration}">
                        <c:if test="${not empty erreurs.dateExpiration}"><div class="erreur">${erreurs.dateExpiration}</div></c:if>
                    </div>
                </div>

                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/nouveau/etape1">&larr; Precedent</a>
                    <button type="submit" class="btn btn-primary">Suivant &rarr;</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
