<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transfert — Nouveau passeport</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css"/>
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp"/>
    <main class="main-content">

        <div class="page-header">
            <h1>Nouveau passeport</h1>
            <p class="sous-titre">
                Saisissez les informations du nouveau passeport
                du demandeur.
            </p>
        </div>

        <c:if test="${not empty erreur}">
            <div class="alert-erreur">${erreur}</div>
        </c:if>

        <div class="card">
            <form method="post"
                  action="${pageContext.request.contextPath}/transfert/etape6">

                <div class="form-group">
                    <label for="numeroPasseport">
                        Numéro de passeport *
                    </label>
                    <input type="text" id="numeroPasseport"
                           name="numeroPasseport"
                           value="${numeroPasseport}"
                           placeholder="Ex: AB123456" required/>
                    <c:if test="${not empty erreurs['numeroPasseport']}">
                        <span class="erreur">
                            ${erreurs['numeroPasseport']}
                        </span>
                    </c:if>
                </div>

                <div class="form-row-2col">
                    <div class="form-group">
                        <label for="dateDelivrance">
                            Date de délivrance *
                        </label>
                        <input type="date" id="dateDelivrance"
                               name="dateDelivrance"
                               value="${dateDelivrance}" required/>
                        <c:if test="${not empty erreurs['dateDelivrance']}">
                            <span class="erreur">
                                ${erreurs['dateDelivrance']}
                            </span>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="dateExpiration">
                            Date d'expiration *
                        </label>
                        <input type="date" id="dateExpiration"
                               name="dateExpiration"
                               value="${dateExpiration}" required/>
                        <c:if test="${not empty erreurs['dateExpiration']}">
                            <span class="erreur">
                                ${erreurs['dateExpiration']}
                            </span>
                        </c:if>
                    </div>
                </div>

                <div class="form-group">
                    <label for="paysDelivrance">
                        Pays de délivrance *
                    </label>
                    <input type="text" id="paysDelivrance"
                           name="paysDelivrance"
                           value="${paysDelivrance}"
                           placeholder="Ex: France" required/>
                    <c:if test="${not empty erreurs['paysDelivrance']}">
                        <span class="erreur">
                            ${erreurs['paysDelivrance']}
                        </span>
                    </c:if>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/transfert/etape5"
                       class="btn-secondaire">← Précédent</a>
                    <button type="submit" class="btn-primaire">
                        Confirmer et soumettre →
                    </button>
                </div>
            </form>
        </div>
    </main>
</div>
</body>
</html>