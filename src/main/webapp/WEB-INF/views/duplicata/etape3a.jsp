<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Duplicata - Etape 3A</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <h1 class="page-title">Demande de Duplicata</h1>
        <p class="page-subtitle">Selectionnez le type de visa, la date de demande et les pieces communes.</p>

        <div class="stepper">
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Recherche</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Infos personnelles</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Passeport et Visa</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item active"><div><div class="stepper-node">4</div><div class="stepper-label">Pieces</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">5</div><div class="stepper-label">Upload</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">6</div><div class="stepper-label">Nouveau titre</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">7</div><div class="stepper-label">Ancienne carte</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">8</div><div class="stepper-label">Resume</div></div></div>
        </div>

        <section class="card">
            <h2 style="margin-top: 0;">Type de visa et pieces communes</h2>

            <form method="post" action="${pageContext.request.contextPath}/duplicata/etape3a">
                <div class="form-row">
                    <label for="typeVisaId">Type de visa</label>
                    <select id="typeVisaId" name="typeVisaId">
                        <option value="">-- Selectionner --</option>
                        <c:forEach items="${typesVisa}" var="typeVisa">
                            <option value="${typeVisa.id}" <c:if test="${typeVisaId == typeVisa.id}">selected</c:if>>
                                ${typeVisa.libelle}
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty erreurs.typeVisaId}"><div class="erreur">${erreurs.typeVisaId}</div></c:if>
                </div>

                <div class="form-row">
                    <label for="dateDemande">Date de la demande</label>
                    <input type="date" id="dateDemande" name="dateDemande" value="${not empty dateDemande ? dateDemande : ''}">
                    <c:if test="${not empty erreurs.dateDemande}"><div class="erreur">${erreurs.dateDemande}</div></c:if>
                </div>

                <div class="form-row">
                    <label>Pieces communes</label>
                    <div class="checkbox-grid">
                        <c:forEach items="${piecesCommunes}" var="piece">
                            <label class="checkbox-item">
                                <input type="checkbox" name="piece_${piece.id}"
                                       <c:if test="${not empty piecesChecked and piecesChecked[piece.id]}">checked</c:if>>
                                <span>${piece.libelle} <c:if test="${piece.obligatoire}">(obligatoire)</c:if></span>
                            </label>
                        </c:forEach>
                    </div>
                    <c:if test="${not empty erreurs.piecesCommunes}"><div class="erreur">${erreurs.piecesCommunes}</div></c:if>
                </div>

                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/duplicata/etape2">&larr; Precedent</a>
                    <button type="submit" class="btn btn-primary">Suivant &rarr;</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
