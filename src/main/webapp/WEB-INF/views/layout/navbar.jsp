<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="sidebar">
    <div class="sidebar-logo">Viseo Backoffice</div>
    <div class="sidebar-separator"></div>

    <nav class="sidebar-nav">
        <a class="sidebar-link ${menuActif == 'accueil' ? 'active' : ''}" href="${pageContext.request.contextPath}/">Accueil</a>
        <div class="nav-groupe">
            <span class="nav-groupe-label ${menuActif == 'nouvelle-demande' || menuActif == 'nouveau-titre' || menuActif == 'duplicata' || menuActif == 'transfert' ? 'active' : ''}">Nouvelle demande ▸</span>
            <div class="sous-menu">
                <a href="${pageContext.request.contextPath}/demande/nouveau/etape1" class="${menuActif eq 'nouveau-titre' ? 'active' : ''}">Nouveau titre</a>
                <a href="${pageContext.request.contextPath}/demande/autre/recherche?type=duplicata" class="${menuActif eq 'duplicata' ? 'active' : ''}">Duplicata</a>
                <a href="${pageContext.request.contextPath}/demande/autre/recherche?type=transfert" class="${menuActif eq 'transfert' ? 'active' : ''}">Transfert de visa</a>
            </div>
        </div>
        <a class="sidebar-link ${menuActif == 'scan' ? 'active' : ''}" href="${pageContext.request.contextPath}/scan/liste">Scan des pieces</a>
        <a class="sidebar-link ${menuActif == 'liste-demandes' ? 'active' : ''}" href="${pageContext.request.contextPath}/demande/liste">Liste des demandes</a>
        <a class="sidebar-link ${menuActif == 'demandeurs' ? 'active' : ''}" href="${pageContext.request.contextPath}/demandeurs">Demandeurs</a>
    </nav>

    <div class="sidebar-footer">v1.0 - Sprint 2</div>
</aside>
