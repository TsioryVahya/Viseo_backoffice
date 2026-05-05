<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transfert — Ancien visa</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css"/>
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp"/>
    <main class="main-content">

        <%-- Stepper 7 étapes, étape 5 active --%>
        <%-- étapes : Infos > Passeport > Pièces > Upload > [Ancien visa] > Nouveau passeport > Confirmation --%>

        <div class="page-header">
            <h1>Informations de l'ancien visa</h1>
            <p class="sous-titre">
                Saisissez les informations du visa qui sera marqué
                comme "Passeport expiré".
            </p>
        </div>

        <c:if test="${not empty erreur}">
            <div class="alert-erreur">${erreur}</div>
        </c:if>

        <div class="card">
            <form method="post"
                  action="${pageContext.request.contextPath}/duplicata/etape5">

                <div class="form-group">
                    <label for="reference">Référence du visa *</label>
                    <input type="text" id="reference" name="reference"
                           value="${reference}"
                           placeholder="Ex: VIS-2024-001" required/>
                    <c:if test="${not empty erreurs['reference']}">
                        <span class="erreur">${erreurs['reference']}</span>
                    </c:if>
                </div>

                <div class="form-row-2col">
                    <div class="form-group">
                        <label for="dateDebut">Date de début *</label>
                        <input type="date" id="dateDebut" name="dateDebut"
                               value="${dateDebut}" required/>
                        <c:if test="${not empty erreurs['dateDebut']}">
                            <span class="erreur">${erreurs['dateDebut']}</span>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="dateFin">Date de fin *</label>
                        <input type="date" id="dateFin" name="dateFin"
                               value="${dateFin}" required/>
                        <c:if test="${not empty erreurs['dateFin']}">
                            <span class="erreur">${erreurs['dateFin']}</span>
                        </c:if>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/transfert/etape4"
                       class="btn-secondaire">← Précédent</a>
                    <button type="submit" class="btn-primaire">
                        Suivant →
                    </button>
                </div>
            </form>
        </div>
    </main>
</div>
</body>
</html>