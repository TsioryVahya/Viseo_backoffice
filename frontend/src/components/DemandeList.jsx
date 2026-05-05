import React, { useState } from 'react';
import DemandeCard from './DemandeCard';
import { Search, Filter, Plus } from 'lucide-react';

const MOCK_DEMANDES = [
  {
    id: 1,
    date_demande: '2024-05-01',
    demandeur_nom: 'RABEMANANJARA',
    demandeur_prenom: 'Andry',
    type_visa: 'Investisseur',
    type_demande: 'Nouveau titre',
    statut: 'Validee'
  },
  {
    id: 2,
    date_demande: '2024-05-02',
    demandeur_nom: 'ANDRIAMAHAVOLA',
    demandeur_prenom: 'Tahiana',
    type_visa: 'Travailleur',
    type_demande: 'Transfert de visa',
    statut: 'Pieces manquantes'
  },
  {
    id: 3,
    date_demande: '2024-05-03',
    demandeur_nom: 'RAZAFY',
    demandeur_prenom: 'Miora',
    type_visa: 'Investisseur',
    type_demande: 'Duplicata',
    statut: 'Titre delivre'
  },
  {
    id: 4,
    date_demande: '2024-05-04',
    demandeur_nom: 'SMITH',
    demandeur_prenom: 'John',
    type_visa: 'Travailleur',
    type_demande: 'Nouveau titre',
    statut: 'En cours de saisie'
  }
];

const DemandeList = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const filteredDemandes = MOCK_DEMANDES.filter(d => 
    d.demandeur_nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    d.demandeur_prenom.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="container">
      <header className="header">
        <div>
          <h1 className="title">Gestion des Demandes</h1>
          <p className="text-slate-400 mt-2">Gérez les demandes de visa et titres de séjour</p>
        </div>
        <button className="btn-primary flex items-center gap-2">
          <Plus size={20} /> Nouvelle Demande
        </button>
      </header>

      <div className="flex flex-col md:flex-row gap-4 mb-8">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-slate-500" size={18} />
          <input 
            type="text" 
            placeholder="Rechercher un demandeur..." 
            className="w-full bg-slate-800/50 border border-slate-700 rounded-lg py-2.5 pl-10 pr-4 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50 transition-all"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <button className="bg-slate-800 border border-slate-700 p-2.5 rounded-lg text-slate-400 hover:text-white transition-colors">
          <Filter size={20} />
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredDemandes.map(demande => (
          <DemandeCard key={demande.id} demande={demande} />
        ))}
      </div>
      
      {filteredDemandes.length === 0 && (
        <div className="text-center py-20">
          <p className="text-slate-500">Aucune demande trouvée pour "{searchTerm}"</p>
        </div>
      )}
    </div>
  );
};

export default DemandeList;
