<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Viseo Backoffice</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <aside class="sidebar">
        <div class="sidebar-logo">Viseo Backoffice</div>
        <div class="sidebar-separator"></div>

        <nav class="sidebar-nav">
            <a class="sidebar-link active" href="${pageContext.request.contextPath}/demande/nouveau/etape1">Nouvelle demande</a>
            <a class="sidebar-link" href="${pageContext.request.contextPath}/demande/duplicata-transfert/recherche">Duplicata / Transfert</a>
            <a class="sidebar-link" href="#">Liste des demandes</a>
            <a class="sidebar-link" href="#">Demandeurs</a>
        </nav>

        <div class="sidebar-footer">v2.0 - Sprint 2</div>
    </aside>

    <main class="main-content">
        <h1 class="page-title">Bienvenue sur Viseo Backoffice</h1>
        <p class="page-subtitle">Gestion des demandes de visa et de titres de sejour.</p>

        <section class="card">
            <h2 style="margin-top: 0; color: var(--noir);">Demarrer un nouveau dossier</h2>
            <p style="color: var(--gris-text); margin-bottom: 1.2rem;">
                Creez une nouvelle demande en suivant le parcours en 4 etapes: informations personnelles,
                passeport, pieces justificatives et confirmation.
            </p>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/demande/nouveau/etape1">
                Demarrer une demande de nouveau titre ->
            </a>
        </section>

        <section class="card" style="margin-top: 2rem;">
            <h2 style="margin-top: 0; color: var(--green);">Duplicata / Transfert (Sprint 2)</h2>
            <p style="color: var(--gris-text); margin-bottom: 1.2rem;">
                Creez rapidement une demande de Duplicata ou Transfert. La demande sera automatiquement
                approuvee avec le statut "Titre délivré".
            </p>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/demande/duplicata-transfert/recherche">
                Créer une demande Duplicata/Transfert ->
            </a>
        </section>
    </main>
</div>
</body>
</html>
