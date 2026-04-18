<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande - Etape 1</title>
    <jsp:include page="/WEB-INF/views/common/styles.jspf" />
</head>
<body>
<div class="container">
    <div class="steps">
        <span class="step active">[1]</span>
        <span class="step">2</span>
        <span class="step">3</span>
        <span class="step">4</span>
    </div>

    <h2>Informations personnelles</h2>

    <form method="post" action="${pageContext.request.contextPath}/demande/nouveau/etape1">
        <div class="form-row">
            <label for="nom">Nom</label>
            <input type="text" id="nom" name="nom" value="${form.nom}">
            <c:if test="${not empty erreurs.nom}"><div class="erreur">${erreurs.nom}</div></c:if>
        </div>

        <div class="form-row">
            <label for="prenom">Prenom</label>
            <input type="text" id="prenom" name="prenom" value="${form.prenom}">
            <c:if test="${not empty erreurs.prenom}"><div class="erreur">${erreurs.prenom}</div></c:if>
        </div>

        <div class="form-row">
            <label for="dateNaissance">Date de naissance</label>
            <input type="date" id="dateNaissance" name="dateNaissance" value="${form.dateNaissance}">
            <c:if test="${not empty erreurs.dateNaissance}"><div class="erreur">${erreurs.dateNaissance}</div></c:if>
        </div>

        <div class="form-row">
            <label for="lieuNaissance">Lieu de naissance</label>
            <input type="text" id="lieuNaissance" name="lieuNaissance" value="${form.lieuNaissance}">
            <c:if test="${not empty erreurs.lieuNaissance}"><div class="erreur">${erreurs.lieuNaissance}</div></c:if>
        </div>

        <div class="form-row">
            <label for="telephone">Telephone</label>
            <input type="text" id="telephone" name="telephone" value="${form.telephone}">
            <c:if test="${not empty erreurs.telephone}"><div class="erreur">${erreurs.telephone}</div></c:if>
        </div>

        <div class="form-row">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="${form.email}">
            <c:if test="${not empty erreurs.email}"><div class="erreur">${erreurs.email}</div></c:if>
        </div>

        <div class="form-row">
            <label for="adresse">Adresse</label>
            <textarea id="adresse" name="adresse">${form.adresse}</textarea>
            <c:if test="${not empty erreurs.adresse}"><div class="erreur">${erreurs.adresse}</div></c:if>
        </div>

        <div class="form-row">
            <label for="idNationalite">Nationalite</label>
            <select id="idNationalite" name="idNationalite">
                <option value="">-- Selectionner --</option>
                <c:forEach items="${nationalites}" var="nationalite">
                    <option value="${nationalite.id}" <c:if test="${form.idNationalite == nationalite.id.toString()}">selected</c:if>>
                        ${nationalite.libelle}
                    </option>
                </c:forEach>
            </select>
            <c:if test="${not empty erreurs.idNationalite}"><div class="erreur">${erreurs.idNationalite}</div></c:if>
        </div>

        <div class="form-row">
            <label for="idSituationFamiliale">Situation familiale</label>
            <select id="idSituationFamiliale" name="idSituationFamiliale">
                <option value="">-- Selectionner --</option>
                <c:forEach items="${situationsFamiliales}" var="situation">
                    <option value="${situation.id}" <c:if test="${form.idSituationFamiliale == situation.id.toString()}">selected</c:if>>
                        ${situation.libelle}
                    </option>
                </c:forEach>
            </select>
            <c:if test="${not empty erreurs.idSituationFamiliale}"><div class="erreur">${erreurs.idSituationFamiliale}</div></c:if>
        </div>

        <div class="actions">
            <button type="submit" class="btn">Suivant -&gt;</button>
        </div>
    </form>
</div>
</body>
</html>
