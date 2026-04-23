-- =============================================
-- VUE 1 : Statut actuel de chaque demande
-- La plus importante — utilisée partout
-- =============================================
CREATE VIEW vue_statut_actuel_demande AS
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
CREATE VIEW vue_historique_statuts AS
SELECT
    sd.id_demande,
    sdt.libelle AS statut,
    sd.date_changement,
    ROW_NUMBER() OVER (
        PARTITION BY sd.id_demande
        ORDER BY sd.date_changement ASC
    ) AS numero_etape
FROM Statut_demande sd
JOIN Statut_demande_type sdt ON sdt.id = sd.id_statut_type
ORDER BY sd.id_demande, sd.date_changement ASC;

-- USAGE :
-- SELECT * FROM vue_historique_statuts WHERE id_demande = 1;


-- =============================================
-- VUE 3 : Liste complète des demandes avec infos
-- Pour la liste principale et le scan
-- =============================================
CREATE VIEW vue_demandes_completes AS
SELECT
    d.id AS id_demande,
    d.date_demande,
    dem.id AS id_demandeur,
    dem.nom,
    dem.prenom,
    dem.telephone,
    dem.email,
    tv.libelle AS type_visa,
    td.libelle AS type_demande,
    vsa.statut_actuel,
    vsa.date_changement AS date_dernier_statut
FROM Demande d
JOIN Demandeur dem ON dem.id = d.id_demandeur
JOIN type_visa tv ON tv.id = d.id_type_visa
JOIN Type_demande td ON td.id = d.id_type_demande
JOIN vue_statut_actuel_demande vsa ON vsa.id_demande = d.id
ORDER BY d.date_demande DESC;

-- USAGE :
-- SELECT * FROM vue_demandes_completes;
-- SELECT * FROM vue_demandes_completes WHERE statut_actuel = 'Dossier créé';
-- SELECT * FROM vue_demandes_completes WHERE statut_actuel IN ('Dossier créé', 'Pièces manquantes');
-- SELECT * FROM vue_demandes_completes WHERE nom ILIKE '%dupont%';


-- =============================================
-- VUE 4 : Etat des pièces communes par demande
-- Pour vérifier ce qui est uploadé ou non
-- =============================================
CREATE VIEW vue_pieces_communes_etat AS
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
CREATE VIEW vue_pieces_specifiques_etat AS
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
CREATE VIEW vue_resume_upload_demande AS
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
CREATE VIEW vue_demandes_a_scanner AS
SELECT *
FROM vue_demandes_completes
WHERE statut_actuel IN ('Dossier créé', 'Pièces manquantes')
ORDER BY date_dernier_statut DESC;

-- USAGE :
-- SELECT * FROM vue_demandes_a_scanner;
-- SELECT * FROM vue_demandes_a_scanner WHERE type_visa = 'Investisseur';