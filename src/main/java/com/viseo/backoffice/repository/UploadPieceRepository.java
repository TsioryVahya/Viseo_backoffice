package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.PieceDemande;
import com.viseo.backoffice.model.PieceDemandeSpecifique;
import com.viseo.backoffice.model.UploadPiece;

public interface UploadPieceRepository extends JpaRepository<UploadPiece, Integer> {

    List<UploadPiece> findByPieceDemande(PieceDemande pieceDemande);

    List<UploadPiece> findByPieceDemandeSpecifique(PieceDemandeSpecifique piece);

    Optional<UploadPiece> findFirstByPieceDemandeOrderByDateUploadDesc(PieceDemande piece);

    Optional<UploadPiece> findFirstByPieceDemandeSpecifiqueOrderByDateUploadDesc(PieceDemandeSpecifique piece);
}
