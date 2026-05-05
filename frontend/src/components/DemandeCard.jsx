import React from 'react';
import { motion } from 'framer-motion';
import { Calendar, User, FileText, ChevronRight } from 'lucide-react';

const DemandeCard = ({ demande }) => {
  const getStatusBadgeClass = (status) => {
    switch (status) {
      case 'Validee':
      case 'Titre delivre':
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

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      whileHover={{ scale: 1.02 }}
      className="glass-card p-6 flex flex-col gap-4 relative overflow-hidden"
      style={{ marginBottom: '1rem' }}
    >
      <div className="flex justify-between items-start">
        <div className="flex items-center gap-3">
          <div className="p-3 rounded-xl bg-indigo-500/10 text-indigo-400">
            <FileText size={24} />
          </div>
          <div>
            <h3 className="text-lg font-bold text-white">{demande.type_visa}</h3>
            <p className="text-sm text-slate-400 flex items-center gap-1">
              <Calendar size={14} /> {new Date(demande.date_demande).toLocaleDateString()}
            </p>
          </div>
        </div>
        <span className={`badge ${getStatusBadgeClass(demande.statut)}`}>
          {demande.statut}
        </span>
      </div>

      <div className="flex items-center gap-2 mt-2">
        <div className="w-8 h-8 rounded-full bg-slate-700 flex items-center justify-center text-xs font-bold text-slate-300">
          {demande.demandeur_nom[0]}{demande.demandeur_prenom[0]}
        </div>
        <div>
          <p className="text-sm font-medium text-slate-200">{demande.demandeur_nom} {demande.demandeur_prenom}</p>
          <p className="text-xs text-slate-500">{demande.type_demande}</p>
        </div>
      </div>

      <div className="flex justify-between items-center mt-4 pt-4 border-t border-slate-700/50">
        <span className="text-xs text-slate-500">ID: #{demande.id}</span>
        <button className="text-indigo-400 flex items-center gap-1 text-sm font-semibold hover:text-indigo-300">
          Détails <ChevronRight size={16} />
        </button>
      </div>
    </motion.div>
  );
};

export default DemandeCard;
