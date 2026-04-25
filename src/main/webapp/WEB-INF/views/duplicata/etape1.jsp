<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Duplicata - Etape 1</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <h1 class="page-title">Demande de Duplicata</h1>
        <p class="page-subtitle">Renseignez les informations personnelles du demandeur.</p>

        <div class="stepper">
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Recherche</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item active"><div><div class="stepper-node">2</div><div class="stepper-label">Infos personnelles</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">3</div><div class="stepper-label">Passeport et Visa</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">4</div><div class="stepper-label">Pieces</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">5</div><div class="stepper-label">Upload</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">6</div><div class="stepper-label">Nouveau titre</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">7</div><div class="stepper-label">Ancienne carte</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">8</div><div class="stepper-label">Resume</div></div></div>
        </div>

        <section class="card">
            <h2 style="margin-top: 0;">Informations personnelles</h2>

            <form method="post" action="${pageContext.request.contextPath}/duplicata/etape1">
                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="nom">Nom</label>
                        <input type="text" id="nom" name="nom" value="${not empty demandeur ? demandeur.nom : param.nom}">
                        <c:if test="${not empty erreurs.nom}"><div class="erreur">${erreurs.nom}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="prenom">Prenom</label>
                        <input type="text" id="prenom" name="prenom" value="${not empty demandeur ? demandeur.prenom : param.prenom}">
                        <c:if test="${not empty erreurs.prenom}"><div class="erreur">${erreurs.prenom}</div></c:if>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="dateNaissance">Date de naissance</label>
                        <input type="date" id="dateNaissance" name="dateNaissance" value="${not empty demandeur ? demandeur.dateNaissance : param.dateNaissance}">
                        <c:if test="${not empty erreurs.dateNaissance}"><div class="erreur">${erreurs.dateNaissance}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="lieuNaissance">Lieu de naissance</label>
                        <input type="text" id="lieuNaissance" name="lieuNaissance" value="${not empty demandeur ? demandeur.lieuNaissance : param.lieuNaissance}">
                        <c:if test="${not empty erreurs.lieuNaissance}"><div class="erreur">${erreurs.lieuNaissance}</div></c:if>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="telephone">Telephone</label>
                        <input type="text" id="telephone" name="telephone" value="${not empty demandeur ? demandeur.telephone : param.telephone}">
                        <c:if test="${not empty erreurs.telephone}"><div class="erreur">${erreurs.telephone}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" value="${not empty demandeur ? demandeur.email : param.email}">
                        <c:if test="${not empty erreurs.email}"><div class="erreur">${erreurs.email}</div></c:if>
                    </div>
                </div>

                <div class="form-row">
                    <label for="adresse">Adresse</label>
                    <textarea id="adresse" name="adresse">${not empty demandeur ? demandeur.adresse : param.adresse}</textarea>
                    <c:if test="${not empty erreurs.adresse}"><div class="erreur">${erreurs.adresse}</div></c:if>
                </div>

                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="idNationalite">Nationalite</label>
                        <select id="idNationalite" name="idNationalite">
                            <option value="">-- Selectionner --</option>
                            <c:forEach items="${nationalites}" var="nationalite">
                                <option value="${nationalite.id}" <c:if test="${not empty demandeur and demandeur.nationalite.id == nationalite.id}">selected</c:if>>
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
                                <option value="${situation.id}" <c:if test="${not empty demandeur and demandeur.situationFamiliale.id == situation.id}">selected</c:if>>
                                    ${situation.libelle}
                                </option>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty erreurs.idSituationFamiliale}"><div class="erreur">${erreurs.idSituationFamiliale}</div></c:if>
                    </div>
                </div>

                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/duplicata/recherche">&larr; Retour</a>
                    <button type="submit" class="btn btn-primary">Suivant &rarr;</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
