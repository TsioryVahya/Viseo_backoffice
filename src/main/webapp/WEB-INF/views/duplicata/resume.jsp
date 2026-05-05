<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Resume du dossier</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">

        <%-- Titre dynamique selon le type --%>
        <h1 class="page-title">
            <c:choose>
                <c:when test="${typeDemandeLibelle eq 'Transfert de visa'}">
                    Resume du dossier — Transfert de visa
                </c:when>
                <c:otherwise>
                    Resume du dossier — Duplicata
                </c:otherwise>
            </c:choose>
        </h1>
        <p class="page-subtitle">
            Verifiez toutes les informations avant confirmation finale.
        </p>

        <%-- Stepper --%>
        <div class="stepper">
            <div class="stepper-item done">
                <div><div class="stepper-node">✓</div>
                <div class="stepper-label">Recherche</div></div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item done">
                <div><div class="stepper-node">✓</div>
                <div class="stepper-label">Infos personnelles</div></div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item done">
                <div><div class="stepper-node">✓</div>
                <div class="stepper-label">Passeport et Visa</div></div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item done">
                <div><div class="stepper-node">✓</div>
                <div class="stepper-label">Pieces</div></div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item done">
                <div><div class="stepper-node">✓</div>
                <div class="stepper-label">Upload</div></div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item done">
                <div><div class="stepper-node">✓</div>
                <div class="stepper-label">
                    <c:choose>
                        <c:when test="${typeDemandeLibelle eq 'Transfert de visa'}">
                            Ancien visa
                        </c:when>
                        <c:otherwise>
                            Nouveau titre
                        </c:otherwise>
                    </c:choose>
                </div></div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item done">
                <div><div class="stepper-node">✓</div>
                <div class="stepper-label">
                    <c:choose>
                        <c:when test="${typeDemandeLibelle eq 'Transfert de visa'}">
                            Nouveau passeport
                        </c:when>
                        <c:otherwise>
                            Ancienne carte
                        </c:otherwise>
                    </c:choose>
                </div></div>
                <div class="stepper-track"></div>
            </div>
            <div class="stepper-item active">
                <div><div class="stepper-node">8</div>
                <div class="stepper-label">Resume</div></div>
            </div>
        </div>

        <c:if test="${not empty erreur}">
            <div class="alert-erreur">${erreur}</div>
        </c:if>

        <%-- Section infos personnelles — commune aux deux --%>
        <section class="card">
            <h3 style="margin-top: 0;">Informations personnelles</h3>
            <div class="resume-grid">
                <div class="resume-label">Nom</div>
                <div class="resume-value">${demande.demandeur.nom}</div>
                <div class="resume-label">Prenom</div>
                <div class="resume-value">${demande.demandeur.prenom}</div>
                <div class="resume-label">Date naissance</div>
                <div class="resume-value">
                    ${demande.demandeur.dateNaissance}
                </div>
                <div class="resume-label">Lieu naissance</div>
                <div class="resume-value">
                    ${demande.demandeur.lieuNaissance}
                </div>
                <div class="resume-label">Telephone</div>
                <div class="resume-value">
                    ${demande.demandeur.telephone}
                </div>
                <div class="resume-label">Email</div>
                <div class="resume-value">${demande.demandeur.email}</div>
                <div class="resume-label">Adresse</div>
                <div class="resume-value">${demande.demandeur.adresse}</div>
                <div class="resume-label">Nationalite</div>
                <div class="resume-value">
                    ${demande.demandeur.nationalite.libelle}
                </div>
                <div class="resume-label">Situation familiale</div>
                <div class="resume-value">
                    ${demande.demandeur.situationFamiliale.libelle}
                </div>
            </div>
        </section>

        <%-- Section passeport — commune aux deux --%>
        <section class="card">
            <h3 style="margin-top: 0;">Passeport et Visa transformable</h3>
            <div class="resume-grid">
                <div class="resume-label">Numero passeport</div>
                <div class="resume-value">
                    ${demande.visaTransformable.passeport.numeroPasseport}
                </div>
                <div class="resume-label">Date delivrance</div>
                <div class="resume-value">
                    ${demande.visaTransformable.passeport.dateDelivrance}
                </div>
                <div class="resume-label">Date expiration</div>
                <div class="resume-value">
                    ${demande.visaTransformable.passeport.dateExpiration}
                </div>
                <div class="resume-label">Pays delivrance</div>
                <div class="resume-value">
                    ${demande.visaTransformable.passeport.paysDelivrance}
                </div>
                <div class="resume-label">Reference visa transformable</div>
                <div class="resume-value">
                    ${demande.visaTransformable.numeroReference}
                </div>
                <div class="resume-label">Date expiration visa</div>
                <div class="resume-value">
                    ${demande.visaTransformable.dateExpiration}
                </div>
            </div>
        </section>

        <%-- Section type demande — commune aux deux --%>
        <section class="card">
            <h3 style="margin-top: 0;">Type de demande</h3>
            <div class="resume-grid">
                <div class="resume-label">Type de demande</div>
                <div class="resume-value">${typeDemandeLibelle}</div>
                <div class="resume-label">Type de visa</div>
                <div class="resume-value">${demande.typeVisa.libelle}</div>
            </div>
        </section>

        <%-- Section pieces — commune aux deux --%>
        <section class="card">
            <h3 style="margin-top: 0;">Pieces justificatives</h3>
            <h4>Communes</h4>
            <ul style="margin: 0 0 1rem 0; padding-left: 1.1rem;">
                <c:forEach var="piece" items="${piecesCommunes}">
                    <li>
                        <c:choose>
                            <c:when test="${not empty uploadsCommunes[piece.id]}">
                                <span class="status-ok">✓</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-ko">✗</span>
                            </c:otherwise>
                        </c:choose>
                        ${piece.typePieceCommune.libelle}
                        <c:if test="${not empty uploadsCommunes[piece.id]}">
                            — ${uploadsCommunes[piece.id].nomFichierOriginal}
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
            <h4>Specifiques</h4>
            <ul style="margin: 0; padding-left: 1.1rem;">
                <c:forEach var="piece" items="${piecesSpecifiques}">
                    <li>
                        <c:choose>
                            <c:when test="${not empty uploadsSpecifiques[piece.id]}">
                                <span class="status-ok">✓</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-ko">✗</span>
                            </c:otherwise>
                        </c:choose>
                        ${piece.typePiece.libelle}
                        <c:if test="${not empty uploadsSpecifiques[piece.id]}">
                            — ${uploadsSpecifiques[piece.id].nomFichierOriginal}
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </section>

        <%-- Section spécifique DUPLICATA --%>
        <c:if test="${typeDemandeLibelle eq 'Duplicata'}">

            <fmt:parseDate value="${dateDebutNouveauTitre}"
                pattern="yyyy-MM-dd" var="dateDebutNouveauTitreFmt"/>
            <fmt:parseDate value="${dateFinNouveauTitre}"
                pattern="yyyy-MM-dd" var="dateFinNouveauTitreFmt"/>
            <fmt:parseDate value="${dateDebutAncienneCarte}"
                pattern="yyyy-MM-dd" var="dateDebutAncienneCarteFmt"/>
            <fmt:parseDate value="${dateFinAncienneCarte}"
                pattern="yyyy-MM-dd" var="dateFinAncienneCarteFmt"/>

            <section class="card">
                <h3 style="margin-top: 0;">Nouveau titre a delivrer</h3>
                <div class="resume-grid">
                    <div class="resume-label">Reference</div>
                    <div class="resume-value">${referenceNouveauTitre}</div>
                    <div class="resume-label">Date debut</div>
                    <div class="resume-value">
                        <fmt:formatDate value="${dateDebutNouveauTitreFmt}"
                            pattern="dd/MM/yyyy"/>
                    </div>
                    <div class="resume-label">Date fin</div>
                    <div class="resume-value">
                        <fmt:formatDate value="${dateFinNouveauTitreFmt}"
                            pattern="dd/MM/yyyy"/>
                    </div>
                </div>
            </section>

            <section class="card">
                <h3 style="margin-top: 0;">Ancienne carte declaree perdue</h3>
                <div class="resume-grid">
                    <div class="resume-label">Reference</div>
                    <div class="resume-value">${referenceAncienneCarte}</div>
                    <div class="resume-label">Date debut</div>
                    <div class="resume-value">
                        <fmt:formatDate value="${dateDebutAncienneCarteFmt}"
                            pattern="dd/MM/yyyy"/>
                    </div>
                    <div class="resume-label">Date fin</div>
                    <div class="resume-value">
                        <fmt:formatDate value="${dateFinAncienneCarteFmt}"
                            pattern="dd/MM/yyyy"/>
                    </div>
                    <div class="resume-label">Statut</div>
                    <div class="resume-value">
                        <span class="badge badge-danger">Perdu</span>
                    </div>
                </div>
            </section>

        </c:if>

        <%-- Section spécifique TRANSFERT --%>
        <c:if test="${typeDemandeLibelle eq 'Transfert de visa'}">

            <fmt:parseDate value="${dateDebutAncienVisa}"
                pattern="yyyy-MM-dd" var="dateDebutAncienVisaFmt"/>
            <fmt:parseDate value="${dateFinAncienVisa}"
                pattern="yyyy-MM-dd" var="dateFinAncienVisaFmt"/>
            <fmt:parseDate value="${nouvelleDateDelivrance}"
                pattern="yyyy-MM-dd" var="nouvelleDateDelivranceFmt"/>
            <fmt:parseDate value="${nouvelleDateExpiration}"
                pattern="yyyy-MM-dd" var="nouvelleDateExpirationFmt"/>

            <section class="card">
                <h3 style="margin-top: 0;">
                    Ancien visa — Passeport expire
                </h3>
                <div class="resume-grid">
                    <div class="resume-label">Reference</div>
                    <div class="resume-value">${referenceAncienVisa}</div>
                    <div class="resume-label">Date debut</div>
                    <div class="resume-value">
                        <fmt:formatDate value="${dateDebutAncienVisaFmt}"
                            pattern="dd/MM/yyyy"/>
                    </div>
                    <div class="resume-label">Date fin</div>
                    <div class="resume-value">
                        <fmt:formatDate value="${dateFinAncienVisaFmt}"
                            pattern="dd/MM/yyyy"/>
                    </div>
                    <div class="resume-label">Statut</div>
                    <div class="resume-value">
                        <span class="badge badge-warning">
                            Passeport expire
                        </span>
                    </div>
                </div>
            </section>

            <section class="card">
                <h3 style="margin-top: 0;">Nouveau passeport</h3>
                <div class="resume-grid">
                    <div class="resume-label">Numero passeport</div>
                    <div class="resume-value">${nouveauNumeroPasseport}</div>
                    <div class="resume-label">Date delivrance</div>
                    <div class="resume-value">
                        <fmt:formatDate value="${nouvelleDateDelivranceFmt}"
                            pattern="dd/MM/yyyy"/>
                    </div>
                    <div class="resume-label">Date expiration</div>
                    <div class="resume-value">
                        <fmt:formatDate value="${nouvelleDateExpirationFmt}"
                            pattern="dd/MM/yyyy"/>
                    </div>
                    <div class="resume-label">Pays delivrance</div>
                    <div class="resume-value">${nouveauPaysDelivrance}</div>
                </div>
            </section>

        </c:if>

        <%-- Boutons de navigation --%>
        <form method="post"
              action="${pageContext.request.contextPath}/duplicata/confirmer">
            <div class="actions">
                <a class="btn btn-secondary"
                   href="${pageContext.request.contextPath}/duplicata/etape6">
                    ← Modifier
                </a>
                <button type="submit" class="btn btn-primary">
                    Confirmer et soumettre
                </button>
            </div>
        </form>

    </main>
</div>
</body>
</html>