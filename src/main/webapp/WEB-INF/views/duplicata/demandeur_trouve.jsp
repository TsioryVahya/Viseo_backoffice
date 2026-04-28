<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande de duplicata - Demandeur trouve</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <h1 class="page-title">Demande de duplicata - Demandeur trouve</h1>
        <p class="page-subtitle status-ok">✓ Le demandeur est deja enregistre dans le systeme.</p>

        <c:if test="${not empty erreur}">
            <div class="alert-erreur">${erreur}</div>
        </c:if>

        <section class="card">
            <form method="post" action="${pageContext.request.contextPath}/duplicata/demandeur-trouve">
                <h2 style="margin-top: 0;">Date de la demande</h2>
                <div class="form-row">
                    <label for="dateDemande">Date de la demande *</label>
                    <input type="date" id="dateDemande" name="dateDemande"
                           value="${not empty dateDemande ? dateDemande : today}" required>
                    <c:if test="${not empty erreurs['dateDemande']}">
                        <span class="erreur">${erreurs['dateDemande']}</span>
                    </c:if>
                </div>

                <h2>Choix des titres a dupliquer</h2>
                <p class="page-subtitle" style="margin-bottom: 1rem;">Cochez les titres a dupliquer (au moins un obligatoire)</p>
                <c:if test="${not empty erreurs['titres']}">
                    <div class="alert-erreur">${erreurs['titres']}</div>
                </c:if>

                <div class="form-row">
                    <label class="inline-check">
                        <input type="checkbox" id="cocheVisa" name="cocheVisa" value="true" onchange="toggleFormVisa(this)">
                        Visa
                    </label>
                </div>

                <div id="form-visa" style="display:none;" class="form-inline-card">
                    <label for="referenceVisa">Reference du visa *</label>
                    <input type="text" id="referenceVisa" name="referenceVisa"
                           placeholder="Ex: VIS-2024-001" value="${param.referenceVisa}">
                    <c:if test="${not empty erreurs['referenceVisa']}">
                        <span class="erreur">${erreurs['referenceVisa']}</span>
                    </c:if>
                </div>

                <div class="form-row" style="margin-top: 1rem;">
                    <label class="inline-check">
                        <input type="checkbox" id="cocheCarte" name="cocheCarte" value="true" onchange="toggleFormCarte(this)">
                        Carte de resident
                    </label>
                </div>

                <div id="form-carte" style="display:none;" class="form-inline-card">
                    <label for="referenceCarte">Reference de la carte *</label>
                    <input type="text" id="referenceCarte" name="referenceCarte"
                           placeholder="Ex: CR-2024-001" value="${param.referenceCarte}">
                    <c:if test="${not empty erreurs['referenceCarte']}">
                        <span class="erreur">${erreurs['referenceCarte']}</span>
                    </c:if>
                </div>

                <div class="actions" style="justify-content: flex-end;">
                    <button type="submit" class="btn btn-primary">Confirmer -&gt;</button>
                </div>
            </form>
        </section>
    </main>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var cocheVisa = document.getElementById('cocheVisa');
        var cocheCarte = document.getElementById('cocheCarte');

        if ('${param.cocheVisa}' === 'true') {
            cocheVisa.checked = true;
            document.getElementById('form-visa').style.display = 'block';
        }
        if ('${param.cocheCarte}' === 'true') {
            cocheCarte.checked = true;
            document.getElementById('form-carte').style.display = 'block';
        }
    });

    function toggleFormVisa(checkbox) {
        var formVisa = document.getElementById('form-visa');
        formVisa.style.display = checkbox.checked ? 'block' : 'none';
        if (!checkbox.checked) {
            formVisa.querySelector('input[name="referenceVisa"]').value = '';
        }
    }

    function toggleFormCarte(checkbox) {
        var formCarte = document.getElementById('form-carte');
        formCarte.style.display = checkbox.checked ? 'block' : 'none';
        if (!checkbox.checked) {
            formCarte.querySelector('input[name="referenceCarte"]').value = '';
        }
    }
</script>
</body>
</html>
