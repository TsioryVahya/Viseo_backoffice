import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { ChevronLeft, Printer, User, FileText, CheckCircle, Info, Phone, Mail } from 'lucide-react';

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
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-10 w-10 border-t-2 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (!demande || demande.error) {
    return (
      <div className="text-center py-20 bg-white rounded-2xl border border-sand-dark shadow-sm">
        <h2 className="text-xl font-bold text-noir">Demande non trouvée</h2>
        <p className="text-gris mt-2">Le dossier N° {id} n'existe pas dans notre base de données.</p>
        <Link to="/" className="text-primary mt-6 inline-flex items-center gap-2 hover:underline font-bold">
          <ChevronLeft size={18} /> Retour à la liste
        </Link>
      </div>
    );
  }

  return (
    <motion.div 
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-4xl mx-auto"
    >
      <div className="flex justify-between items-center mb-8 no-print">
        <Link to="/" className="flex items-center gap-2 text-gris hover:text-primary transition-colors font-semibold">
          <ChevronLeft size={20} /> Retour à la gestion des demandes
        </Link>
        <button 
          onClick={() => window.print()}
          className="btn-secondary shadow-sm"
        >
          <Printer size={18} /> Imprimer le récapitulatif
        </button>
      </div>

      <div className="space-y-6">
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center bg-white p-8 rounded-2xl border border-sand-dark shadow-sm gap-6">
          <div className="flex items-center gap-5">
            <div className="w-16 h-16 bg-primary rounded-2xl flex items-center justify-center text-white shadow-lg shadow-primary/20">
              <FileText size={32} />
            </div>
            <div>
              <h1 className="text-2xl font-black text-noir tracking-tight">Récapitulatif Dossier</h1>
              <p className="text-gris mt-1 flex items-center gap-2 text-sm">
                <Info size={14} /> Référence : <span className="font-bold text-primary font-mono">#{String(demande.id).padStart(5, '0')}</span>
              </p>
            </div>
          </div>
          <div className="flex items-center gap-3 bg-green-50 px-5 py-2.5 rounded-full border border-green-200">
            <CheckCircle size={20} className="text-green-600" />
            <span className="text-green-700 font-bold text-xs uppercase tracking-widest">Dossier Certifié Conforme</span>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <section className="bg-white border border-sand-dark rounded-2xl p-8 shadow-sm">
              <h3 className="text-primary font-bold flex items-center gap-2 mb-8 pb-3 border-b border-sand">
                <User size={18} />
                Informations Personnelles
              </h3>
              <div className="space-y-6">
                <div className="resume-grid">
                  <div className="resume-label">Nom complet</div>
                  <div className="resume-value uppercase tracking-tight">{demande.demandeur.nom}</div>
                  
                  <div className="resume-label">Prénom(s)</div>
                  <div className="resume-value">{demande.demandeur.prenom}</div>
                  
                  <div className="resume-label">Adresse Email</div>
                  <div className="resume-value text-sm font-mono lowercase break-all">{demande.demandeur.email}</div>
                  
                  <div className="resume-label">Téléphone</div>
                  <div className="resume-value">{demande.demandeur.telephone}</div>
                </div>
              </div>
            </section>

            <section className="bg-white border border-sand-dark rounded-2xl p-8 shadow-sm">
              <h3 className="text-primary font-bold flex items-center gap-2 mb-8 pb-3 border-b border-sand">
                <FileText size={18} />
                Détails de la Demande
              </h3>
              <div className="space-y-6">
                <div className="resume-grid">
                  <div className="resume-label">Catégorie Visa</div>
                  <div className="resume-value text-primary font-black">{demande.type_visa}</div>
                  
                  <div className="resume-label">Procédure</div>
                  <div className="resume-value">{demande.type_demande}</div>
                  
                  <div className="resume-label">Date Soumission</div>
                  <div className="resume-value">{new Date(demande.date_demande).toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' })}</div>
                </div>
              </div>
            </section>
          </div>

          <section className="bg-white border border-sand-dark rounded-2xl p-8 shadow-sm">
            <h3 className="text-primary font-bold flex items-center gap-2 mb-6">
              <Info size={18} />
              Historique des Statuts
            </h3>
            <div className="space-y-4">
              {demande.historique_statuts.map((statut, index) => (
                <div key={index} className="flex items-start gap-3">
                  <div className="w-8 h-8 flex-shrink-0 bg-sand rounded-full flex items-center justify-center mt-1">
                    <CheckCircle size={16} className="text-primary" />
                  </div>
                  <div>
                    <p className="font-semibold text-noir text-sm">{statut.statut}</p>
                    <p className="text-gris text-xs font-mono">{new Date(statut.date).toLocaleString('fr-FR')}</p>
                  </div>
                </div>
              ))}
            </div>
          </section>
        </div>

        <div className="p-10 bg-white border-2 border-dashed border-sand-dark rounded-3xl relative overflow-hidden group">
          <div className="relative z-10 flex flex-col items-center text-center">
            <div className="w-12 h-12 bg-sand rounded-full flex items-center justify-center mb-4 group-hover:scale-110 transition-transform duration-500">
              <CheckCircle size={24} className="text-primary" />
            </div>
            <p className="text-noir font-black text-lg">Certification Système Viseo</p>
            <p className="text-gris text-sm mt-3 leading-relaxed max-w-lg">
              Ce document constitue un récapitulatif officiel généré par le système central de gestion Viseo. 
              Il atteste de l'intégrité des données enregistrées pour le traitement de votre demande.
            </p>
          </div>
          {/* Subtle decoration */}
          <div className="absolute -bottom-16 -right-16 opacity-[0.03] text-primary pointer-events-none group-hover:opacity-[0.06] transition-opacity duration-1000">
            <CheckCircle size={280} />
          </div>
        </div>
      </div>

      <style dangerouslySetInnerHTML={{ __html: `
        @media print {
          .no-print { display: none !important; }
          body { background: white !important; }
          .main-content { padding: 0 !important; max-width: 100% !important; }
          .bg-white { border: 1px solid #DDD6B9 !important; box-shadow: none !important; }
          .rounded-2xl, .rounded-3xl { border-radius: 8px !important; }
          .p-8, .p-10 { padding: 1.5rem !important; }
          .resume-grid { grid-template-columns: 180px 1fr !important; gap: 0.5rem !important; }
        }
      `}} />
    </motion.div>
  );
};

export default DemandeDetail;
