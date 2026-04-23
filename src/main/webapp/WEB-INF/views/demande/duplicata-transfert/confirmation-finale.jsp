<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmation Finale - Demande Approuvée</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .success-banner {
            background: linear-gradient(135deg, var(--green), #2d4535);
            color: var(--blanc);
            padding: 2rem;
            border-radius: 12px;
            text-align: center;
            margin-bottom: 2rem;
        }

        .success-icon {
            font-size: 48px;
            margin-bottom: 1rem;
        }

        .success-banner h2 {
            margin: 0 0 0.5rem;
            font-size: 28px;
        }

        .success-banner p {
            margin: 0;
            opacity: 0.95;
        }
    </style>
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <c:if test="${not empty message}">
            <div class="success-banner">
                <div class="success-icon">✓</div>
                <h2>Succès !</h2>
                <p>${message}</p>
            </div>
        </c:if>

        <section class="card">
            <h1 style="margin-top: 0; color: var(--green); text-align: center;">Demande Enregistrée et Approuvée</h1>
            
            <c:if test="${not empty demande}">
                <div style="background: var(--sand-light); border: 1px solid var(--sand); border-radius: 8px; padding: 1.5rem; margin-bottom: 1.5rem;">
                    <h3 style="margin-top: 0; color: var(--green);">Résumé de la Demande</h3>
                    
                    <div class="form-grid-2">
                        <div>
                            <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">ID Demande</div>
                            <div style="font-size: 16px; font-weight: 500;">#${demande.id}</div>
                        </div>
                        <div>
                            <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Type de Demande</div>
                            <div style="font-size: 16px; font-weight: 500;">${demande.typeDemande.libelle}</div>
                        </div>
                    </div>

                    <div class="form-grid-2" style="margin-top: 1rem;">
                        <div>
                            <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Demandeur</div>
                            <div style="font-size: 14px;">${demande.demandeur.prenom} ${demande.demandeur.nom}</div>
                        </div>
                        <div>
                            <div style="font-size: 12px; color: var(--gris-text); margin-bottom: 0.25rem;">Date de Demande</div>
                            <div style="font-size: 14px;">${demande.dateDemande}</div>
                        </div>
                    </div>

                    <div style="margin-top: 1rem; padding: 1rem; background: #d4edda; border-left: 4px solid #28a745; border-radius: 4px;">
                        <div style="color: #155724; font-weight: 500;">✓ Statut : Titre Délivré</div>
                        <div style="color: #155724; font-size: 12px; margin-top: 0.5rem;">La demande a été automatiquement approuvée.</div>
                    </div>
                </div>
            </c:if>

            <div style="background: var(--sand-light); border: 1px solid var(--sand); border-radius: 8px; padding: 1.5rem; margin-bottom: 1.5rem;">
                <h3 style="margin-top: 0;">Les Prochaines Étapes</h3>
                <ol style="margin: 0; padding-left: 1.5rem; color: var(--gris-text);">
                    <li style="margin-bottom: 0.8rem;">
                        <strong>Traitement</strong> - La demande est en cours de traitement administratif
                    </li>
                    <li style="margin-bottom: 0.8rem;">
                        <strong>Vérification</strong> - Les documents justificatifs seront vérifiés
                    </li>
                    <li style="margin-bottom: 0.8rem;">
                        <strong>Finalisation</strong> - Le titre sera remis au demandeur
                    </li>
                </ol>
            </div>

            <div class="actions">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/demande/duplicata-transfert/recherche">
                    Nouvelle Demande
                </a>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/">
                    Accueil
                </a>
            </div>

            <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--sand); color: var(--gris-text); font-size: 14px; text-align: center;">
                <strong>Merci d'avoir utilisé Viseo Backoffice</strong><br>
                Pour toute question, contactez l'équipe d'administration.
            </div>
        </section>
    </main>
</div>
</body>
</html>
