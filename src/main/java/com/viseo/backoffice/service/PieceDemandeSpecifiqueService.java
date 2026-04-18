package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.PieceDemandeSpecifique;
import com.viseo.backoffice.repository.PieceDemandeSpecifiqueRepository;

@Service
public class PieceDemandeSpecifiqueService {

    private final PieceDemandeSpecifiqueRepository pieceDemandeSpecifiqueRepository;

    public PieceDemandeSpecifiqueService(PieceDemandeSpecifiqueRepository pieceDemandeSpecifiqueRepository) {
        this.pieceDemandeSpecifiqueRepository = pieceDemandeSpecifiqueRepository;
    }

    public List<PieceDemandeSpecifique> findAll() {
        return pieceDemandeSpecifiqueRepository.findAll();
    }

    public Optional<PieceDemandeSpecifique> findById(Integer id) {
        return pieceDemandeSpecifiqueRepository.findById(id);
    }

    public PieceDemandeSpecifique save(PieceDemandeSpecifique entity) {
        return pieceDemandeSpecifiqueRepository.save(entity);
    }

    public void deleteById(Integer id) {
        pieceDemandeSpecifiqueRepository.deleteById(id);
    }
}
