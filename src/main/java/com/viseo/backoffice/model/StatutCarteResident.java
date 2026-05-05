package com.viseo.backoffice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "statut_carte_resident")
public class StatutCarteResident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_carte_resident", nullable = false)
    private CarteResident carteResident;

    @ManyToOne
    @JoinColumn(name = "id_statut_type", nullable = false)
    private StatutTitreType statutType;

    @Column(name = "date_changement")
    private LocalDateTime dateChangement;

    @Column(name = "commentaire")
    private String commentaire;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CarteResident getCarteResident() {
        return carteResident;
    }

    public void setCarteResident(CarteResident carteResident) {
        this.carteResident = carteResident;
    }

    public StatutTitreType getStatutType() {
        return statutType;
    }

    public void setStatutType(StatutTitreType statutType) {
        this.statutType = statutType;
    }

    public LocalDateTime getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(LocalDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
