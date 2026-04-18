<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande confirmee</title>
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
        <section class="card" style="max-width: 760px; margin: 1rem auto; text-align: center;">
            <div class="confirmation-check">✓</div>
            <h2 style="margin: 0 0 0.5rem; color: var(--green);">Demande confirmee avec succes</h2>
            <p style="color: var(--gris-text); margin-top: 0;">
                Votre dossier est enregistre et transmis pour traitement.
            </p>

            <div style="margin: 1rem 0 1.2rem;">
                <span class="dossier-box">Numero de dossier: ${numeroDossier}</span>
            </div>

            <section class="card" style="text-align: left; padding: 1rem 1.2rem; background: #fcfcfa;">
                <h3 style="margin-top: 0;">Resume</h3>
                <div class="resume-grid">
                    <div class="resume-label">Statut initial</div>
                    <div class="resume-value">Dossier cree</div>
                    <div class="resume-label">Type de demande</div>
                    <div class="resume-value">Nouveau titre</div>
                    <div class="resume-label">Prochaine action</div>
                    <div class="resume-value">Controle des pieces et validation du dossier</div>
                </div>
            </section>

            <div class="actions" style="justify-content: center; margin-top: 1.2rem;">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/">Retour accueil</a>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/demande/nouveau/etape1">Nouvelle demande</a>
            </div>
        </section>
    </main>
</div>
</body>
</html>
