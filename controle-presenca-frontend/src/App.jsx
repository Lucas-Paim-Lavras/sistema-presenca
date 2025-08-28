import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard.jsx';
import TurmasPage from './components/TurmasPage.jsx';        // ← Correto
import AlunosPage from './components/AlunosPage.jsx';        // ← Correto  
import PresencasPage from './components/PresencasPage.jsx';  // ← Correto
import Layout from './components/Layout.jsx';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Dashboard />} />
          <Route path="turmas" element={<TurmasPage />} />      {/* ← Correto */}
          <Route path="alunos" element={<AlunosPage />} />      {/* ← Correto */}
          <Route path="presencas" element={<PresencasPage />} />{/* ← Correto */}
          <Route path="*" element={<div>Página não encontrada</div>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;