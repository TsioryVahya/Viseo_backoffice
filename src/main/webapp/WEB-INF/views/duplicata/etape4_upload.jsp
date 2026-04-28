<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Duplicata - Upload</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />

    <main class="main-content">
        <h1 class="page-title">Upload des pieces - Dossier n${demande.id}</h1>
        <p class="page-subtitle">Ajoutez les fichiers justificatifs pour chaque piece declaree presente.</p>

        <div class="stepper">
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Recherche</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Infos personnelles</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Passeport et Visa</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item done"><div><div class="stepper-node">✓</div><div class="stepper-label">Pieces</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item active"><div><div class="stepper-node">5</div><div class="stepper-label">Upload</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">6</div><div class="stepper-label">Nouveau titre</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">7</div><div class="stepper-label">Ancienne carte</div></div><div class="stepper-track"></div></div>
            <div class="stepper-item future"><div><div class="stepper-node">8</div><div class="stepper-label">Resume</div></div></div>
        </div>

        <c:if test="${not empty succes}">
            <div class="alert-succes">${succes}</div>
        </c:if>
        <c:if test="${not empty erreur}">
            <div class="alert-erreur">${erreur}</div>
        </c:if>

        <section class="card">
            <h2 style="margin-top: 0;">Pieces communes</h2>
            <div class="table-wrap">
                <table class="scan-table">
                    <thead>
                    <tr>
                        <th>Piece</th>
                        <th>Obligatoire</th>
                        <th>Statut</th>
                        <th>Dernier fichier</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${piecesCommunes}" var="piece">
                        <tr>
                            <td>${piece.typePieceCommune.libelle}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${piece.typePieceCommune.obligatoire}"><span class="badge badge-orange">Oui</span></c:when>
                                    <c:otherwise><span class="badge badge-neutre">Non</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${piece.uploaded}"><span class="badge badge-succes">Uploadee</span></c:when>
                                    <c:otherwise><span class="badge badge-danger">En attente</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>${not empty derniersUploadsCommunes[piece.id] ? derniersUploadsCommunes[piece.id].nomFichierOriginal : '-'}</td>
                            <td>${not empty derniersUploadsCommunes[piece.id] ? derniersUploadsCommunes[piece.id].dateUpload : '-'}</td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/duplicata/etape4/piece" enctype="multipart/form-data" class="upload-inline-form">
                                    <input type="hidden" name="idPiece" value="${piece.id}">
                                    <input type="hidden" name="typePiece" value="commune">
                                    <input type="file" name="fichier" accept=".jpg,.jpeg,.png,.pdf" required>
                                    <input type="date" name="dateUpload" value="${today}" required>
                                    <button type="submit" class="btn btn-primary btn-sm">Soumettre</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

        <section class="card">
            <h2 style="margin-top: 0;">Pieces specifiques</h2>
            <div class="table-wrap">
                <table class="scan-table">
                    <thead>
                    <tr>
                        <th>Piece</th>
                        <th>Obligatoire</th>
                        <th>Statut</th>
                        <th>Dernier fichier</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${piecesSpecifiques}" var="piece">
                        <tr>
                            <td>${piece.typePiece.libelle}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${piece.typePiece.obligatoire}"><span class="badge badge-orange">Oui</span></c:when>
                                    <c:otherwise><span class="badge badge-neutre">Non</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${piece.uploaded}"><span class="badge badge-succes">Uploadee</span></c:when>
                                    <c:otherwise><span class="badge badge-danger">En attente</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>${not empty derniersUploadsSpecifiques[piece.id] ? derniersUploadsSpecifiques[piece.id].nomFichierOriginal : '-'}</td>
                            <td>${not empty derniersUploadsSpecifiques[piece.id] ? derniersUploadsSpecifiques[piece.id].dateUpload : '-'}</td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/duplicata/etape4/piece" enctype="multipart/form-data" class="upload-inline-form">
                                    <input type="hidden" name="idPiece" value="${piece.id}">
                                    <input type="hidden" name="typePiece" value="specifique">
                                    <input type="file" name="fichier" accept=".jpg,.jpeg,.png,.pdf" required>
                                    <input type="date" name="dateUpload" value="${today}" required>
                                    <button type="submit" class="btn btn-primary btn-sm">Soumettre</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="actions" style="justify-content: flex-end; margin-top: 1.2rem;">
                <c:choose>
                    <c:when test="${peutContinuer}">
                        <a href="${pageContext.request.contextPath}/duplicata/etape5" class="btn btn-primary">Continuer -&gt; Informations du titre</a>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-secondary" disabled>Continuer -&gt;</button>
                        <p class="texte-erreur" style="margin: 0; padding: 0.4rem 0.6rem; border-radius: 8px;">Uploadez toutes les pieces obligatoires pour continuer.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </main>
</div>
</body>
</html>
