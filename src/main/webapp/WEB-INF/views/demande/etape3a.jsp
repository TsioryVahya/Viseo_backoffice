<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande - Etape 3A</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <h1 class="page-title">Nouvelle demande</h1>
        <p class="page-subtitle">Selectionnez le type de visa et les pieces communes.</p>
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
            <h2 style="margin-top: 0;">Type de visa et pieces communes</h2>
            <c:if test="${not empty avertissementVisa}">
                <div class="erreur" style="margin-bottom: 1rem; border: 1px solid #d07f2c; background: #fff4e5; color: #8a5d20; padding: 0.7rem; border-radius: 8px;">
                    ${avertissementVisa}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/demande/nouveau/etape3a">
                <div class="form-row">
                    <label for="dateDemande">Date de la demande</label>
                    <input type="date" id="dateDemande" name="dateDemande" value="${dateDemande}" required>
                    <c:if test="${not empty erreurs.dateDemande}"><div class="erreur">${erreurs.dateDemande}</div></c:if>
                </div>

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
                    <label>Pieces communes</label>
                    <div class="checkbox-grid">
                        <c:forEach items="${piecesCommunes}" var="piece">
                            <label class="checkbox-item">
                                <input type="checkbox" name="pieceCommune_${piece.id}" <c:if test="${piecesCommunesSelection[piece.id]}">checked</c:if>>
                                <span>${piece.libelle} <c:if test="${piece.obligatoire}">(obligatoire)</c:if></span>
                            </label>
                        </c:forEach>
                    </div>
                </div>

                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/nouveau/etape2">&larr; Precedent</a>
                    <button type="submit" class="btn btn-primary">Suivant &rarr;</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
