import React, { useState, useEffect } from 'react';
import { Search, Filter, Plus, RefreshCw, QrCode, ChevronRight, X } from 'lucide-react';
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
      <tr 
        className="hover:bg-sand-hover cursor-pointer border-b border-sand-dark transition-colors" 
        onClick={() => navigate(`/demande/${demande.id}`)}
      >
        <td className="p-4">
          <span className="font-mono font-bold text-xs bg-gray-100 text-gray-600 px-2 py-1 rounded">#{demande.id}</span>
        </td>
        <td className="p-4">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-full bg-primary/10 flex items-center justify-center text-xs font-bold text-primary">
              {demande.demandeur_nom ? demande.demandeur_nom[0] : '?'}{demande.demandeur_prenom ? demande.demandeur_prenom[0] : '?'}
            </div>
            <div>
              <p className="font-semibold text-noir">{demande.demandeur_nom} {demande.demandeur_prenom}</p>
            </div>
          </div>
        </td>
        <td className="p-4">
          <div className="flex flex-col">
            <span className="text-sm font-bold text-noir">{demande.type_visa}</span>
            <span className="text-[10px] text-gris">{demande.type_demande}</span>
          </div>
        </td>
        <td className="p-4">
          <span className="text-sm text-gris">{new Date(demande.date_demande).toLocaleDateString()}</span>
        </td>
        <td className="p-4">
          <span className={`badge ${getStatusBadgeClass(demande.statut)}`}>
            {demande.statut}
          </span>
        </td>
        <td className="p-4" onClick={e => e.stopPropagation()}>
          <div className="flex gap-2">
            <button 
              onClick={fetchQRCode}
              disabled={loadingQr}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg border border-sand-dark bg-white text-gris hover:text-primary hover:border-primary transition-all text-xs font-semibold"
            >
              {loadingQr ? <RefreshCw size={14} className="animate-spin" /> : <QrCode size={14} />}
              QR
            </button>
            <button 
              onClick={() => navigate(`/demande/${demande.id}`)}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg border border-sand-dark bg-white text-gris hover:text-primary hover:border-primary transition-all text-xs font-semibold"
            >
              <ChevronRight size={14} />
              Détails
            </button>
          </div>
        </td>
      </tr>

      <AnimatePresence>
        {showQr && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-noir/60 backdrop-blur-sm" onClick={() => setShowQr(false)}>
            <motion.div 
              initial={{ scale: 0.95, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.95, opacity: 0 }}
              className="bg-white rounded-2xl p-8 w-full max-w-sm relative shadow-2xl border border-sand-dark"
              onClick={e => e.stopPropagation()}
            >
              <button 
                onClick={() => setShowQr(false)}
                className="absolute top-4 right-4 text-gris hover:text-noir transition-colors"
              >
                <X size={20} />
              </button>
              <div className="text-center">
                <h3 className="text-xl font-bold text-noir mb-1">Validation Dossier</h3>
                <p className="text-sm text-gris mb-6">ID : #{demande.id} - {demande.demandeur_nom}</p>
                <div className="bg-white p-4 border border-sand-dark rounded-xl inline-block shadow-inner">
                  <img src={qrCode} alt="QR Code" className="w-48 h-48" />
                </div>
                <div className="mt-8 p-3 bg-sand rounded-lg border border-sand-dark text-[10px] font-mono text-gris break-all leading-tight">
                  {`http://localhost:5173/demande/${demande.id}`}
                </div>
              </div>
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

  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      setLoading(true);
      const url = searchTerm 
        ? `http://localhost:8080/api/demandes?q=${encodeURIComponent(searchTerm)}`
        : 'http://localhost:8080/api/demandes';
      
      fetch(url)
        .then(res => res.json())
        .then(data => {
          setDemandes(Array.isArray(data) ? data : []);
          setLoading(false);
        })
        .catch(err => {
          console.error("Error fetching demandes:", err);
          setLoading(false);
        });
    }, 500);

    return () => clearTimeout(delayDebounceFn);
  }, [searchTerm]);

  return (
    <>
      <header className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-10 pb-6 border-b border-sand-dark">
        <div>
          <h1 className="text-3xl font-bold text-primary tracking-tight">Gestion des Demandes</h1>
          <p className="text-gris mt-1">Suivi des dossiers de visa et titres de séjour</p>
        </div>
        <div className="flex gap-3 w-full md:w-auto">
          <button 
            onClick={() => setSearchTerm('')}
            className="p-2.5 rounded-xl bg-white border border-sand-dark text-gris hover:bg-sand-hover transition-all shadow-sm"
            title="Actualiser"
          >
            <RefreshCw size={20} className={loading ? "animate-spin" : ""} />
          </button>
        </div>
      </header>

      <div className="flex flex-col md:flex-row gap-4 mb-8">
        <div className="relative flex-1 group">
          <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-gris group-focus-within:text-primary transition-colors" size={18} />
          <input 
            type="text" 
            placeholder="Rechercher par numéro de demande ou passeport..." 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full h-12 pl-12 pr-4 bg-white border border-sand-dark rounded-xl focus:outline-none focus:ring-4 focus:ring-primary/10 focus:border-primary transition-all shadow-sm text-noir"
          />
        </div>
        <button className="h-12 px-5 rounded-xl bg-white border border-sand-dark text-gris hover:bg-sand-hover transition-all shadow-sm">
          <Filter size={20} />
        </button>
      </div>

      <div className="bg-white rounded-2xl border border-sand-dark shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-sand/50">
                <th className="p-4 text-[11px] font-bold text-gris uppercase tracking-wider border-b border-sand-dark">ID</th>
                <th className="p-4 text-[11px] font-bold text-gris uppercase tracking-wider border-b border-sand-dark">Demandeur</th>
                <th className="p-4 text-[11px] font-bold text-gris uppercase tracking-wider border-b border-sand-dark">Type / Visa</th>
                <th className="p-4 text-[11px] font-bold text-gris uppercase tracking-wider border-b border-sand-dark">Date</th>
                <th className="p-4 text-[11px] font-bold text-gris uppercase tracking-wider border-b border-sand-dark">Statut</th>
                <th className="p-4 text-[11px] font-bold text-gris uppercase tracking-wider border-b border-sand-dark">Actions</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="6" className="p-20 text-center">
                    <div className="inline-flex items-center gap-3 text-gris font-medium">
                      <RefreshCw size={20} className="animate-spin text-primary" />
                      Chargement des dossiers...
                    </div>
                  </td>
                </tr>
              ) : demandes.length === 0 ? (
                <tr>
                  <td colSpan="6" className="p-20 text-center">
                    <div className="text-gris">
                      <p className="text-lg font-semibold">Aucun dossier trouvé</p>
                      <p className="text-sm mt-1">Essayez un autre numéro de demande ou de passeport.</p>
                    </div>
                  </td>
                </tr>
              ) : (
                demandes.map(demande => (
                  <DemandeRow key={demande.id} demande={demande} />
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};

export default DemandeList;
