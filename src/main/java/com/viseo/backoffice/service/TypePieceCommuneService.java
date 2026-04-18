package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.TypePieceCommune;
import com.viseo.backoffice.repository.TypePieceCommuneRepository;

@Service
public class TypePieceCommuneService {

    private final TypePieceCommuneRepository typePieceCommuneRepository;

    public TypePieceCommuneService(TypePieceCommuneRepository typePieceCommuneRepository) {
        this.typePieceCommuneRepository = typePieceCommuneRepository;
    }

    public List<TypePieceCommune> findAll() {
        return typePieceCommuneRepository.findAll();
    }

    public Optional<TypePieceCommune> findById(Integer id) {
        return typePieceCommuneRepository.findById(id);
    }

    public TypePieceCommune save(TypePieceCommune entity) {
        return typePieceCommuneRepository.save(entity);
    }

    public void deleteById(Integer id) {
        typePieceCommuneRepository.deleteById(id);
    }
}
