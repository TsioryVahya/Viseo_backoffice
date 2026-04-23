package com.viseo.backoffice.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "upload_piece")
public class UploadPiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_piece_demande")
    private PieceDemande pieceDemande;

    @ManyToOne
    @JoinColumn(name = "id_piece_demande_specifique")
    private PieceDemandeSpecifique pieceDemandeSpecifique;

    @Column(name = "chemin_fichier", nullable = false, length = 255)
    private String cheminFichier;

    @Column(name = "nom_fichier_original", nullable = false, length = 255)
    private String nomFichierOriginal;

    @Column(name = "date_upload", nullable = false)
    private LocalDate dateUpload;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PieceDemande getPieceDemande() {
        return pieceDemande;
    }

    public void setPieceDemande(PieceDemande pieceDemande) {
        this.pieceDemande = pieceDemande;
    }

    public PieceDemandeSpecifique getPieceDemandeSpecifique() {
        return pieceDemandeSpecifique;
    }

    public void setPieceDemandeSpecifique(PieceDemandeSpecifique pieceDemandeSpecifique) {
        this.pieceDemandeSpecifique = pieceDemandeSpecifique;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public String getNomFichierOriginal() {
        return nomFichierOriginal;
    }

    public void setNomFichierOriginal(String nomFichierOriginal) {
        this.nomFichierOriginal = nomFichierOriginal;
    }

    public LocalDate getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(LocalDate dateUpload) {
        this.dateUpload = dateUpload;
    }
}
