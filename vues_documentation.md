# Documentation des Vues SQL

Ce document détaille les vues SQL utilisées dans le projet, leur objectif, et comment les utiliser.

---

### 1. `vue_statut_actuel_demande`

-   **Objectif :** Obtenir le statut le plus récent pour chaque demande. C'est une vue fondamentale utilisée par de nombreuses autres vues pour connaître l'état actuel d'un dossier.
-   **Colonnes clés :**
    -   `id_demande`: Identifiant de la demande.
    -   `statut_actuel`: Libellé du dernier statut (ex: 'Dossier créé', 'En attente de paiement').
    -   `date_changement`: Date de ce dernier statut.
-   **Usage typique :**
    ```sql
    -- Connaître le statut de la demande de visa avec l'ID 42
    SELECT statut_actuel FROM vue_statut_actuel_demande WHERE id_demande = 42;
    ```

---

### 2. `vue_historique_statuts`

-   **Objectif :** Tracer l'historique complet de tous les changements de statut pour une demande, du début à la fin. Utile pour l'audit et le suivi du parcours d'un dossier.
-   **Colonnes clés :**
    -   `id_demande`: Identifiant de la demande.
    -   `statut`: Libellé du statut à une étape donnée.
    -   `date_changement`: Date à laquelle le statut a été appliqué.
    -   `numero_changement`: Ordre chronologique du statut (1, 2, 3...).
    -   `duree_statut_jours`: Nombre de jours pendant lesquels la demande est restée dans ce statut.
-   **Usage typique :**
    ```sql
    -- Voir toutes les étapes de la demande 42
    SELECT numero_changement, statut, date_changement FROM vue_historique_statuts WHERE id_demande = 42;
    ```

---

### 3. `vue_demandes_completes`

-   **Objectif :** Fournir une vue d'ensemble complète et détaillée de chaque demande, en joignant les informations du demandeur, le type de visa, le statut actuel, et les informations sur les demandes liées (duplicata, transfert). C'est la vue principale pour lister et afficher les détails des demandes.
-   **Colonnes clés :**
    -   `id_demande`, `date_demande`
    -   `nom`, `prenom`, `email` du demandeur
    -   `type_visa`, `type_demande`
    -   `statut_actuel`
    -   Informations sur le passeport et le visa transformable (si applicable)
    -   `id_demande_origine` (pour les duplicatas/transferts)
-   **Usage typique :**
    ```sql
    -- Lister toutes les demandes avec les infos du demandeur et le statut
    SELECT id_demande, nom, prenom, type_visa, statut_actuel FROM vue_demandes_completes;

    -- Afficher les détails complets d'une seule demande
    SELECT * FROM vue_demandes_completes WHERE id_demande = 42;
    ```

---

### 4. `vue_pieces_communes_etat`

-   **Objectif :** Suivre l'état de chaque pièce justificative *commune* pour une demande donnée. Permet de savoir si une pièce est obligatoire, si elle a été marquée comme "présente" et si le fichier a été uploadé.
-   **Colonnes clés :**
    -   `id_demande`
    -   `libelle_piece`: Nom de la pièce (ex: 'Copie du passeport').
    -   `obligatoire`: `true` ou `false`.
    -   `uploaded`: `true` si un fichier a été uploadé pour cette pièce, sinon `false`.
    -   `dernier_fichier`: Nom du dernier fichier uploadé.
-   **Usage typique :**
    ```sql
    -- Lister les pièces obligatoires non encore uploadées pour la demande 42
    SELECT libelle_piece FROM vue_pieces_communes_etat
    WHERE id_demande = 42 AND obligatoire = true AND uploaded = false;
    ```

---

### 5. `vue_pieces_specifiques_etat`

-   **Objectif :** Identique à la vue précédente, mais pour les pièces justificatives *spécifiques* à un certain type de visa.
-   **Colonnes clés :** Similaires à `vue_pieces_communes_etat`.
-   **Usage typique :**
    ```sql
    -- Vérifier l'état de toutes les pièces spécifiques pour la demande 42
    SELECT libelle_piece, uploaded FROM vue_pieces_specifiques_etat WHERE id_demande = 42;
    ```

---

### 6. `vue_resume_upload_demande`

-   **Objectif :** Fournir un résumé quantitatif de l'avancement de l'upload des pièces pour chaque demande. Très utile pour le tableau de bord du scan.
-   **Colonnes clés :**
    -   `id_demande`, `nom`, `prenom`
    -   `total_pieces_communes`, `pieces_communes_uploadees`
    -   `pieces_communes_obligatoires_manquantes`
    -   Mêmes colonnes pour les pièces spécifiques.
-   **Usage typique :**
    ```sql
    -- Savoir si toutes les pièces obligatoires de la demande 42 ont été scannées
    SELECT pieces_communes_obligatoires_manquantes, pieces_specifiques_obligatoires_manquantes
    FROM vue_resume_upload_demande WHERE id_demande = 42;
    -- Si les deux sont à 0, le scan est complet.
    ```

---

### 7. `vue_demandes_a_scanner`

-   **Objectif :** Filtrer et afficher uniquement les demandes qui sont prêtes à être traitées par l'agent de scan. Elle se base sur `vue_demandes_completes` et ne retient que les statuts pertinents.
-   **Colonnes clés :** Toutes les colonnes de `vue_demandes_completes`.
-   **Usage typique :**
    ```sql
    -- Récupérer la liste de travail pour l'agent de scan
    SELECT * FROM vue_demandes_a_scanner;
    ```

---

### 8. `vue_statut_actuel_titre`

-   **Objectif :** Connaître le statut actuel d'un titre de séjour, qu'il s'agisse d'un **visa** ou d'une **carte de résident**. Elle fusionne les informations des deux types de titres.
-   **Colonnes clés :**
    -   `type_titre`: 'visa' ou 'carte'.
    -   `id_titre`: ID du visa ou de la carte.
    -   `statut_actuel`: Libellé du statut (ex: 'Actif', 'Expiré').
-   **Usage typique :**
    ```sql
    -- Vérifier le statut du visa dont l'ID est 101
    SELECT statut_actuel FROM vue_statut_actuel_titre WHERE type_titre = 'visa' AND id_titre = 101;
    ```
