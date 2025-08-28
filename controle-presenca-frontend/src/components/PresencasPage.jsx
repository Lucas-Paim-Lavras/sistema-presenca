import { useState, useEffect } from 'react'
import { Plus, Edit, Trash2, Search, Filter, CheckSquare, Clock } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { presencasAPI, turmasAPI, alunosAPI } from '../services/api'
import PresencaModal from './PresencaModal'

const PresencasPage = () => {
  const [presencas, setPresencas] = useState([])
  const [filteredPresencas, setFilteredPresencas] = useState([])
  const [turmas, setTurmas] = useState([])
  const [alunos, setAlunos] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedTurma, setSelectedTurma] = useState('')
  const [selectedData, setSelectedData] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editingPresenca, setEditingPresenca] = useState(null)

  useEffect(() => {
    carregarDados()
  }, [])

  useEffect(() => {
    // Filtrar presenças baseado nos filtros
    let filtered = presencas

    if (searchTerm) {
      filtered = filtered.filter(presenca =>
        presenca.alunoNome.toLowerCase().includes(searchTerm.toLowerCase()) ||
        presenca.alunoMatricula.toLowerCase().includes(searchTerm.toLowerCase()) ||
        presenca.turmaNome.toLowerCase().includes(searchTerm.toLowerCase())
      )
    }

    if (selectedTurma) {
      filtered = filtered.filter(presenca => presenca.turmaId.toString() === selectedTurma)
    }

    if (selectedData) {
      filtered = filtered.filter(presenca => presenca.dataPresenca === selectedData)
    }

    setFilteredPresencas(filtered)
  }, [presencas, searchTerm, selectedTurma, selectedData])

  const carregarDados = async () => {
    try {
      setLoading(true)
      const [presencasData, turmasData, alunosData] = await Promise.all([
        presencasAPI.listar(),
        turmasAPI.listar(),
        alunosAPI.listar()
      ])
      setPresencas(presencasData)
      setTurmas(turmasData)
      setAlunos(alunosData)
    } catch (error) {
      console.error('Erro ao carregar dados:', error)
      alert('Erro ao carregar dados. Verifique se o backend está rodando.')
    } finally {
      setLoading(false)
    }
  }

  const handleNovaPresenca = () => {
    setEditingPresenca(null)
    setModalOpen(true)
  }

  const handleEditarPresenca = (presenca) => {
    setEditingPresenca(presenca)
    setModalOpen(true)
  }

  const handleExcluirPresenca = async (id, alunoNome, data) => {
    if (window.confirm(`Tem certeza que deseja remover a presença de "${alunoNome}" do dia ${formatarData(data)}?`)) {
      try {
        await presencasAPI.remover(id)
        await carregarDados()
        alert('Presença removida com sucesso!')
      } catch (error) {
        console.error('Erro ao remover presença:', error)
        alert('Erro ao remover presença.')
      }
    }
  }

  const handleRegistrarPresencaRapida = async (alunoId, turmaId) => {
    try {
      await presencasAPI.registrarRapida(alunoId, turmaId)
      await carregarDados()
      alert('Presença registrada com sucesso!')
    } catch (error) {
      console.error('Erro ao registrar presença:', error)
      alert('Erro ao registrar presença: ' + error.message)
    }
  }

  const handleSalvarPresenca = async (dadosPresenca) => {
    try {
      if (editingPresenca) {
        await presencasAPI.atualizar(editingPresenca.id, dadosPresenca)
        alert('Presença atualizada com sucesso!')
      } else {
        await presencasAPI.registrar(dadosPresenca)
        alert('Presença registrada com sucesso!')
      }
      setModalOpen(false)
      await carregarDados()
    } catch (error) {
      console.error('Erro ao salvar presença:', error)
      alert('Erro ao salvar presença: ' + error.message)
    }
  }

  const formatarData = (data) => {
    return new Date(data + 'T00:00:00').toLocaleDateString('pt-BR')
  }

  const formatarHora = (hora) => {
    return hora.substring(0, 5) // Remove os segundos
  }

  const limparFiltros = () => {
    setSearchTerm('')
    setSelectedTurma('')
    setSelectedData('')
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Presenças</h1>
          <p className="mt-1 text-sm text-gray-500">
            Gerencie os registros de presença
          </p>
        </div>
        <Button onClick={handleNovaPresenca} className="flex items-center space-x-2">
          <Plus className="h-4 w-4" />
          <span>Nova Presença</span>
        </Button>
      </div>

      {/* Filtros */}
      <div className="bg-white p-4 rounded-lg shadow space-y-4">
        <div className="flex flex-col lg:flex-row items-center space-y-4 lg:space-y-0 lg:space-x-4">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
            <Input
              type="text"
              placeholder="Buscar por aluno ou turma..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10"
            />
          </div>
          <div className="flex flex-col sm:flex-row items-center space-y-2 sm:space-y-0 sm:space-x-2">
            <div className="flex items-center space-x-2">
              <Filter className="h-4 w-4 text-gray-400" />
              <Select value={selectedTurma || "all"} onValueChange={(value) => setSelectedTurma(value === "all" ? "" : value)}>
                <SelectTrigger className="w-48">
                  <SelectValue placeholder="Filtrar por turma" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todas as turmas</SelectItem>
                  {turmas.map((turma) => (
                    <SelectItem key={turma.id} value={turma.id.toString()}>
                      {turma.nome}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <Input
              type="date"
              value={selectedData}
              onChange={(e) => setSelectedData(e.target.value)}
              className="w-48"
            />
            <Button variant="outline" onClick={limparFiltros}>
              Limpar
            </Button>
          </div>
        </div>
      </div>

      {/* Registro rápido de presença */}
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
        <h3 className="text-sm font-medium text-blue-900 mb-2">Registro Rápido de Presença</h3>
        <p className="text-xs text-blue-700 mb-3">
          Selecione uma turma para ver os alunos e registrar presenças rapidamente
        </p>
        <div className="flex items-center space-x-4">
          <Select value={selectedTurma} onValueChange={setSelectedTurma}>
            <SelectTrigger className="w-64">
              <SelectValue placeholder="Selecione uma turma" />
            </SelectTrigger>
            <SelectContent>
              {turmas.map((turma) => (
                <SelectItem key={turma.id} value={turma.id.toString()}>
                  {turma.nome} ({turma.codigo})
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        {selectedTurma && (
          <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-2">
            {alunos
              .filter(aluno => aluno.turmaId.toString() === selectedTurma)
              .map((aluno) => (
                <div key={aluno.id} className="flex items-center justify-between bg-white p-2 rounded border">
                  <div className="flex-1">
                    <div className="text-sm font-medium">{aluno.nome}</div>
                    <div className="text-xs text-gray-500">{aluno.matricula}</div>
                  </div>
                  <Button
                    size="sm"
                    onClick={() => handleRegistrarPresencaRapida(aluno.id, parseInt(selectedTurma))}
                    className="flex items-center space-x-1"
                  >
                    <CheckSquare className="h-3 w-3" />
                    <span>Registrar</span>
                  </Button>
                </div>
              ))}
          </div>
        )}
      </div>

      {/* Lista de presenças */}
      <div className="bg-white shadow rounded-lg">
        {loading ? (
          <div className="p-6 text-center">
            <div className="text-gray-500">Carregando presenças...</div>
          </div>
        ) : filteredPresencas.length === 0 ? (
          <div className="p-6 text-center">
            <div className="text-gray-500">
              {searchTerm || selectedTurma || selectedData ? 'Nenhuma presença encontrada.' : 'Nenhuma presença registrada.'}
            </div>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Data/Hora
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Aluno
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Turma
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Observações
                  </th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Ações
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredPresencas.map((presenca) => (
                  <tr key={presenca.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <Clock className="h-4 w-4 text-gray-400 mr-2" />
                        <div>
                          <div className="text-sm font-medium text-gray-900">
                            {formatarData(presenca.dataPresenca)}
                          </div>
                          <div className="text-sm text-gray-500">
                            {formatarHora(presenca.horaPresenca)}
                          </div>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {presenca.alunoNome}
                      </div>
                      <div className="text-sm text-gray-500">
                        {presenca.alunoMatricula}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">
                        {presenca.turmaNome}
                      </div>
                      <div className="text-sm text-gray-500">
                        {presenca.turmaCodigo}
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="text-sm text-gray-500 max-w-xs truncate">
                        {presenca.observacoes || '-'}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <div className="flex items-center justify-end space-x-2">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleEditarPresenca(presenca)}
                        >
                          <Edit className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleExcluirPresenca(presenca.id, presenca.alunoNome, presenca.dataPresenca)}
                          className="text-red-600 hover:text-red-900"
                        >
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Modal para criar/editar presença */}
      {modalOpen && (
        <PresencaModal
          presenca={editingPresenca}
          turmas={turmas}
          alunos={alunos}
          onSave={handleSalvarPresenca}
          onClose={() => setModalOpen(false)}
        />
      )}
    </div>
  )
}

export default PresencasPage

