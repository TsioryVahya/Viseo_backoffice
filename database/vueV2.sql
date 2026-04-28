-- =============================================
-- VUE 1 : Statut actuel de chaque demande
-- La plus importante — utilisée partout
-- =============================================
CREATE OR REPLACE VIEW vue_statut_actuel_demande AS
SELECT DISTINCT ON (sd.id_demande)
    sd.id_demande,
    sd.id_statut_type,
    sdt.libelle AS statut_actuel,
    sd.date_changement
FROM Statut_demande sd
JOIN Statut_demande_type sdt ON sdt.id = sd.id_statut_type
ORDER BY sd.id_demande, sd.date_changement DESC;

-- USAGE :
-- SELECT * FROM vue_statut_actuel_demande WHERE id_demande = 1;
-- SELECT * FROM vue_statut_actuel_demande WHERE statut_actuel = 'Dossier créé';


-- =============================================
-- VUE 2 : Historique complet des statuts
-- Pour voir toutes les transitions d'une demande
-- =============================================
CREATE OR REPLACE VIEW vue_historique_statuts AS
SELECT
    sd.id                           AS id_statut,
    sd.id_demande,
    d.date_demande,
    dem.id                          AS id_demandeur,
    dem.nom,
    dem.prenom,
    td.libelle                      AS type_demande,
    tv.libelle                      AS type_visa,
    sdt.libelle                     AS statut,
    sd.date_changement,
    ROW_NUMBER() OVER (
        PARTITION BY sd.id_demande
        ORDER BY sd.date_changement ASC
    )                               AS numero_changement,
    EXTRACT(DAY FROM (
        LEAD(sd.date_changement)
            OVER (
                PARTITION BY sd.id_demande
                ORDER BY sd.date_changement ASC
            )
        - sd.date_changement
    ))                              AS duree_statut_jours
FROM Statut_demande sd
JOIN Statut_demande_type sdt
    ON sdt.id = sd.id_statut_type
JOIN Demande d
    ON d.id = sd.id_demande
JOIN Demandeur dem
    ON dem.id = d.id_demandeur
JOIN Type_demande td
    ON td.id = d.id_type_demande
JOIN type_visa tv
    ON tv.id = d.id_type_visa
ORDER BY sd.id_demande ASC, sd.date_changement ASC;

-- USAGE :
-- SELECT * FROM vue_historique_statuts WHERE id_demande = 1;


-- =============================================
-- VUE 3 : Liste complète et détails des demandes
-- Pour la liste principale, le scan et les détails
-- =============================================
CREATE OR REPLACE VIEW vue_demandes_completes AS
SELECT
    d.id                            AS id_demande,
    d.date_demande,
    dem.id                          AS id_demandeur,
    dem.nom,
    dem.prenom,
    dem.telephone,
    dem.email,
    tv.libelle                      AS type_visa,
    td.libelle                      AS type_demande,
    vsa.statut_actuel,
    vsa.date_changement             AS date_dernier_statut,
    p.id                            AS id_passeport,
    p.numero_passeport,
    p.date_delivrance               AS passeport_date_delivrance,
    p.date_expiration               AS passeport_date_expiration,
    p.pays_delivrance,
    vt.id                           AS id_visa_transformable,
    vt.numero_reference             AS visa_transformable_reference,
    vt.date_expiration              AS visa_transformable_date_expiration,
    dl.id_demande_origine,
    dl.type_lien,
    d_origine.date_demande          AS date_demande_origine,
    dem_origine.nom                 AS nom_demandeur_origine,
    dem_origine.prenom              AS prenom_demandeur_origine,
    td_origine.libelle              AS type_demande_origine
FROM Demande d
JOIN Demandeur dem ON dem.id = d.id_demandeur
JOIN type_visa tv ON tv.id = d.id_type_visa
JOIN Type_demande td ON td.id = d.id_type_demande
JOIN vue_statut_actuel_demande vsa ON vsa.id_demande = d.id
LEFT JOIN Visa_transformable vt ON vt.id = d.id_visa_transformable
LEFT JOIN Passeport p ON p.id = vt.id_passeport
LEFT JOIN Demande_liee dl ON dl.id_demande_liee = d.id
LEFT JOIN Demande d_origine ON d_origine.id = dl.id_demande_origine
LEFT JOIN Demandeur dem_origine ON dem_origine.id = d_origine.id_demandeur
LEFT JOIN Type_demande td_origine ON td_origine.id = d_origine.id_type_demande
ORDER BY d.date_demande DESC;

-- USAGE :
-- SELECT * FROM vue_demandes_completes;
-- SELECT * FROM vue_demandes_completes WHERE statut_actuel = 'Dossier créé';
-- SELECT * FROM vue_demandes_completes WHERE nom ILIKE '%dupont%';


-- =============================================
-- VUE 4 : Etat des pièces communes par demande
-- Pour vérifier ce qui est uploadé ou non
-- =============================================
CREATE OR REPLACE VIEW vue_pieces_communes_etat AS
SELECT
    pd.id AS id_piece_demande,
    pd.id_demande,
    tpc.id AS id_type_piece,
    tpc.libelle AS libelle_piece,
    tpc.obligatoire,
    pd.presente,
    pd.uploaded,
    up.nom_fichier_original AS dernier_fichier,
    up.date_upload AS date_dernier_upload
FROM Piece_demande pd
JOIN Type_piece_commune tpc ON tpc.id = pd.id_type_piece_commune
LEFT JOIN LATERAL (
    SELECT nom_fichier_original, date_upload
    FROM Upload_piece
    WHERE id_piece_demande = pd.id
    ORDER BY date_upload DESC
    LIMIT 1
) up ON true
ORDER BY pd.id_demande, tpc.obligatoire DESC, tpc.id;

-- USAGE :
-- SELECT * FROM vue_pieces_communes_etat WHERE id_demande = 1;
-- SELECT * FROM vue_pieces_communes_etat WHERE id_demande = 1 AND uploaded = false;
-- SELECT * FROM vue_pieces_communes_etat WHERE id_demande = 1 AND obligatoire = true AND uploaded = false;


-- =============================================
-- VUE 5 : Etat des pièces spécifiques par demande
-- Même logique que la vue 4 pour les spécifiques
-- =============================================
CREATE OR REPLACE VIEW vue_pieces_specifiques_etat AS
SELECT
    pds.id AS id_piece_demande_specifique,
    pds.id_demande,
    tps.id AS id_type_piece,
    tps.libelle AS libelle_piece,
    tps.obligatoire,
    tv.libelle AS type_visa,
    pds.presente,
    pds.uploaded,
    up.nom_fichier_original AS dernier_fichier,
    up.date_upload AS date_dernier_upload
FROM Piece_demande_specifique pds
JOIN Type_piece_specifique tps ON tps.id = pds.id_type_piece
JOIN type_visa tv ON tv.id = tps.id_type_visa
LEFT JOIN LATERAL (
    SELECT nom_fichier_original, date_upload
    FROM Upload_piece
    WHERE id_piece_demande_specifique = pds.id
    ORDER BY date_upload DESC
    LIMIT 1
) up ON true
ORDER BY pds.id_demande, tps.obligatoire DESC, tps.id;

-- USAGE :
-- SELECT * FROM vue_pieces_specifiques_etat WHERE id_demande = 1;
-- SELECT * FROM vue_pieces_specifiques_etat WHERE id_demande = 1 AND uploaded = false;
-- SELECT * FROM vue_pieces_specifiques_etat WHERE id_demande = 1 AND obligatoire = true AND uploaded = false;


-- =============================================
-- VUE 6 : Résumé upload par demande
-- Pour voir d'un coup d'oeil l'avancement du scan
-- =============================================
CREATE OR REPLACE VIEW vue_resume_upload_demande AS
SELECT
    d.id AS id_demande,
    dem.nom,
    dem.prenom,
    tv.libelle AS type_visa,
    vsa.statut_actuel,

    -- Pièces communes
    COUNT(DISTINCT pd.id) AS total_pieces_communes,
    COUNT(DISTINCT CASE WHEN pd.uploaded = true THEN pd.id END)
        AS pieces_communes_uploadees,
    COUNT(DISTINCT CASE WHEN tpc.obligatoire = true AND pd.uploaded = false
        THEN pd.id END) AS pieces_communes_obligatoires_manquantes,

    -- Pièces spécifiques
    COUNT(DISTINCT pds.id) AS total_pieces_specifiques,
    COUNT(DISTINCT CASE WHEN pds.uploaded = true THEN pds.id END)
        AS pieces_specifiques_uploadees,
    COUNT(DISTINCT CASE WHEN tps.obligatoire = true AND pds.uploaded = false
        THEN pds.id END) AS pieces_specifiques_obligatoires_manquantes,

    -- Total global
    COUNT(DISTINCT pd.id) + COUNT(DISTINCT pds.id)
        AS total_pieces,
    COUNT(DISTINCT CASE WHEN pd.uploaded = true THEN pd.id END) +
    COUNT(DISTINCT CASE WHEN pds.uploaded = true THEN pds.id END)
        AS total_uploadees

FROM Demande d
JOIN Demandeur dem ON dem.id = d.id_demandeur
JOIN type_visa tv ON tv.id = d.id_type_visa
JOIN vue_statut_actuel_demande vsa ON vsa.id_demande = d.id
LEFT JOIN Piece_demande pd ON pd.id_demande = d.id
LEFT JOIN Type_piece_commune tpc ON tpc.id = pd.id_type_piece_commune
LEFT JOIN Piece_demande_specifique pds ON pds.id_demande = d.id
LEFT JOIN Type_piece_specifique tps ON tps.id = pds.id_type_piece
GROUP BY d.id, dem.nom, dem.prenom, tv.libelle, vsa.statut_actuel;

-- USAGE :
-- SELECT * FROM vue_resume_upload_demande;
-- SELECT * FROM vue_resume_upload_demande WHERE id_demande = 1;
-- Vérifier si scan peut être terminé :
-- SELECT * FROM vue_resume_upload_demande
--     WHERE id_demande = 1
--     AND pieces_communes_obligatoires_manquantes = 0
--     AND pieces_specifiques_obligatoires_manquantes = 0;


-- =============================================
-- VUE 7 : Demandes prêtes pour le scan
-- Filtre direct sur statut "Dossier créé" et "Pièces manquantes"
-- =============================================
CREATE OR REPLACE VIEW vue_demandes_a_scanner AS
SELECT *
FROM vue_demandes_completes
WHERE statut_actuel IN ('Dossier créé', 'Pièces manquantes')
ORDER BY date_dernier_statut DESC;

-- USAGE :
-- SELECT * FROM vue_demandes_a_scanner;
-- SELECT * FROM vue_demandes_a_scanner WHERE type_visa = 'Investisseur';

-- =============================================
-- VUE 8 : Statut actuel d'un titre (visa ou carte)
-- =============================================
CREATE OR REPLACE VIEW vue_statut_actuel_titre AS
-- Statut des visas
SELECT
    'visa' AS type_titre,
    sv.id_visa AS id_titre,
    sv.id_statut_type,
    stt.libelle AS statut_actuel,
    sv.date_changement,
    sv.commentaire
FROM Statut_visa sv
JOIN Statut_titre_type stt ON stt.id = sv.id_statut_type
WHERE (sv.id_visa, sv.date_changement) IN (
    SELECT id_visa, MAX(date_changement)
    FROM Statut_visa
    GROUP BY id_visa
)
UNION ALL
-- Statut des cartes de résident
SELECT
    'carte' AS type_titre,
    sc.id_carte_resident AS id_titre,
    sc.id_statut_type,
    stt.libelle AS statut_actuel,
    sc.date_changement,
    sc.commentaire
FROM Statut_carte_resident sc
JOIN Statut_titre_type stt ON stt.id = sc.id_statut_type
WHERE (sc.id_carte_resident, sc.date_changement) IN (
    SELECT id_carte_resident, MAX(date_changement)
    FROM Statut_carte_resident
    GROUP BY id_carte_resident
);

-- USAGE:
-- SELECT * FROM vue_statut_actuel_titre WHERE type_titre = 'visa' AND id_titre = 1;
-- SELECT * FROM vue_statut_actuel_titre WHERE statut_actuel = 'Actif';