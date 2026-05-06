package com.viseo.backoffice.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "statut_demande")
public class StatutDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    @JsonBackReference
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_statut_type", nullable = false)
    private StatutDemandeType statutDemandeType;

    @Column(name = "date_changement")
    private LocalDateTime dateChangement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public StatutDemandeType getStatutDemandeType() {
        return statutDemandeType;
    }

    public void setStatutDemandeType(StatutDemandeType statutDemandeType) {
        this.statutDemandeType = statutDemandeType;
    }

    public LocalDateTime getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(LocalDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }
}
