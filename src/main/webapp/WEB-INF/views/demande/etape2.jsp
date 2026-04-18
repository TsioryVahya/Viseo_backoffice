<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande - Etape 2</title>
    <jsp:include page="/WEB-INF/views/common/styles.jspf" />
</head>
<body>
<div class="container">
    <div class="steps">
        <span class="step">1</span>
        <span class="step active">[2]</span>
        <span class="step">3</span>
        <span class="step">4</span>
    </div>

    <h2>Passeport</h2>

    <form method="post" action="${pageContext.request.contextPath}/demande/nouveau/etape2">
        <div class="form-row">
            <label for="numeroPasseport">Numero passeport</label>
            <input type="text" id="numeroPasseport" name="numeroPasseport" value="${form.numeroPasseport}">
            <c:if test="${not empty erreurs.numeroPasseport}"><div class="erreur">${erreurs.numeroPasseport}</div></c:if>
        </div>

        <div class="form-row">
            <label for="dateDelivrance">Date delivrance</label>
            <input type="date" id="dateDelivrance" name="dateDelivrance" value="${form.dateDelivrance}">
            <c:if test="${not empty erreurs.dateDelivrance}"><div class="erreur">${erreurs.dateDelivrance}</div></c:if>
        </div>

        <div class="form-row">
            <label for="dateExpiration">Date expiration</label>
            <input type="date" id="dateExpiration" name="dateExpiration" value="${form.dateExpiration}">
            <c:if test="${not empty erreurs.dateExpiration}"><div class="erreur">${erreurs.dateExpiration}</div></c:if>
        </div>

        <div class="form-row">
            <label for="paysDelivrance">Pays de delivrance</label>
            <input type="text" id="paysDelivrance" name="paysDelivrance" value="${form.paysDelivrance}">
        </div>

        <div class="actions">
            <a class="btn secondary" href="${pageContext.request.contextPath}/demande/nouveau/etape1">&lt;- Precedent</a>
            <button type="submit" class="btn">Suivant -&gt;</button>
        </div>
    </form>
</div>
</body>
</html>
