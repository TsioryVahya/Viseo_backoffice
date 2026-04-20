<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmation - Demandeur Trouvé</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<c:set var="activeMenu" value="duplicata-transfert" />

<div class="app-layout">
    <aside class="sidebar">
        <div class="sidebar-logo">Viseo Backoffice</div>
        <div class="sidebar-separator"></div>

        <nav class="sidebar-nav">
            <a class="sidebar-link ${activeMenu == 'nouvelle-demande' ? 'active' : ''}" href="${pageContext.request.contextPath}/demande/nouveau/etape1">Nouvelle demande</a>
            <a class="sidebar-link ${activeMenu == 'duplicata-transfert' ? 'active' : ''}" href="${pageContext.request.contextPath}/demande/duplicata-transfert/recherche">Duplicata / Transfert</a>
            <a class="sidebar-link ${activeMenu == 'liste-demandes' ? 'active' : ''}" href="#">Liste des demandes</a>
            <a class="sidebar-link ${activeMenu == 'demandeurs' ? 'active' : ''}" href="#">Demandeurs</a>
        </nav>

        <div class="sidebar-footer">v2.0 - Sprint 2</div>
    </aside>

    <main class="main-content">
        <h1 class="page-title">Confirmation</h1>
        <p class="page-subtitle">Demandeur trouvé - Validez la demande de ${typeDemande.libelle}</p>

        <section class="card">
            <h2 style="margin-top: 0;">Informations du Demandeur</h2>
            <p style="color: var(--gris-text); margin-bottom: 1rem;">
                Les informations suivantes ont été trouvées. Cliquez sur "Valider" pour créer la demande avec approbation immédiate.
            </p>

            <div style="background: var(--sand-light); border: 1px solid var(--sand); border-radius: 8px; padding: 1rem; margin-bottom: 1.5rem;">
                <div class="form-grid-2">
                    <div>
                        <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Nom</div>
                        <div style="font-size: 16px; font-weight: 500;">${demandeur.nom}</div>
                    </div>
                    <div>
                        <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Prénom</div>
                        <div style="font-size: 16px; font-weight: 500;">${demandeur.prenom}</div>
                    </div>
                </div>

                <div class="form-grid-2" style="margin-top: 1rem;">
                    <div>
                        <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Date de naissance</div>
                        <div style="font-size: 14px;">
                            ${demandeur.dateNaissance}
                        </div>
                    </div>
                    <div>
                        <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Lieu de naissance</div>
                        <div style="font-size: 14px;">${demandeur.lieuNaissance}</div>
                    </div>
                </div>

                <div class="form-grid-2" style="margin-top: 1rem;">
                    <div>
                        <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Téléphone</div>
                        <div style="font-size: 14px;">${demandeur.telephone}</div>
                    </div>
                    <div>
                        <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Email</div>
                        <div style="font-size: 14px;">${demandeur.email}</div>
                    </div>
                </div>

                <div style="margin-top: 1rem;">
                    <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Adresse</div>
                    <div style="font-size: 14px;">${demandeur.adresse}</div>
                </div>
            </div>

            <h3 style="margin-top: 1.5rem; color: var(--green);">Type de demande</h3>
            <div style="background: var(--sand-light); border: 1px solid var(--sand); border-radius: 8px; padding: 1rem; margin-bottom: 1.5rem;">
                <div style="font-size: 16px; font-weight: 500;">${typeDemande.libelle}</div>
            </div>

            <h3 style="margin-top: 1.5rem; color: var(--green);">Détails de la demande</h3>
            <form method="post" action="${pageContext.request.contextPath}/demande/duplicata-transfert/valider">
                <input type="hidden" name="nom" value="${demandeur.nom}">
                <input type="hidden" name="prenom" value="${demandeur.prenom}">
                <input type="hidden" name="dateNaissance" value="${demandeur.dateNaissance}">
                <input type="hidden" name="lieuNaissance" value="${demandeur.lieuNaissance}">
                <input type="hidden" name="telephone" value="${demandeur.telephone}">
                <input type="hidden" name="email" value="${demandeur.email}">
                <input type="hidden" name="adresse" value="${demandeur.adresse}">
                <input type="hidden" name="id" value="${demandeur.id}">
                <input type="hidden" name="idTypeDemande" value="${idTypeDemande}">
                <input type="hidden" name="idSituationFamiliale" value="${idSituationFamiliale}">
                <input type="hidden" name="idNationalite" value="${idNationalite}">

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

                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/duplicata-transfert/recherche">
                        ← Retour
                    </a>
                    <button type="submit" class="btn btn-primary">
                        Valider et Approuver →
                    </button>
                </div>
            </form>

            <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--sand); color: var(--gris-text); font-size: 14px;">
                <strong>Important :</strong> La demande sera automatiquement approuvée avec le statut <strong>"Titre délivré"</strong> dès validation.
            </div>
        </section>
    </main>
</div>
</body>
</html>
