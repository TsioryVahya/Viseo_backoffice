import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import DemandeList from './components/DemandeList';
import DemandeDetail from './components/DemandeDetail';
import './index.css';

function App() {
  return (
    <Router>
      <div className="flex flex-col min-h-screen">
        <main className="flex-1 w-full max-w-7xl mx-auto p-4 md:p-8">
          <Routes>
            <Route path="/" element={<DemandeList />} />
            <Route path="/demande/:id" element={<DemandeDetail />} />
          </Routes>
        </main>
        
        <footer className="py-8 text-center text-gris text-sm border-t border-sand-dark mt-auto bg-white/50">
          <p>© 2024 Viseo Backoffice - Système de Gestion des Visas</p>
        </footer>
      </div>
    </Router>
  );
}

export default App;
