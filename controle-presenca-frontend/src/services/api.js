// Configuração da API
const API_BASE_URL = 'http://localhost:8080/api'

// Função auxiliar para fazer requisições
const apiRequest = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`
  
  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  }

  try {
    const response = await fetch(url, config)
    
    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(errorText || `HTTP error! status: ${response.status}`)
    }

    // Se a resposta for vazia, retorna null
    const contentType = response.headers.get('content-type')
    if (!contentType || !contentType.includes('application/json')) {
      return null
    }

    return await response.json()
  } catch (error) {
    console.error('API Error:', error)
    throw error
  }
}

// Serviços para Turmas
export const turmasAPI = {
  // Listar todas as turmas ativas
  listar: () => apiRequest('/turmas'),
  
  // Listar todas as turmas (incluindo inativas)
  listarTodas: () => apiRequest('/turmas/todas'),
  
  // Buscar turma por ID
  buscarPorId: (id) => apiRequest(`/turmas/${id}`),
  
  // Buscar turma por código
  buscarPorCodigo: (codigo) => apiRequest(`/turmas/codigo/${codigo}`),
  
  // Buscar turmas por nome
  buscarPorNome: (nome) => apiRequest(`/turmas/buscar?nome=${encodeURIComponent(nome)}`),
  
  // Criar nova turma
  criar: (turma) => apiRequest('/turmas', {
    method: 'POST',
    body: JSON.stringify(turma),
  }),
  
  // Atualizar turma
  atualizar: (id, turma) => apiRequest(`/turmas/${id}`, {
    method: 'PUT',
    body: JSON.stringify(turma),
  }),
  
  // Remover turma (soft delete)
  remover: (id) => apiRequest(`/turmas/${id}`, {
    method: 'DELETE',
  }),
  
  // Excluir turma permanentemente
  excluir: (id) => apiRequest(`/turmas/${id}/permanente`, {
    method: 'DELETE',
  }),
}

// Serviços para Alunos
export const alunosAPI = {
  // Listar todos os alunos ativos
  listar: () => apiRequest('/alunos'),
  
  // Listar todos os alunos (incluindo inativos)
  listarTodos: () => apiRequest('/alunos/todos'),
  
  // Listar alunos por turma
  listarPorTurma: (turmaId) => apiRequest(`/alunos/turma/${turmaId}`),
  
  // Buscar aluno por ID
  buscarPorId: (id) => apiRequest(`/alunos/${id}`),
  
  // Buscar aluno por matrícula
  buscarPorMatricula: (matricula) => apiRequest(`/alunos/matricula/${matricula}`),
  
  // Buscar aluno por email
  buscarPorEmail: (email) => apiRequest(`/alunos/email/${encodeURIComponent(email)}`),
  
  // Buscar alunos por nome
  buscarPorNome: (nome) => apiRequest(`/alunos/buscar?nome=${encodeURIComponent(nome)}`),
  
  // Buscar alunos por turma e nome
  buscarPorTurmaENome: (turmaId, nome) => apiRequest(`/alunos/turma/${turmaId}/buscar?nome=${encodeURIComponent(nome)}`),
  
  // Criar novo aluno
  criar: (aluno) => apiRequest('/alunos', {
    method: 'POST',
    body: JSON.stringify(aluno),
  }),
  
  // Atualizar aluno
  atualizar: (id, aluno) => apiRequest(`/alunos/${id}`, {
    method: 'PUT',
    body: JSON.stringify(aluno),
  }),
  
  // Remover aluno (soft delete)
  remover: (id) => apiRequest(`/alunos/${id}`, {
    method: 'DELETE',
  }),
  
  // Excluir aluno permanentemente
  excluir: (id) => apiRequest(`/alunos/${id}/permanente`, {
    method: 'DELETE',
  }),
}

// Serviços para Presenças
export const presencasAPI = {
  // Listar todas as presenças
  listar: () => apiRequest('/presencas'),
  
  // Listar presenças por turma
  listarPorTurma: (turmaId) => apiRequest(`/presencas/turma/${turmaId}`),
  
  // Listar presenças por aluno
  listarPorAluno: (alunoId) => apiRequest(`/presencas/aluno/${alunoId}`),
  
  // Listar presenças por data
  listarPorData: (data) => apiRequest(`/presencas/data/${data}`),
  
  // Listar presenças por turma e data
  listarPorTurmaEData: (turmaId, data) => apiRequest(`/presencas/turma/${turmaId}/data/${data}`),
  
  // Listar presenças por período
  listarPorPeriodo: (dataInicio, dataFim) => apiRequest(`/presencas/periodo?dataInicio=${dataInicio}&dataFim=${dataFim}`),
  
  // Buscar presença por ID
  buscarPorId: (id) => apiRequest(`/presencas/${id}`),
  
  // Registrar nova presença
  registrar: (presenca) => apiRequest('/presencas', {
    method: 'POST',
    body: JSON.stringify(presenca),
  }),
  
  // Registrar presença rápida (data/hora atual)
  registrarRapida: (alunoId, turmaId) => apiRequest('/presencas/rapida', {
    method: 'POST',
    body: JSON.stringify({ alunoId, turmaId }),
  }),
  
  // Atualizar presença
  atualizar: (id, presenca) => apiRequest(`/presencas/${id}`, {
    method: 'PUT',
    body: JSON.stringify(presenca),
  }),
  
  // Remover presença
  remover: (id) => apiRequest(`/presencas/${id}`, {
    method: 'DELETE',
  }),
  
  // Gerar relatório
  gerarRelatorio: (turmaId, dataInicio, dataFim) => {
    const params = new URLSearchParams()
    if (turmaId) params.append('turmaId', turmaId)
    if (dataInicio) params.append('dataInicio', dataInicio)
    if (dataFim) params.append('dataFim', dataFim)
    
    return apiRequest(`/presencas/relatorio?${params.toString()}`)
  },
  
  // Contar presenças por turma
  contarPorTurma: (turmaId) => apiRequest(`/presencas/turma/${turmaId}/contar`),
  
  // Contar presenças por aluno
  contarPorAluno: (alunoId) => apiRequest(`/presencas/aluno/${alunoId}/contar`),
}

// Serviços para Relatórios
export const relatoriosAPI = {
  // Exportar presenças em CSV
  exportarPresencasCSV: async (turmaId, dataInicio, dataFim) => {
    const params = new URLSearchParams()
    if (turmaId) params.append('turmaId', turmaId)
    if (dataInicio) params.append('dataInicio', dataInicio)
    if (dataFim) params.append('dataFim', dataFim)
    
    const response = await fetch(`${API_BASE_URL}/relatorios/presencas/csv?${params.toString()}`)
    if (!response.ok) throw new Error('Erro ao exportar CSV')
    
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `relatorio-presencas-${new Date().toISOString().split('T')[0]}.csv`
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    window.URL.revokeObjectURL(url)
  },
  
  // Exportar presenças em Excel
  exportarPresencasExcel: async (turmaId, dataInicio, dataFim) => {
    const params = new URLSearchParams()
    if (turmaId) params.append('turmaId', turmaId)
    if (dataInicio) params.append('dataInicio', dataInicio)
    if (dataFim) params.append('dataFim', dataFim)
    
    const response = await fetch(`${API_BASE_URL}/relatorios/presencas/excel?${params.toString()}`)
    if (!response.ok) throw new Error('Erro ao exportar Excel')
    
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `relatorio-presencas-${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    window.URL.revokeObjectURL(url)
  },
  
  // Exportar alunos em CSV
  exportarAlunosCSV: async (turmaId) => {
    const params = new URLSearchParams()
    if (turmaId) params.append('turmaId', turmaId)
    
    const response = await fetch(`${API_BASE_URL}/relatorios/alunos/csv?${params.toString()}`)
    if (!response.ok) throw new Error('Erro ao exportar CSV')
    
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `relatorio-alunos-${new Date().toISOString().split('T')[0]}.csv`
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    window.URL.revokeObjectURL(url)
  },
  
  // Exportar alunos em Excel
  exportarAlunosExcel: async (turmaId) => {
    const params = new URLSearchParams()
    if (turmaId) params.append('turmaId', turmaId)
    
    const response = await fetch(`${API_BASE_URL}/relatorios/alunos/excel?${params.toString()}`)
    if (!response.ok) throw new Error('Erro ao exportar Excel')
    
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `relatorio-alunos-${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    window.URL.revokeObjectURL(url)
  },
  
  // Exportar turmas em CSV
  exportarTurmasCSV: async () => {
    const response = await fetch(`${API_BASE_URL}/relatorios/turmas/csv`)
    if (!response.ok) throw new Error('Erro ao exportar CSV')
    
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `relatorio-turmas-${new Date().toISOString().split('T')[0]}.csv`
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    window.URL.revokeObjectURL(url)
  },
  
  // Exportar turmas em Excel
  exportarTurmasExcel: async () => {
    const response = await fetch(`${API_BASE_URL}/relatorios/turmas/excel`)
    if (!response.ok) throw new Error('Erro ao exportar Excel')
    
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `relatorio-turmas-${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    window.URL.revokeObjectURL(url)
  },
}


// Serviços para Mentores
export const mentoresAPI = {
  // Listar todos os mentores ativos
  listar: () => apiRequest('/mentores'),
  
  // Listar todos os mentores (incluindo inativos)
  listarTodos: () => apiRequest('/mentores/todos'),
  
  // Listar mentores ativos
  listarAtivos: () => apiRequest('/mentores/ativos'),
  
  // Listar mentores por tipo
  listarPorTipo: (tipo) => apiRequest(`/mentores/tipo/${tipo}`),
  
  // Buscar mentor por ID
  buscarPorId: (id) => apiRequest(`/mentores/${id}`),
  
  // Buscar mentor por email
  buscarPorEmail: (email) => apiRequest(`/mentores/email/${encodeURIComponent(email)}`),
  
  // Criar novo mentor
  criar: (mentor) => apiRequest('/mentores', {
    method: 'POST',
    body: JSON.stringify(mentor),
  }),
  
  // Atualizar mentor
  atualizar: (id, mentor) => apiRequest(`/mentores/${id}`, {
    method: 'PUT',
    body: JSON.stringify(mentor),
  }),
  
  // Remover mentor (soft delete)
  remover: (id) => apiRequest(`/mentores/${id}`, {
    method: 'DELETE',
  }),
}

// Serviços para Chamadas de Mentores
export const chamadasMentoresAPI = {
  // Listar todas as chamadas de mentores
  listar: () => apiRequest('/chamadas-mentores'),
  
  // Buscar chamada por ID
  buscarPorId: (id) => apiRequest(`/chamadas-mentores/${id}`),
  
  // Buscar chamada por data
  buscarPorData: (data) => apiRequest(`/chamadas-mentores/data/${data}`),
  
  // Listar chamadas por período
  listarPorPeriodo: (dataInicio, dataFim) => apiRequest(`/chamadas-mentores/periodo?dataInicio=${dataInicio}&dataFim=${dataFim}`),
  
  // Criar nova chamada
  criar: (chamada) => apiRequest('/chamadas-mentores', {
    method: 'POST',
    body: JSON.stringify(chamada),
  }),
  
  // Atualizar chamada
  atualizar: (id, chamada) => apiRequest(`/chamadas-mentores/${id}`, {
    method: 'PUT',
    body: JSON.stringify(chamada),
  }),
  
  // Remover chamada
  remover: (id) => apiRequest(`/chamadas-mentores/${id}`, {
    method: 'DELETE',
  }),
  
  // Obter estatísticas
  obterEstatisticas: () => apiRequest('/chamadas-mentores/estatisticas'),
}

// Objeto principal da API para compatibilidade
export const api = {
  get: (endpoint) => apiRequest(endpoint),
  post: (endpoint, data) => apiRequest(endpoint, {
    method: 'POST',
    body: JSON.stringify(data),
  }),
  put: (endpoint, data) => apiRequest(endpoint, {
    method: 'PUT',
    body: JSON.stringify(data),
  }),
  delete: (endpoint) => apiRequest(endpoint, {
    method: 'DELETE',
  }),
  
  // Serviços específicos
  turmas: turmasAPI,
  alunos: alunosAPI,
  presencas: presencasAPI,
  mentores: mentoresAPI,
  chamadasMentores: chamadasMentoresAPI,
  relatorios: relatoriosAPI,
}

