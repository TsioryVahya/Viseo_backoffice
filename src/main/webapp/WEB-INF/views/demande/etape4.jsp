<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande - Etape 4</title>
    <jsp:include page="/WEB-INF/views/common/styles.jspf" />
</head>
<body>
<div class="container">
    <div class="steps">
        <span class="step">1</span>
        <span class="step">2</span>
        <span class="step">3</span>
        <span class="step active">[4]</span>
    </div>

    <h2>Resume et confirmation</h2>
    <c:if test="${not empty erreurs.global}"><div class="erreur">${erreurs.global}</div></c:if>

    <div class="resume-section">
        <h3>Informations personnelles</h3>
        <p><strong>Nom:</strong> ${demandeur.nom}</p>
        <p><strong>Prenom:</strong> ${demandeur.prenom}</p>
        <p><strong>Date de naissance:</strong> <fmt:formatDate value="${dateNaissanceDate}" pattern="dd/MM/yyyy" /></p>
        <p><strong>Lieu de naissance:</strong> ${demandeur.lieuNaissance}</p>
        <p><strong>Telephone:</strong> ${demandeur.telephone}</p>
        <p><strong>Email:</strong> ${demandeur.email}</p>
        <p><strong>Adresse:</strong> ${demandeur.adresse}</p>
        <p><strong>Nationalite:</strong> ${demandeur.nationalite.libelle}</p>
        <p><strong>Situation familiale:</strong> ${demandeur.situationFamiliale.libelle}</p>
    </div>

    <div class="resume-section">
        <h3>Passeport</h3>
        <p><strong>Numero:</strong> ${passeport.numeroPasseport}</p>
        <p><strong>Date delivrance:</strong> <fmt:formatDate value="${dateDelivranceDate}" pattern="dd/MM/yyyy" /></p>
        <p><strong>Date expiration:</strong> <fmt:formatDate value="${dateExpirationDate}" pattern="dd/MM/yyyy" /></p>
        <p><strong>Pays delivrance:</strong> ${passeport.paysDelivrance}</p>
    </div>

    <div class="resume-section">
        <h3>Type de visa</h3>
        <p>${typeVisa.libelle}</p>
    </div>

    <div class="resume-section">
        <h3>Pieces communes</h3>
        <ul>
            <c:forEach items="${typesPiecesCommunes}" var="piece">
                <li>
                    <c:choose>
                        <c:when test="${piecesCommunesSelection[piece.id]}"><span class="badge-ok">✓</span></c:when>
                        <c:otherwise><span class="badge-ko">✗</span></c:otherwise>
                    </c:choose>
                    ${piece.libelle}
                </li>
            </c:forEach>
        </ul>
    </div>

    <div class="resume-section">
        <h3>Pieces specifiques</h3>
        <ul>
            <c:forEach items="${typesPiecesSpecifiques}" var="piece">
                <li>
                    <c:choose>
                        <c:when test="${piecesSpecifiquesSelection[piece.id]}"><span class="badge-ok">✓</span></c:when>
                        <c:otherwise><span class="badge-ko">✗</span></c:otherwise>
                    </c:choose>
                    ${piece.libelle}
                </li>
            </c:forEach>
            <c:if test="${empty typesPiecesSpecifiques}"><li>Aucune piece specifique.</li></c:if>
        </ul>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/demande/nouveau/etape4">
        <div class="actions">
            <a class="btn secondary" href="${pageContext.request.contextPath}/demande/nouveau/etape1">&lt;- Modifier</a>
            <button type="submit" class="btn">Confirmer et soumettre</button>
        </div>
    </form>
</div>
</body>
</html>
