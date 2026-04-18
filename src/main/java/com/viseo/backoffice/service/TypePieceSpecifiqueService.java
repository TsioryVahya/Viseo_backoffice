package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.TypePieceSpecifique;
import com.viseo.backoffice.repository.TypePieceSpecifiqueRepository;

@Service
public class TypePieceSpecifiqueService {

    private final TypePieceSpecifiqueRepository typePieceSpecifiqueRepository;

    public TypePieceSpecifiqueService(TypePieceSpecifiqueRepository typePieceSpecifiqueRepository) {
        this.typePieceSpecifiqueRepository = typePieceSpecifiqueRepository;
    }

    public List<TypePieceSpecifique> findAll() {
        return typePieceSpecifiqueRepository.findAll();
    }

    public Optional<TypePieceSpecifique> findById(Integer id) {
        return typePieceSpecifiqueRepository.findById(id);
    }

    public TypePieceSpecifique save(TypePieceSpecifique entity) {
        return typePieceSpecifiqueRepository.save(entity);
    }

    public void deleteById(Integer id) {
        typePieceSpecifiqueRepository.deleteById(id);
    }
}
