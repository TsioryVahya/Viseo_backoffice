<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Recherche - Duplicata / Transfert</title>
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
        <h1 class="page-title">Demande de Duplicata / Transfert</h1>
        <p class="page-subtitle">Recherchez un demandeur existant ou créez un nouveau dossier.</p>

        <section class="card">
            <h2 style="margin-top: 0;">Recherche de Demandeur</h2>
            <p style="color: var(--gris-text); margin-bottom: 1.5rem;">
                Entrez le nom et le prénom du demandeur pour vérifier s'il existe déjà. 
                Si le demandeur n'existe pas, vous pourrez créer un nouveau dossier.
            </p>

            <form method="post" action="${pageContext.request.contextPath}/demande/duplicata-transfert/verifier">
                
                <!-- Type de demande -->
                <div class="form-row">
                    <label for="idTypeDemande">Type de demande *</label>
                    <select id="idTypeDemande" name="idTypeDemande" required>
                        <option value="">-- Sélectionner un type --</option>
                        <c:forEach var="type" items="${typesDemande}">
                            <option value="${type.id}">${type.libelle}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Recherche par nom et prénom -->
                <div class="form-grid-2">
                    <div class="form-row">
                        <label for="nom">Nom du demandeur *</label>
                        <input type="text" id="nom" name="nom" placeholder="Ex: Dupont" required>
                    </div>

                    <div class="form-row">
                        <label for="prenom">Prénom du demandeur *</label>
                        <input type="text" id="prenom" name="prenom" placeholder="Ex: Jean" required>
                    </div>
                </div>

                <!-- Actions -->
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/">
                        ← Accueil
                    </a>
                    <button type="submit" class="btn btn-primary">
                        Rechercher →
                    </button>
                </div>
            </form>

            <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--sand); color: var(--gris-text); font-size: 14px;">
                <strong>Comment ça marche :</strong>
                <ul style="margin: 0.5rem 0; padding-left: 1.5rem;">
                    <li>Sélectionnez le type de demande (Duplicata ou Transfert)</li>
                    <li>Entrez le nom et prénom du demandeur</li>
                    <li>Cliquez sur "Rechercher" pour vérifier son existence</li>
                    <li>Si trouvé, vous pouvez valider la demande directement</li>
                    <li>Si non trouvé, créez un nouveau dossier en complétant le formulaire</li>
                </ul>
            </div>
        </section>
    </main>
</div>
</body>
</html>
