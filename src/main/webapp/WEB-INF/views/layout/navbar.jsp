<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="sidebar">
    <div class="sidebar-logo">Viseo Backoffice</div>
    <div class="sidebar-separator"></div>

    <nav class="sidebar-nav">
        <a class="sidebar-link ${menuActif == 'accueil' ? 'active' : ''}" href="${pageContext.request.contextPath}/">Accueil</a>
        <a class="sidebar-link ${menuActif == 'nouvelle-demande' ? 'active' : ''}" href="${pageContext.request.contextPath}/demande/nouveau/etape1">Nouvelle demande</a>
        <a class="sidebar-link ${menuActif == 'scan' ? 'active' : ''}" href="${pageContext.request.contextPath}/scan/liste">Scan des pieces</a>
        <a class="sidebar-link ${menuActif == 'liste-demandes' ? 'active' : ''}" href="${pageContext.request.contextPath}/demande/liste">Liste des demandes</a>
        <a class="sidebar-link ${menuActif == 'demandeurs' ? 'active' : ''}" href="${pageContext.request.contextPath}/demandeurs">Demandeurs</a>
    </nav>

    <div class="sidebar-footer">v1.0 - Sprint 2</div>
</aside>
