<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Renouvellement - Recherche</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <h1 class="page-title">Demande de renouvellement</h1>
        <p class="page-subtitle">Choisissez le type de renouvellement puis recherchez le demandeur avec son nom, prenom et numero de passeport.</p>

        <c:if test="${not empty info}">
            <div class="alert-info">${info}</div>
        </c:if>

        <section class="card">
            <h2 style="margin-top: 0;">Recherche du demandeur pour renouvellement</h2>

            <form method="post" action="${pageContext.request.contextPath}/duplicata/recherche">
                <div class="form-row">
                    <label for="typeDemandeId">Type de renouvellement *</label>
                    <select name="typeDemandeId" id="typeDemandeId" required>
                        <option value="">-- Choisir --</option>
                        <c:forEach var="td" items="${typesDemande}">
                            <option value="${td.id}" ${td.id == param.typeDemandeId ? 'selected' : ''}>
                                ${td.libelle}
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty erreurs['typeDemandeId']}">
                        <span class="erreur">${erreurs['typeDemandeId']}</span>
                    </c:if>
                </div>

                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="nom">Nom</label>
                        <input type="text" id="nom" name="nom" value="${param.nom}">
                        <c:if test="${not empty erreurs.nom}"><div class="erreur">${erreurs.nom}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="prenom">Prenom</label>
                        <input type="text" id="prenom" name="prenom" value="${param.prenom}">
                        <c:if test="${not empty erreurs.prenom}"><div class="erreur">${erreurs.prenom}</div></c:if>
                    </div>
                </div>

                <div class="form-row">
                    <label for="numeroPasseport">Numero de passeport</label>
                    <input type="text" id="numeroPasseport" name="numeroPasseport" value="${param.numeroPasseport}">
                    <c:if test="${not empty erreurs.numeroPasseport}"><div class="erreur">${erreurs.numeroPasseport}</div></c:if>
                </div>

                <div class="actions" style="justify-content: flex-end;">
                    <button type="submit" class="btn btn-primary">Continuer -&gt;</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
