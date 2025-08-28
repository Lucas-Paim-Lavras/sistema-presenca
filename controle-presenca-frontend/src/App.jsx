import { useState } from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './App.css'

// Componentes
import Layout from './components/Layout'
import Dashboard from './components/Dashboard'
import TurmasPage from './components/TurmasPage'
import AlunosPage from './components/AlunosPage'
import PresencasPage from './components/PresencasPage'
import MentoresPage from './components/MentoresPage'
import ChamadasMentoresPage from './components/ChamadasMentoresPage'
import RelatoriosPage from './components/RelatoriosPage'

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/turmas" element={<TurmasPage />} />
          <Route path="/alunos" element={<AlunosPage />} />
          <Route path="/presencas" element={<PresencasPage />} />
          <Route path="/mentores" element={<MentoresPage />} />
          <Route path="/chamadas-mentores" element={<ChamadasMentoresPage />} />
          <Route path="/relatorios" element={<RelatoriosPage />} />
        </Routes>
      </Layout>
    </Router>
  )
}

export default App

