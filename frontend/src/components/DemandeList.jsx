import React, { useState, useEffect } from 'react';
import { Search, Filter, Plus, RefreshCw, QrCode, ChevronRight, X, Calendar, FileText, User } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';

const DemandeRow = ({ demande }) => {
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
      <tr className="cursor-pointer" onClick={() => navigate(`/demande/${demande.id}`)}>
        <td>
          <span className="font-mono font-bold text-xs bg-slate-100 px-2 py-1 rounded">#{demande.id}</span>
        </td>
        <td>
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-[#3E5F44]/10 flex items-center justify-center text-xs font-bold text-[#3E5F44]">
              {demande.demandeur_nom ? demande.demandeur_nom[0] : '?'}{demande.demandeur_prenom ? demande.demandeur_prenom[0] : '?'}
            </div>
            <div>
              <p className="font-medium">{demande.demandeur_nom} {demande.demandeur_prenom}</p>
            </div>
          </div>
        </td>
        <td>
          <div className="flex flex-col">
            <span className="text-sm font-semibold">{demande.type_visa}</span>
            <span className="text-[10px] text-slate-500">{demande.type_demande}</span>
          </div>
        </td>
        <td>
          <span className="text-sm text-slate-600">{new Date(demande.date_demande).toLocaleDateString()}</span>
        </td>
        <td>
          <span className={`badge ${getStatusBadgeClass(demande.statut)}`}>
            {demande.statut}
          </span>
        </td>
        <td onClick={e => e.stopPropagation()}>
          <div className="action-buttons">
            <button 
              onClick={fetchQRCode}
              disabled={loadingQr}
              className="btn-table"
              title="QR Code"
            >
              {loadingQr ? (
                <RefreshCw size={14} className="animate-spin" />
              ) : (
                <QrCode size={14} />
              )}
              QR
            </button>
            <button 
              onClick={() => navigate(`/demande/${demande.id}`)}
              className="btn-table"
              title="Détails"
            >
              <ChevronRight size={14} />
              Détails
            </button>
          </div>
        </td>
      </tr>

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
              <h3 className="text-lg font-bold text-[#1a1a16] mb-1 text-center">Validation Dossier</h3>
              <p className="text-sm text-[#6b6b60] text-center mb-6">
                ID : #{demande.id} - {demande.demandeur_nom}
              </p>
              <div className="bg-white p-3 border border-[#DDD6B9] rounded-xl flex justify-center">
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

const DemandeList = () => {
  const [demandes, setDemandes] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  const fetchDemandes = () => {
    setLoading(true);
    fetch('http://localhost:8080/api/demandes')
      .then(res => res.json())
      .then(data => {
        setDemandes(Array.isArray(data) ? data : []);
        setLoading(false);
      })
      .catch(err => {
        console.error("Error fetching demandes:", err);
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchDemandes();
  }, []);

  const filteredDemandes = demandes.filter(d => 
    d.demandeur_nom?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    d.demandeur_prenom?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    d.id?.toString().includes(searchTerm)
  );

  return (
    <div className="container">
      <header className="header">
        <div>
          <h1 className="title">Gestion des Demandes</h1>
          <p className="subtitle">Suivi des dossiers de visa et titres de séjour</p>
        </div>
        <div className="flex gap-3">
          <button 
            onClick={fetchDemandes}
            className="p-2.5 rounded-lg bg-white border border-[#DDD6B9] text-[#6b6b60] hover:bg-[#F5F1E6] transition-all"
            title="Actualiser"
          >
            <RefreshCw size={18} className={loading ? "animate-spin" : ""} />
          </button>
          <button className="btn btn-primary">
            <Plus size={18} /> Nouvelle Demande
          </button>
        </div>
      </header>

      <div className="flex flex-col md:flex-row gap-4 mb-6">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-[#6b6b60]" size={16} />
          <input 
            type="text" 
            placeholder="Rechercher par nom ou numéro..." 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>
        <button className="bg-white border border-[#DDD6B9] p-2.5 rounded-lg text-[#6b6b60] hover:bg-[#F5F1E6] transition-colors">
          <Filter size={18} />
        </button>
      </div>

      <div className="table-container">
        {loading ? (
          <div className="flex flex-col items-center justify-center py-20">
            <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-[#3E5F44] mb-4"></div>
            <p className="text-sm text-[#6b6b60]">Chargement...</p>
          </div>
        ) : (
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Demandeur</th>
                <th>Type / Visa</th>
                <th>Date</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredDemandes.map(demande => (
                <DemandeRow key={demande.id} demande={demande} />
              ))}
              {filteredDemandes.length === 0 && (
                <tr>
                  <td colSpan="6" className="text-center py-10 text-[#6b6b60]">
                    Aucun dossier trouvé pour "{searchTerm}"
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default DemandeList;
