<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande - Etape 3B</title>
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
        <h1 class="page-title">Nouvelle demande</h1>
        <p class="page-subtitle">Completer les pieces specifiques au visa selectionne.</p>
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
            <div class="stepper-item active">
                <div>
                    <div class="stepper-node">3</div>
                    <div class="stepper-label">Demande</div>
                </div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item future">
                <div>
                    <div class="stepper-node">4</div>
                    <div class="stepper-label">Resume</div>
                </div>
            </div>
        </div>

        <section class="card">
            <h2 style="margin-top: 0;">Pieces specifiques</h2>

            <div class="form-row">
                <label>Type de visa selectionne</label>
                <input type="text" value="${typeVisa.libelle}" readonly>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/demande/nouveau/etape3b">
                <div class="form-row">
                    <label>Pieces specifiques</label>
                    <div class="checkbox-grid">
                        <c:forEach items="${piecesSpecifiques}" var="piece">
                            <label class="checkbox-item">
                                <input type="checkbox" name="pieceSpecifique_${piece.id}" <c:if test="${piecesSpecifiquesSelection[piece.id]}">checked</c:if>>
                                <span>${piece.libelle} <c:if test="${piece.obligatoire}">(obligatoire)</c:if></span>
                            </label>
                        </c:forEach>
                    </div>
                    <c:if test="${empty piecesSpecifiques}">
                        <p style="color: var(--gris-text); margin-top: 0.6rem;">Aucune piece specifique pour ce type de visa.</p>
                    </c:if>
                </div>

                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/nouveau/etape3a">&larr; Precedent</a>
                    <button type="submit" class="btn btn-primary">Suivant &rarr;</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
