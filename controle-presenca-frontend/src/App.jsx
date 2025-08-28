import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import Turmas from './components/Turmas';
import Alunos from './components/Alunos';
import Presencas from './components/Presencas';
import Layout from './components/Layout';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Dashboard />} />
          <Route path="turmas" element={<Turmas />} />
          <Route path="alunos" element={<Alunos />} />
          <Route path="presencas" element={<Presencas />} />
          <Route path="*" element={<div>Página não encontrada</div>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;