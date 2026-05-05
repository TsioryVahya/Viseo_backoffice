import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import DemandeList from './components/DemandeList';
import DemandeDetail from './components/DemandeDetail';
import './index.css';

function App() {
  return (
    <Router>
      <div className="min-h-screen flex flex-col">
        {/* Top border bar like the sidebar color */}
        <div className="h-1 bg-[#3E5F44] w-full"></div>
        
        <main className="flex-1">
          <Routes>
            <Route path="/" element={<DemandeList />} />
            <Route path="/demande/:id" element={<DemandeDetail />} />
          </Routes>
        </main>

        <footer className="footer container">
          <p>© 2024 Viseo Backoffice - Système de Gestion des Visas</p>
        </footer>
      </div>
    </Router>
  );
}

export default App;
