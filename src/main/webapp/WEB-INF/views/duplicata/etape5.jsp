<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Duplicata - Etape 5</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <h1 class="page-title">Informations du nouveau titre a delivrer</h1>
        <p class="page-subtitle">Ces informations correspondent a la carte de resident qui sera delivree au demandeur.</p>

        <div class="stepper">
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Recherche</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Infos personnelles</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Passeport et Visa</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Pieces</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Upload</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item active"><div><div class="stepper-node">6</div><div class="stepper-label">Nouveau titre</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">7</div><div class="stepper-label">Ancienne carte</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">8</div><div class="stepper-label">Resume</div></div></div>
        </div>

        <section class="card">
            <form method="post" action="${pageContext.request.contextPath}/duplicata/etape5">
                <div class="form-row">
                    <label for="reference">Reference</label>
                    <input type="text" id="reference" name="reference" value="${not empty reference ? reference : param.reference}">
                    <c:if test="${not empty erreurs.reference}"><div class="erreur">${erreurs.reference}</div></c:if>
                </div>

                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="dateDebut">Date debut</label>
                        <input type="date" id="dateDebut" name="dateDebut" value="${not empty dateDebut ? dateDebut : param.dateDebut}">
                        <c:if test="${not empty erreurs.dateDebut}"><div class="erreur">${erreurs.dateDebut}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="dateFin">Date fin</label>
                        <input type="date" id="dateFin" name="dateFin" value="${not empty dateFin ? dateFin : param.dateFin}">
                        <c:if test="${not empty erreurs.dateFin}"><div class="erreur">${erreurs.dateFin}</div></c:if>
                    </div>
                </div>

                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/duplicata/etape4">&larr; Precedent</a>
                    <button type="submit" class="btn btn-primary">Suivant &rarr;</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
