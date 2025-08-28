import { useState, useEffect } from 'react'
import { Plus, Edit, Trash2, Search, Filter } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { alunosAPI, turmasAPI } from '../services/api'
import AlunoModal from './AlunoModal'

const AlunosPage = () => {
  const [alunos, setAlunos] = useState([])
  const [filteredAlunos, setFilteredAlunos] = useState([])
  const [turmas, setTurmas] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedTurma, setSelectedTurma] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editingAluno, setEditingAluno] = useState(null)

  useEffect(() => {
    carregarDados()
  }, [])

  useEffect(() => {
    // Filtrar alunos baseado no termo de busca e turma selecionada
    let filtered = alunos

    if (searchTerm) {
      filtered = filtered.filter(aluno =>
        aluno.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
        aluno.matricula.toLowerCase().includes(searchTerm.toLowerCase()) ||
        aluno.email.toLowerCase().includes(searchTerm.toLowerCase())
      )
    }

    if (selectedTurma) {
      filtered = filtered.filter(aluno => aluno.turmaId.toString() === selectedTurma)
    }

    setFilteredAlunos(filtered)
  }, [alunos, searchTerm, selectedTurma])

  const carregarDados = async () => {
    try {
      setLoading(true)
      const [alunosData, turmasData] = await Promise.all([
        alunosAPI.listar(),
        turmasAPI.listar()
      ])
      setAlunos(alunosData)
      setTurmas(turmasData)
    } catch (error) {
      console.error('Erro ao carregar dados:', error)
      alert('Erro ao carregar dados. Verifique se o backend está rodando.')
    } finally {
      setLoading(false)
    }
  }

  const handleNovoAluno = () => {
    setEditingAluno(null)
    setModalOpen(true)
  }

  const handleEditarAluno = (aluno) => {
    setEditingAluno(aluno)
    setModalOpen(true)
  }

  const handleExcluirAluno = async (id, nome) => {
    if (window.confirm(`Tem certeza que deseja remover o aluno "${nome}"?`)) {
      try {
        await alunosAPI.remover(id)
        await carregarDados()
        alert('Aluno removido com sucesso!')
      } catch (error) {
        console.error('Erro ao remover aluno:', error)
        alert('Erro ao remover aluno.')
      }
    }
  }

  const handleSalvarAluno = async (dadosAluno) => {
    try {
      if (editingAluno) {
        await alunosAPI.atualizar(editingAluno.id, dadosAluno)
        alert('Aluno atualizado com sucesso!')
      } else {
        await alunosAPI.criar(dadosAluno)
        alert('Aluno criado com sucesso!')
      }
      setModalOpen(false)
      await carregarDados()
    } catch (error) {
      console.error('Erro ao salvar aluno:', error)
      alert('Erro ao salvar aluno: ' + error.message)
    }
  }

  const getTurmaNome = (turmaId) => {
    const turma = turmas.find(t => t.id === turmaId)
    return turma ? turma.nome : 'Turma não encontrada'
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Alunos</h1>
          <p className="mt-1 text-sm text-gray-500">
            Gerencie os alunos do sistema
          </p>
        </div>
        <Button onClick={handleNovoAluno} className="flex items-center space-x-2">
          <Plus className="h-4 w-4" />
          <span>Novo Aluno</span>
        </Button>
      </div>

      {/* Filtros */}
      <div className="flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0 sm:space-x-4">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
          <Input
            type="text"
            placeholder="Buscar por nome, matrícula ou email..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>
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
      </div>

      {/* Lista de alunos */}
      <div className="bg-white shadow rounded-lg">
        {loading ? (
          <div className="p-6 text-center">
            <div className="text-gray-500">Carregando alunos...</div>
          </div>
        ) : filteredAlunos.length === 0 ? (
          <div className="p-6 text-center">
            <div className="text-gray-500">
              {searchTerm || selectedTurma ? 'Nenhum aluno encontrado.' : 'Nenhum aluno cadastrado.'}
            </div>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Aluno
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Matrícula
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Email
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Turma
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Presenças
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Ações
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredAlunos.map((aluno) => (
                  <tr key={aluno.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {aluno.nome}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-500">
                        {aluno.matricula}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-500">
                        {aluno.email}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-500">
                        {aluno.turmaNome || getTurmaNome(aluno.turmaId)}
                      </div>
                      <div className="text-xs text-gray-400">
                        {aluno.turmaCodigo}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-500">
                        {aluno.totalPresencas || 0}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                        aluno.ativo 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {aluno.ativo ? 'Ativo' : 'Inativo'}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <div className="flex items-center justify-end space-x-2">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleEditarAluno(aluno)}
                        >
                          <Edit className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleExcluirAluno(aluno.id, aluno.nome)}
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

      {/* Modal para criar/editar aluno */}
      {modalOpen && (
        <AlunoModal
          aluno={editingAluno}
          turmas={turmas}
          onSave={handleSalvarAluno}
          onClose={() => setModalOpen(false)}
        />
      )}
    </div>
  )
}

export default AlunosPage

