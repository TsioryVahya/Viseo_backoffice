-- =============================================
-- BASE DE DONNeES VISA - PostgreSQL
-- =============================================

CREATE TABLE Nationalite (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE Situation_familiale (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE type_visa (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE Type_demande (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

CREATE TABLE Demandeur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE NOT NULL,
    lieu_naissance VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    adresse TEXT NOT NULL,
    id_situation_familiale INT NOT NULL,
    id_nationalite INT NOT NULL,
    FOREIGN KEY (id_situation_familiale) REFERENCES Situation_familiale(id),
    FOREIGN KEY (id_nationalite) REFERENCES Nationalite(id)
);

CREATE TABLE Passeport (
    id SERIAL PRIMARY KEY,
    id_demandeur INT NOT NULL,
    numero_passeport VARCHAR(50) NOT NULL UNIQUE,
    date_delivrance DATE NOT NULL,
    date_expiration DATE NOT NULL,
    pays_delivrance VARCHAR(100),
    FOREIGN KEY (id_demandeur) REFERENCES Demandeur(id)
);

CREATE TABLE Visa_transformable (
    id SERIAL PRIMARY KEY,
    id_demandeur INT NOT NULL,
    id_passeport INT NOT NULL,
    numero_reference VARCHAR(50) NOT NULL UNIQUE,
    date_expiration DATE NOT NULL,
    FOREIGN KEY (id_demandeur) REFERENCES Demandeur(id),
    FOREIGN KEY (id_passeport) REFERENCES Passeport(id)
);

CREATE TABLE Demande (
    id SERIAL PRIMARY KEY,
    id_visa_transformable INT,
    date_demande DATE NOT NULL,
    id_demandeur INT NOT NULL,
    id_type_visa INT NOT NULL,
    id_type_demande INT NOT NULL,
    FOREIGN KEY (id_type_demande) REFERENCES Type_demande(id),
    FOREIGN KEY (id_demandeur) REFERENCES Demandeur(id),
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id),
    FOREIGN KEY (id_visa_transformable) REFERENCES Visa_transformable(id)
);

CREATE TABLE Statut_demande_type (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL,
    description TEXT

);

CREATE TABLE Statut_demande (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    id_statut_type INT NOT NULL,
    date_changement TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (id_demande) REFERENCES Demande(id),
    FOREIGN KEY (id_statut_type) REFERENCES Statut_demande_type(id)
);
CREATE TABLE Type_piece_commune (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(150) NOT NULL,
    obligatoire BOOLEAN DEFAULT TRUE
);

CREATE TABLE Type_piece_specifique (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(150) NOT NULL,
    id_type_visa INT NOT NULL,
    obligatoire BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id)
);

-- =============================================
-- PIECE_DEMANDE — modifiee
-- =============================================
CREATE TABLE Piece_demande (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    id_type_piece_commune INT NOT NULL,
    presente BOOLEAN DEFAULT FALSE,
    uploaded BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_demande) REFERENCES Demande(id),
    FOREIGN KEY (id_type_piece_commune) REFERENCES Type_piece_commune(id),
    UNIQUE (id_demande, id_type_piece_commune)
);

-- =============================================
-- PIECE_DEMANDE_SPECIFIQUE — modifiee
-- =============================================
CREATE TABLE Piece_demande_specifique (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    id_type_piece INT NOT NULL,
    presente BOOLEAN DEFAULT FALSE,
    uploaded BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_demande) REFERENCES Demande(id),
    FOREIGN KEY (id_type_piece) REFERENCES Type_piece_specifique(id),
    UNIQUE (id_demande, id_type_piece)
);


-- =============================================
-- UPLOAD_PIECE — nouvelle table
-- =============================================
CREATE TABLE Upload_piece (
    id SERIAL PRIMARY KEY,
    id_piece_demande INT,
    id_piece_demande_specifique INT,
    chemin_fichier VARCHAR(255) NOT NULL,
    nom_fichier_original VARCHAR(255) NOT NULL,
    date_upload DATE NOT NULL,
    FOREIGN KEY (id_piece_demande)
        REFERENCES Piece_demande(id),
    FOREIGN KEY (id_piece_demande_specifique)
        REFERENCES Piece_demande_specifique(id),
    CONSTRAINT check_piece_type
        CHECK (
            (id_piece_demande IS NOT NULL AND id_piece_demande_specifique IS NULL)
            OR
            (id_piece_demande IS NULL AND id_piece_demande_specifique IS NOT NULL)
        )
);

CREATE TABLE Visa (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    reference VARCHAR(50),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_passeport INT NOT NULL,
    FOREIGN KEY (id_passeport) REFERENCES Passeport(id),
    FOREIGN KEY (id_demande) REFERENCES Demande(id)
);

CREATE TABLE carte_resident (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    reference VARCHAR(50),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_passeport INT NOT NULL,
    FOREIGN KEY (id_demande) REFERENCES Demande(id),
    FOREIGN KEY (id_passeport) REFERENCES Passeport(id)
);

-- =============================================
-- DONNeES DE ReFeRENCE
-- =============================================

INSERT INTO Nationalite (libelle) VALUES ('Français'), ('Malagasy'), ('Chinois(e)'), ('Indien(ne)'), ('Autre');

INSERT INTO Situation_familiale (libelle) VALUES ('Celibataire'), ('Marie(e)'), ('Divorce(e)'), ('Veuf/Veuve');

INSERT INTO type_visa (libelle) VALUES ('Investisseur'), ('Travailleur');

INSERT INTO Type_demande (libelle) VALUES ('Nouveau titre'), ('Duplicata'), ('Transfert de visa');

INSERT INTO Statut_demande_type (id, libelle, description) VALUES
(1, 'Dossier cree', 'Demande soumise, en attente de traitement'),
(2, 'Pieces manquantes', 'Demande suspendue, des pieces sont manquantes ou invalides'),
(3, 'Scan termine', 'Scan des pieces termine'),
(4, 'Rejetee', 'Demande rejetee'),
(5, 'Titre delivre', 'Le titre a ete remis au demandeur'),
(6, 'Validee', 'Demande validee par les agents'),
(7, 'En cours de saisie', 'Demande initialisee et en cours de saisie');

INSERT INTO Type_piece_commune (libelle, obligatoire) VALUES
('02 photos d''identite recentes', TRUE),
('Notice de renseignement', TRUE),
('Demande ecrite adressee au Ministere de l''Interieur', TRUE),
('Photocopie certifiee de la premiere page du passeport', TRUE),
('Certificat de residence ou attestation d''hebergement', TRUE),
('Extrait de casier judiciaire (< 3 mois)', TRUE);

INSERT INTO Type_piece_specifique (libelle, id_type_visa, obligatoire) VALUES
('Statut de la Societe', 1, TRUE),
('Extrait d''inscription au registre de commerce', 1, TRUE),
('Carte fiscale', 1, TRUE),
('Contrat de travail', 2, TRUE),
('Autorisation de travail', 2, TRUE);

