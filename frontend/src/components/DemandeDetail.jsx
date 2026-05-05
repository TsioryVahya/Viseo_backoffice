import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { ChevronLeft, User, Calendar, FileText, Phone, Mail, CheckCircle2, ShieldCheck, Printer } from 'lucide-react';

const DemandeDetail = () => {
  const { id } = useParams();
  const [demande, setDemande] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(`http://localhost:8080/api/demandes/${id}`)
      .then(res => res.json())
      .then(data => {
        setDemande(data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, [id]);

  if (loading) {
    return (
      <div className="container flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-10 w-10 border-t-2 border-b-2 border-[#3E5F44]"></div>
      </div>
    );
  }

  if (!demande || demande.error) {
    return (
      <div className="container text-center py-20">
        <h2 className="text-xl font-bold text-[#1a1a16]">Demande non trouvée</h2>
        <Link to="/" className="text-[#3E5F44] mt-4 inline-block hover:underline font-medium">Retour à la liste</Link>
      </div>
    );
  }

  return (
    <motion.div 
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="container max-w-3xl"
    >
      <div className="flex justify-between items-center mb-6">
        <Link to="/" className="flex items-center gap-2 text-[#6b6b60] hover:text-[#3E5F44] transition-colors font-medium">
          <ChevronLeft size={18} /> Retour à la liste des demandes
        </Link>
        <button 
          onClick={() => window.print()}
          className="btn-table flex items-center gap-2 px-4"
        >
          <Printer size={16} /> Imprimer
        </button>
      </div>

      <div className="card shadow-xl border-[#DDD6B9] relative overflow-hidden">
        {/* Top accent bar */}
        <div className="absolute top-0 left-0 w-full h-2 bg-[#3E5F44]"></div>

        {/* Header Section */}
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-10 pt-4 border-b border-[#F5F1E6] pb-8 gap-6">
          <div className="flex items-center gap-4">
            <div className="w-16 h-16 bg-[#3E5F44] rounded-2xl flex items-center justify-center text-white shadow-lg shadow-[#3E5F44]/20">
              <ShieldCheck size={36} />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-[#1a1a16] tracking-tight">Récapitulatif Officiel</h1>
              <p className="text-sm text-[#6b6b60] font-medium uppercase tracking-wider">Viseo Backoffice System</p>
            </div>
          </div>
          <div className="bg-[#F5F1E6] p-4 rounded-xl border border-[#DDD6B9] text-right min-w-[180px]">
            <p className="text-[10px] text-[#6b6b60] uppercase font-bold mb-1">Numéro de Dossier</p>
            <p className="text-2xl font-mono font-black text-[#3E5F44]">#{String(demande.id).padStart(5, '0')}</p>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-x-12 gap-y-10">
          {/* Identity Section */}
          <section className="space-y-6">
            <div className="flex items-center gap-2 border-b border-[#F5F1E6] pb-2 mb-4">
              <User size={18} className="text-[#3E5F44]" />
              <h3 className="text-sm font-bold text-[#1a1a16] uppercase tracking-widest">Identité du Demandeur</h3>
            </div>
            
            <div className="space-y-4 px-1">
              <div>
                <p className="text-[10px] text-[#6b6b60] uppercase font-bold mb-1">Nom et Prénom</p>
                <p className="text-base text-[#1a1a16] font-semibold">{demande.demandeur.nom} {demande.demandeur.prenom}</p>
              </div>
              
              <div>
                <p className="text-[10px] text-[#6b6b60] uppercase font-bold mb-1">Contact Téléphonique</p>
                <p className="text-base text-[#1a1a16] font-semibold flex items-center gap-2">
                  <Phone size={14} className="text-[#3E5F44]" /> {demande.demandeur.telephone}
                </p>
              </div>
              
              <div>
                <p className="text-[10px] text-[#6b6b60] uppercase font-bold mb-1">Adresse Email</p>
                <p className="text-sm text-[#1a1a16] font-semibold break-all flex items-center gap-2">
                  <Mail size={14} className="text-[#3E5F44]" /> {demande.demandeur.email}
                </p>
              </div>
            </div>
          </section>

          {/* Visa Section */}
          <section className="space-y-6">
            <div className="flex items-center gap-2 border-b border-[#F5F1E6] pb-2 mb-4">
              <FileText size={18} className="text-[#3E5F44]" />
              <h3 className="text-sm font-bold text-[#1a1a16] uppercase tracking-widest">Détails de la Demande</h3>
            </div>
            
            <div className="space-y-4 px-1">
              <div className="bg-[#F5F1E6]/50 p-3 rounded-lg border border-[#DDD6B9]/50">
                <p className="text-[10px] text-[#6b6b60] uppercase font-bold mb-1">Catégorie de Visa</p>
                <p className="text-base text-[#3E5F44] font-bold">{demande.type_visa}</p>
              </div>
              
              <div className="bg-[#F5F1E6]/50 p-3 rounded-lg border border-[#DDD6B9]/50">
                <p className="text-[10px] text-[#6b6b60] uppercase font-bold mb-1">Nature de la Procédure</p>
                <p className="text-base text-[#1a1a16] font-semibold">{demande.type_demande}</p>
              </div>
              
              <div>
                <p className="text-[10px] text-[#6b6b60] uppercase font-bold mb-1">Date de Soumission</p>
                <p className="text-base text-[#1a1a16] font-semibold flex items-center gap-2">
                  <Calendar size={14} className="text-[#3E5F44]" /> {new Date(demande.date_demande).toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' })}
                </p>
              </div>
            </div>
          </section>
        </div>

        {/* Validation Footer */}
        <div className="mt-16 pt-8 border-t border-[#F5F1E6] flex flex-col items-center">
          <div className="flex items-center gap-3 bg-[#e9f7ef] px-6 py-3 rounded-full border border-[#bfe6ce] mb-4">
            <CheckCircle2 size={20} className="text-[#1f7a45]" />
            <p className="text-[#1f7a45] text-xs font-bold uppercase tracking-wide">Dossier certifié conforme</p>
          </div>
          <p className="text-[#6b6b60] text-[10px] text-center max-w-sm">
            Ce document constitue un récapitulatif officiel généré par le système central de gestion Viseo. 
            Toute modification manuelle rend ce document caduc.
          </p>
        </div>

        {/* Watermark-like element */}
        <div className="absolute bottom-[-20px] right-[-20px] opacity-[0.03] rotate-[-15deg] pointer-events-none">
          <ShieldCheck size={200} className="text-[#3E5F44]" />
        </div>
      </div>
    </motion.div>
  );
};

export default DemandeDetail;
