<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Scan des pieces - Dossier n${demande.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />
    <main class="main-content">
        <c:if test="${not empty succes}">
            <div class="alert-succes">${succes}</div>
        </c:if>
        <c:if test="${not empty erreur}">
            <div class="alert-erreur">${erreur}</div>
        </c:if>

        <h1 class="page-title">Scan des pieces - Dossier n${demande.id}</h1>
        <p class="page-subtitle">
            ${demande.demandeur.nom} ${demande.demandeur.prenom} - ${demande.typeVisa.libelle}
        </p>
        <p>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/scan/liste"><- Retour a la liste</a>
        </p>

        <section class="card" style="margin-top: 1rem;">
            <h2 style="margin-top: 0;">Pieces communes</h2>
            <div class="table-wrap">
                <table class="scan-table">
                    <thead>
                    <tr>
                        <th>Piece</th>
                        <th>Obligatoire</th>
                        <th>Statut</th>
                        <th>Dernier fichier</th>
                        <th>Date upload</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="piece" items="${piecesCommunes}">
                        <tr>
                            <td>${piece.typePieceCommune.libelle}</td>
                            <td>
                                <span class="badge ${piece.typePieceCommune.obligatoire ? 'badge-neutre' : 'badge-neutre'}">
                                    ${piece.typePieceCommune.obligatoire ? 'Oui' : 'Non'}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${piece.uploaded}">
                                        <span class="badge badge-succes">Uploade</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">En attente</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty derniersUploadsCommunes[piece.id]}">
                                        ${derniersUploadsCommunes[piece.id].nomFichierOriginal}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty derniersUploadsCommunes[piece.id]}">
                                        ${derniersUploadsCommunes[piece.id].dateUpload}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/scan/upload/${demande.id}/piece"
                                      enctype="multipart/form-data"
                                      class="upload-inline-form">
                                    <input type="hidden" name="idPiece" value="${piece.id}" />
                                    <input type="hidden" name="typePiece" value="commune" />
                                    <input type="file" name="fichier" accept=".jpg,.jpeg,.png,.pdf" required />
                                    <input type="date" name="dateUpload" value="${today}" required />
                                    <button type="submit" class="btn btn-primary btn-sm">Soumettre</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

        <section class="card" style="margin-top: 1rem;">
            <h2 style="margin-top: 0;">Pieces specifiques</h2>
            <div class="table-wrap">
                <table class="scan-table">
                    <thead>
                    <tr>
                        <th>Piece</th>
                        <th>Obligatoire</th>
                        <th>Statut</th>
                        <th>Dernier fichier</th>
                        <th>Date upload</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="piece" items="${piecesSpecifiques}">
                        <tr>
                            <td>${piece.typePiece.libelle}</td>
                            <td>
                                <span class="badge ${piece.typePiece.obligatoire ? 'badge-neutre' : 'badge-neutre'}">
                                    ${piece.typePiece.obligatoire ? 'Oui' : 'Non'}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${piece.uploaded}">
                                        <span class="badge badge-succes">Uploade</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">En attente</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty derniersUploadsSpecifiques[piece.id]}">
                                        ${derniersUploadsSpecifiques[piece.id].nomFichierOriginal}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty derniersUploadsSpecifiques[piece.id]}">
                                        ${derniersUploadsSpecifiques[piece.id].dateUpload}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/scan/upload/${demande.id}/piece"
                                      enctype="multipart/form-data"
                                      class="upload-inline-form">
                                    <input type="hidden" name="idPiece" value="${piece.id}" />
                                    <input type="hidden" name="typePiece" value="specifique" />
                                    <input type="file" name="fichier" accept=".jpg,.jpeg,.png,.pdf" required />
                                    <input type="date" name="dateUpload" value="${today}" required />
                                    <button type="submit" class="btn btn-primary btn-sm">Soumettre</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

        <section class="card" style="margin-top: 1rem;">
            <form method="post" action="${pageContext.request.contextPath}/scan/terminer/${demande.id}">
                <c:choose>
                    <c:when test="${peutTerminer}">
                        <button class="btn btn-primary" type="submit"
                                onclick="return confirm('Confirmer la fin du scan ? Cette action est irreversible.');">
                            Scan termine
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-secondary" type="button" disabled>Scan termine</button>
                        <p class="texte-erreur">Des pieces obligatoires ne sont pas encore uploadees.</p>
                    </c:otherwise>
                </c:choose>
            </form>
        </section>
    </main>
</div>
</body>
</html>
