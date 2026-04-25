package com.viseo.backoffice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "demande_liee")
public class DemandeLiee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_demande_origine", nullable = false)
    private Demande demandeOrigine;

    @ManyToOne
    @JoinColumn(name = "id_demande_liee", nullable = false)
    private Demande demandeLiee;

    @Column(name = "type_lien", nullable = false, length = 50)
    private String typeLien;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Demande getDemandeOrigine() {
        return demandeOrigine;
    }

    public void setDemandeOrigine(Demande demandeOrigine) {
        this.demandeOrigine = demandeOrigine;
    }

    public Demande getDemandeLiee() {
        return demandeLiee;
    }

    public void setDemandeLiee(Demande demandeLiee) {
        this.demandeLiee = demandeLiee;
    }

    public String getTypeLien() {
        return typeLien;
    }

    public void setTypeLien(String typeLien) {
        this.typeLien = typeLien;
    }
}
