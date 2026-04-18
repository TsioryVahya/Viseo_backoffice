package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.PieceDemande;
import com.viseo.backoffice.repository.PieceDemandeRepository;

@Service
public class PieceDemandeService {

    private final PieceDemandeRepository pieceDemandeRepository;

    public PieceDemandeService(PieceDemandeRepository pieceDemandeRepository) {
        this.pieceDemandeRepository = pieceDemandeRepository;
    }

    public List<PieceDemande> findAll() {
        return pieceDemandeRepository.findAll();
    }

    public Optional<PieceDemande> findById(Integer id) {
        return pieceDemandeRepository.findById(id);
    }

    public PieceDemande save(PieceDemande entity) {
        return pieceDemandeRepository.save(entity);
    }

    public void deleteById(Integer id) {
        pieceDemandeRepository.deleteById(id);
    }
}
