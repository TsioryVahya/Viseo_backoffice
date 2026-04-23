<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demande de Duplicata / Transfert - Nouveau Dossier</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <h1 class="page-title">Demande de Duplicata / Transfert</h1>
        <p class="page-subtitle">Créer un nouveau dossier - Renseignez les informations du demandeur.</p>

        <c:if test="${not empty message}">
            <div class="card" style="background: #d4edda; border-color: #c3e6cb; color: #155724; margin-bottom: 1rem;">
                <strong>Nouveau dossier</strong> : ${message}
            </div>
        </c:if>

        <c:if test="${not empty erreur}">
            <div class="card" style="background: #f8d7da; border-color: #f5c6cb; color: #721c24; margin-bottom: 1rem;">
                <strong>Erreur</strong> : ${erreur}
            </div>
        </c:if>

        <section class="card">
            <h2 style="margin-top: 0;">Informations du Demandeur</h2>
            <p style="color: var(--gris-text); margin-bottom: 1.5rem;">
                Complétez les informations personnelles pour créer un nouveau dossier.
            </p>

            <form method="post" action="${pageContext.request.contextPath}/demande/duplicata-transfert/valider">
                
                <!-- Type de demande -->
                <div class="form-row">
                    <label for="idTypeDemande">Type de demande *</label>
                    <select id="idTypeDemande" name="idTypeDemande" required>
                        <option value="">-- Sélectionner un type --</option>
                        <c:forEach var="type" items="${typesDemande}">
                            <option value="${type.id}" <c:if test="${idTypeDemande == type.id}">selected</c:if>>${type.libelle}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Situation Familiale et Nationalité -->
                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="idSituationFamiliale">Situation familiale *</label>
                        <select id="idSituationFamiliale" name="idSituationFamiliale" required>
                            <option value="">-- Sélectionner une situation --</option>
                            <c:forEach var="situation" items="${situationsFamiliales}">
                                <option value="${situation.id}">${situation.libelle}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-row">
                        <label for="idNationalite">Nationalité *</label>
                        <select id="idNationalite" name="idNationalite" required>
                            <option value="">-- Sélectionner une nationalité --</option>
                            <c:forEach var="nat" items="${nationalites}">
                                <option value="${nat.id}">${nat.libelle}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <!-- Type de Visa (obligatoire) -->
                <div class="form-row">
                    <label for="idTypeVisa">Type de visa *</label>
                    <select id="idTypeVisa" name="idTypeVisa" required>
                        <option value="">-- Sélectionner un type de visa --</option>
                        <c:forEach var="tv" items="${typesVisa}">
                            <option value="${tv.id}">${tv.libelle}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Nom et Prénom -->
                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="nom">Nom *</label>
                        <input type="text" id="nom" name="nom" value="${demandeur.nom}" required>
                        <c:if test="${not empty erreurs.nom}"><div class="erreur">${erreurs.nom}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="prenom">Prénom *</label>
                        <input type="text" id="prenom" name="prenom" value="${demandeur.prenom}" required>
                        <c:if test="${not empty erreurs.prenom}"><div class="erreur">${erreurs.prenom}</div></c:if>
                    </div>
                </div>

                <!-- Date et Lieu de naissance -->
                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="dateNaissance">Date de naissance *</label>
                        <input type="date" id="dateNaissance" name="dateNaissance" value="${demandeur.dateNaissance}" required>
                        <c:if test="${not empty erreurs.dateNaissance}"><div class="erreur">${erreurs.dateNaissance}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="lieuNaissance">Lieu de naissance *</label>
                        <input type="text" id="lieuNaissance" name="lieuNaissance" value="${demandeur.lieuNaissance}" required>
                        <c:if test="${not empty erreurs.lieuNaissance}"><div class="erreur">${erreurs.lieuNaissance}</div></c:if>
                    </div>
                </div>

                <!-- Téléphone et Email -->
                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="telephone">Téléphone *</label>
                        <input type="text" id="telephone" name="telephone" value="${demandeur.telephone}" required>
                        <c:if test="${not empty erreurs.telephone}"><div class="erreur">${erreurs.telephone}</div></c:if>
                    </div>

                    <div class="form-row">
                        <label for="email">Email *</label>
                        <input type="email" id="email" name="email" value="${demandeur.email}" required>
                        <c:if test="${not empty erreurs.email}"><div class="erreur">${erreurs.email}</div></c:if>
                    </div>
                </div>

                <!-- Adresse -->
                <div class="form-row">
                    <label for="adresse">Adresse *</label>
                    <textarea id="adresse" name="adresse" required>${demandeur.adresse}</textarea>
                    <c:if test="${not empty erreurs.adresse}"><div class="erreur">${erreurs.adresse}</div></c:if>
                </div>

                <!-- Actions -->
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/duplicata-transfert/recherche">
                        ← Retour
                    </a>
                    <button type="submit" class="btn btn-primary">
                        Soumettre et Approuver →
                    </button>
                </div>
            </form>

            <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--sand); color: var(--gris-text); font-size: 14px;">
                <strong>Remarque :</strong> Ce formulaire crée un dossier pour une demande de <strong>Duplicata</strong> ou <strong>Transfert</strong>. 
                La demande sera automatiquement approuvée avec le statut <strong>"Titre délivré"</strong> une fois soumise.
            </div>
        </section>
    </main>
</div>
</body>
</html>
