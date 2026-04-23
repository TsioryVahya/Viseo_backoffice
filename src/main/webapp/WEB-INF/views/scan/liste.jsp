<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des dossiers - Scan des pieces</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/layout/navbar.jsp" />
    <main class="main-content">
        <h1 class="page-title">Gestion des dossiers - Scan des pieces</h1>
        <p class="page-subtitle">Suivi des dossiers en attente de traitement de scan.</p>

        <div class="scan-filtres">
            <a class="btn btn-secondary ${empty statutFiltreActuel ? 'active' : ''}"
               href="${pageContext.request.contextPath}/scan/liste">Tous</a>
            <a class="btn btn-secondary ${statutFiltreActuel == 1 ? 'active' : ''}"
               href="${pageContext.request.contextPath}/scan/liste?statutId=1">Dossier cree</a>
            <a class="btn btn-secondary ${statutFiltreActuel == 2 ? 'active' : ''}"
               href="${pageContext.request.contextPath}/scan/liste?statutId=2">Pieces manquantes</a>
        </div>

        <section class="card" style="margin-top: 1rem;">
            <div class="table-wrap">
                <table class="scan-table">
                    <thead>
                    <tr>
                        <th>N dossier</th>
                        <th>Nom demandeur</th>
                        <th>Type visa</th>
                        <th>Date demande</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty pageDemandes.content}">
                            <c:forEach var="demande" items="${pageDemandes.content}">
                                <tr>
                                    <td>${demande.id}</td>
                                    <td>${demande.demandeur.nom} ${demande.demandeur.prenom}</td>
                                    <td>${demande.typeVisa.libelle}</td>
                                    <td>${demande.dateDemande}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${libellesDernierStatut[demande.id] == 'Pièces manquantes' || libellesDernierStatut[demande.id] == 'Pieces manquantes'}">
                                                <span class="badge badge-orange">${libellesDernierStatut[demande.id]}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-neutre">${empty libellesDernierStatut[demande.id] ? 'Dossier cree' : libellesDernierStatut[demande.id]}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a class="btn btn-primary btn-sm"
                                           href="${pageContext.request.contextPath}/scan/upload/${demande.id}">Modifier -></a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6">Aucun dossier a traiter.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <div class="scan-pagination">
                <div>
                    Page ${pageDemandes.number + 1} sur ${pageDemandes.totalPages == 0 ? 1 : pageDemandes.totalPages}
                    - ${pageDemandes.totalElements} resultats
                </div>
                <div class="scan-pagination-links">
                    <c:if test="${pageDemandes.number > 0}">
                        <c:url var="prevUrl" value="/scan/liste">
                            <c:param name="page" value="${pageDemandes.number - 1}" />
                            <c:param name="size" value="${pageDemandes.size}" />
                            <c:if test="${statutFiltreActuel != null}">
                                <c:param name="statutId" value="${statutFiltreActuel}" />
                            </c:if>
                        </c:url>
                        <a class="btn btn-secondary btn-sm"
                           href="${pageContext.request.contextPath}${prevUrl}">Precedent</a>
                    </c:if>

                    <c:forEach begin="0" end="${pageDemandes.totalPages > 0 ? pageDemandes.totalPages - 1 : 0}" var="i">
                        <c:url var="pageUrl" value="/scan/liste">
                            <c:param name="page" value="${i}" />
                            <c:param name="size" value="${pageDemandes.size}" />
                            <c:if test="${statutFiltreActuel != null}">
                                <c:param name="statutId" value="${statutFiltreActuel}" />
                            </c:if>
                        </c:url>
                        <a class="btn btn-secondary btn-sm ${i == pageDemandes.number ? 'active' : ''}"
                           href="${pageContext.request.contextPath}${pageUrl}">${i + 1}</a>
                    </c:forEach>

                    <c:if test="${pageDemandes.number + 1 < pageDemandes.totalPages}">
                        <c:url var="nextUrl" value="/scan/liste">
                            <c:param name="page" value="${pageDemandes.number + 1}" />
                            <c:param name="size" value="${pageDemandes.size}" />
                            <c:if test="${statutFiltreActuel != null}">
                                <c:param name="statutId" value="${statutFiltreActuel}" />
                            </c:if>
                        </c:url>
                        <a class="btn btn-secondary btn-sm"
                           href="${pageContext.request.contextPath}${nextUrl}">Suivant</a>
                    </c:if>
                </div>
            </div>
        </section>
    </main>
</div>
</body>
</html>
