<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande - Etape 3B</title>
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

    <h2>Pieces specifiques</h2>

    <div class="form-row">
        <label>Type de visa selectionne</label>
        <input type="text" value="${typeVisa.libelle}" readonly>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/demande/nouveau/etape3b">
        <div class="form-row">
            <label>Pieces specifiques</label>
            <div class="checkbox-list">
                <c:forEach items="${piecesSpecifiques}" var="piece">
                    <div class="checkbox-item">
                        <label>
                            <input type="checkbox" name="pieceSpecifique_${piece.id}" <c:if test="${piecesSpecifiquesSelection[piece.id]}">checked</c:if>>
                            ${piece.libelle}
                            <c:if test="${piece.obligatoire}">(obligatoire)</c:if>
                        </label>
                    </div>
                </c:forEach>
                <c:if test="${empty piecesSpecifiques}">
                    <p>Aucune piece specifique pour ce type de visa.</p>
                </c:if>
            </div>
        </div>

        <div class="actions">
            <a class="btn secondary" href="${pageContext.request.contextPath}/demande/nouveau/etape3a">&lt;- Precedent</a>
            <button type="submit" class="btn">Suivant -&gt;</button>
        </div>
    </form>
</div>
</body>
</html>
