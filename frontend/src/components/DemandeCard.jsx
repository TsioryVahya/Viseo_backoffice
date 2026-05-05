import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Calendar, FileText, ChevronRight, QrCode, X, User } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const DemandeCard = ({ demande }) => {
  const navigate = useNavigate();
  const [qrCode, setQrCode] = useState(null);
  const [showQr, setShowQr] = useState(false);
  const [loadingQr, setLoadingQr] = useState(false);

  const getStatusBadgeClass = (status) => {
    switch (status) {
      case 'Validee':
      case 'Titre delivre':
      case 'Scan termine':
        return 'badge-success';
      case 'Pieces manquantes':
      case 'En cours de saisie':
        return 'badge-warning';
      case 'Rejetee':
        return 'badge-danger';
      default:
        return 'badge-info';
    }
  };

  const fetchQRCode = async (e) => {
    e.stopPropagation();
    if (qrCode) {
      setShowQr(true);
      return;
    }

    setLoadingQr(true);
    try {
      const response = await fetch(`http://localhost:8080/api/demandes/${demande.id}/qr`);
      const data = await response.json();
      setQrCode(data.qr);
      setShowQr(true);
    } catch (err) {
      console.error("Failed to fetch QR code", err);
    } finally {
      setLoadingQr(false);
    }
  };

  return (
    <>
      <motion.div 
        whileHover={{ translateY: -4 }}
        className="card flex flex-col gap-4 cursor-pointer"
        onClick={() => navigate(`/demande/${demande.id}`)}
      >
        <div className="flex justify-between items-start">
          <div className="flex items-center gap-3">
            <div className="p-2.5 rounded-lg bg-[#F5F1E6] text-[#3E5F44]">
              <FileText size={20} />
            </div>
            <div>
              <h3 className="text-base font-semibold text-[#1a1a16]">{demande.type_visa}</h3>
              <p className="text-xs text-[#6b6b60] flex items-center gap-1 mt-0.5">
                <Calendar size={12} /> {new Date(demande.date_demande).toLocaleDateString()}
              </p>
            </div>
          </div>
          <span className={`badge ${getStatusBadgeClass(demande.statut)}`}>
            {demande.statut}
          </span>
        </div>

        <div className="flex items-center gap-3 py-2">
          <div className="w-9 h-9 rounded-full bg-[#3E5F44]/10 flex items-center justify-center text-sm font-bold text-[#3E5F44]">
            {demande.demandeur_nom ? demande.demandeur_nom[0] : '?'}{demande.demandeur_prenom ? demande.demandeur_prenom[0] : '?'}
          </div>
          <div>
            <p className="text-sm font-medium text-[#1a1a16]">{demande.demandeur_nom} {demande.demandeur_prenom}</p>
            <p className="text-xs text-[#6b6b60]">{demande.type_demande}</p>
          </div>
        </div>

        <div className="flex justify-between items-center mt-2 pt-4 border-t border-[#DDD6B9]">
          <button 
            onClick={fetchQRCode}
            disabled={loadingQr}
            className="flex items-center gap-2 text-[#6b6b60] hover:text-[#3E5F44] transition-colors text-sm font-medium"
          >
            {loadingQr ? (
              <div className="w-4 h-4 border-2 border-[#3E5F44] border-t-transparent rounded-full animate-spin"></div>
            ) : (
              <QrCode size={16} />
            )}
            QR Code
          </button>
          <button className="text-[#3E5F44] flex items-center gap-1 text-sm font-semibold hover:underline">
            Détails <ChevronRight size={14} />
          </button>
        </div>
      </motion.div>

      <AnimatePresence>
        {showQr && (
          <div className="modal-overlay" onClick={() => setShowQr(false)}>
            <motion.div 
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="modal-content"
              onClick={e => e.stopPropagation()}
            >
              <button 
                onClick={() => setShowQr(false)}
                className="absolute top-4 right-4 text-[#6b6b60] hover:text-[#1a1a16]"
              >
                <X size={20} />
              </button>
              <h3 className="text-lg font-bold text-[#1a1a16] mb-1">QR Code de Validation</h3>
              <p className="text-sm text-[#6b6b60] text-center mb-6">
                Référence dossier : #{demande.id}
              </p>
              <div className="bg-white p-3 border border-[#DDD6B9] rounded-xl">
                <img src={qrCode} alt="QR Code" className="w-48 h-48" />
              </div>
              <p className="mt-6 text-[10px] text-[#6b6b60] font-mono bg-[#F5F1E6] px-3 py-2 rounded-lg break-all text-center w-full">
                {`http://localhost:5173/demande/${demande.id}`}
              </p>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </>
  );
};

export default DemandeCard;
