<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande - Etape 3A</title>
    <jsp:include page="/WEB-INF/views/common/styles.jspf" />
</head>
<body>
<div class="container">
    <div class="steps">
        <span class="step">1</span>
        <span class="step">2</span>
        <span class="step active">[3]</span>
        <span class="step">4</span>
    </div>

    <h2>Type de visa + Pieces communes</h2>

    <form method="post" action="${pageContext.request.contextPath}/demande/nouveau/etape3a">
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
            <div class="checkbox-list">
                <c:forEach items="${piecesCommunes}" var="piece">
                    <div class="checkbox-item">
                        <label>
                            <input type="checkbox" name="pieceCommune_${piece.id}" <c:if test="${piecesCommunesSelection[piece.id]}">checked</c:if>>
                            ${piece.libelle}
                            <c:if test="${piece.obligatoire}">(obligatoire)</c:if>
                        </label>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="actions">
            <a class="btn secondary" href="${pageContext.request.contextPath}/demande/nouveau/etape2">&lt;- Precedent</a>
            <button type="submit" class="btn">Suivant -&gt;</button>
        </div>
    </form>
</div>
</body>
</html>
