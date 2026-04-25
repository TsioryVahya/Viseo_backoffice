<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Duplicata - Etape 2</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <h1 class="page-title">Demande de Duplicata</h1>
        <p class="page-subtitle">Renseignez les informations du passeport et du visa transformable.</p>

        <div class="stepper">
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Recherche</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Infos personnelles</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item active"><div><div class="stepper-node">3</div><div class="stepper-label">Passeport et Visa</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">4</div><div class="stepper-label">Pieces</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">5</div><div class="stepper-label">Upload</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">6</div><div class="stepper-label">Nouveau titre</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">7</div><div class="stepper-label">Ancienne carte</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">8</div><div class="stepper-label">Resume</div></div></div>
        </div>

        <section class="card">
            <h2 style="margin-top: 0;">Passeport</h2>

            <form method="post" action="${pageContext.request.contextPath}/duplicata/etape2">
                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="numeroPasseport">Numero passeport</label>
                        <input type="text" id="numeroPasseport" name="numeroPasseport" value="${not empty passeportData ? passeportData['numero'] : param.numeroPasseport}">
                        <c:if test="${not empty erreurs.numeroPasseport}"><div class="erreur">${erreurs.numeroPasseport}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="paysDelivrance">Pays de delivrance</label>
                        <input type="text" id="paysDelivrance" name="paysDelivrance" value="${not empty passeportData ? passeportData['paysDelivrance'] : param.paysDelivrance}">
                        <c:if test="${not empty erreurs.paysDelivrance}"><div class="erreur">${erreurs.paysDelivrance}</div></c:if>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="dateDelivrance">Date delivrance</label>
                        <input type="date" id="dateDelivrance" name="dateDelivrance" value="${not empty passeportData ? passeportData['dateDelivrance'] : param.dateDelivrance}">
                        <c:if test="${not empty erreurs.dateDelivrance}"><div class="erreur">${erreurs.dateDelivrance}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="dateExpiration">Date expiration</label>
                        <input type="date" id="dateExpiration" name="dateExpiration" value="${not empty passeportData ? passeportData['dateExpiration'] : param.dateExpiration}">
                        <c:if test="${not empty erreurs.dateExpiration}"><div class="erreur">${erreurs.dateExpiration}</div></c:if>
                    </div>
                </div>

                <hr style="border: 0; border-top: 1px solid var(--sand); margin: 1.2rem 0;">
                <h3 style="margin-top: 0; color: var(--green);">Visa transformable</h3>

                <div class="form-row">
                    <label for="numeroReferenceVisa">Numero de reference du visa transformable</label>
                    <input type="text" id="numeroReferenceVisa" name="numeroReferenceVisa" value="${not empty numeroReferenceVisa ? numeroReferenceVisa : param.numeroReferenceVisa}">
                    <c:if test="${not empty erreurs.numeroReferenceVisa}"><div class="erreur">${erreurs.numeroReferenceVisa}</div></c:if>
                </div>

                <div class="form-row">
                    <label for="dateExpirationVisa">Date d'expiration du visa transformable</label>
                    <input type="date" id="dateExpirationVisa" name="dateExpirationVisa" value="${not empty dateExpirationVisa ? dateExpirationVisa : param.dateExpirationVisa}">
                    <c:if test="${not empty erreurs.dateExpirationVisa}"><div class="erreur">${erreurs.dateExpirationVisa}</div></c:if>
                </div>

                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/duplicata/etape1">&larr; Precedent</a>
                    <button type="submit" class="btn btn-primary">Suivant &rarr;</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
