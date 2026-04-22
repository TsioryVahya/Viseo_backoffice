<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Recherche demande titre</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />
    <main class="main-content">
        <div class="stepper">
            <c:forEach begin="1" end="7" var="i">
                <div class="stepper-item ${i == etapeActuelle ? 'active' : (i < etapeActuelle ? 'done' : 'future')}">
                    <div>
                        <div class="stepper-node">${i}</div>
                        <div class="stepper-label">
                            <c:choose>
                                <c:when test="${i == 1}">Recherche</c:when>
                                <c:when test="${i == 2}">Infos personnelles</c:when>
                                <c:when test="${i == 3}">Passeport</c:when>
                                <c:when test="${i == 4}">Pieces</c:when>
                                <c:when test="${i == 5}">Upload</c:when>
                                <c:when test="${i == 6}">Titre</c:when>
                                <c:otherwise>Resume</c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <c:if test="${i < 7}"><div class="stepper-track"></div></c:if>
                </div>
            </c:forEach>
        </div>

        <h1 class="page-title">Nouvelle demande - ${typeDemande eq 'duplicata' ? 'Duplicata' : 'Transfert de visa'}</h1>

        <c:if test="${not empty erreurRecherche}">
            <div class="alert-erreur">${erreurRecherche}</div>
        </c:if>

        <section class="card">
            <form method="post" action="${pageContext.request.contextPath}/demande/autre/recherche">
                <div class="form-row">
                    <label for="type">Type de demande</label>
                    <select id="type" name="type">
                        <option value="duplicata" ${typeDemande eq 'duplicata' ? 'selected' : ''}>Duplicata</option>
                        <option value="transfert" ${typeDemande eq 'transfert' ? 'selected' : ''}>Transfert de visa</option>
                    </select>
                </div>
                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="nom">Nom</label>
                        <input id="nom" name="nom" type="text" value="${param.nom}">
                        <c:if test="${not empty erreurs.nom}"><div class="erreur">${erreurs.nom}</div></c:if>
                    </div>
                    <div class="form-row">
                        <label for="prenom">Prenom</label>
                        <input id="prenom" name="prenom" type="text" value="${param.prenom}">
                        <c:if test="${not empty erreurs.prenom}"><div class="erreur">${erreurs.prenom}</div></c:if>
                    </div>
                </div>
                <div class="form-row">
                    <label for="numeroPasseport">Numero de passeport</label>
                    <input id="numeroPasseport" name="numeroPasseport" type="text" value="${param.numeroPasseport}">
                    <c:if test="${not empty erreurs.numeroPasseport}"><div class="erreur">${erreurs.numeroPasseport}</div></c:if>
                </div>
                <div class="actions">
                    <button class="btn btn-primary" type="submit">Rechercher -></button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
