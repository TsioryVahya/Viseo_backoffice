import React from 'react';
import DemandeList from './components/DemandeList';
import './index.css';

function App() {
  return (
    <div className="min-h-screen">
      <div className="fixed top-0 left-0 w-full h-1 bg-gradient-to-right from-indigo-500 via-purple-500 to-pink-500 z-50"></div>
      
      {/* Background Orbs for atmosphere */}
      <div className="fixed top-[-10%] right-[-10%] w-[40%] h-[40%] bg-indigo-600/20 rounded-full blur-[120px] pointer-events-none"></div>
      <div className="fixed bottom-[-10%] left-[-10%] w-[30%] h-[30%] bg-purple-600/10 rounded-full blur-[100px] pointer-events-none"></div>

      <DemandeList />

      <footer className="container py-8 mt-20 border-t border-slate-800 text-center text-slate-500 text-sm">
        <p>© 2024 Viseo Backoffice - Système de Gestion des Visas</p>
      </footer>
    </div>
  );
}

export default App;
