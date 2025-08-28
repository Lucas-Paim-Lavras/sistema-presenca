import { useState, useEffect } from 'react'
import { Plus, Edit, Trash2, Search, Users, CheckSquare } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { turmasAPI } from '../services/api'
import TurmaModal from './TurmaModal'

const TurmasPage = () => {
  const [turmas, setTurmas] = useState([])
  const [filteredTurmas, setFilteredTurmas] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editingTurma, setEditingTurma] = useState(null)

  useEffect(() => {
    carregarTurmas()
  }, [])

  useEffect(() => {
    // Filtrar turmas baseado no termo de busca
    const filtered = turmas.filter(turma =>
      turma.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
      turma.codigo.toLowerCase().includes(searchTerm.toLowerCase())
    )
    setFilteredTurmas(filtered)
  }, [turmas, searchTerm])

  const carregarTurmas = async () => {
    try {
      setLoading(true)
      const data = await turmasAPI.listar()
      setTurmas(data)
    } catch (error) {
      console.error('Erro ao carregar turmas:', error)
      alert('Erro ao carregar turmas. Verifique se o backend está rodando.')
    } finally {
      setLoading(false)
    }
  }

  const handleNovaTurma = () => {
    setEditingTurma(null)
    setModalOpen(true)
  }

  const handleEditarTurma = (turma) => {
    setEditingTurma(turma)
    setModalOpen(true)
  }

  const handleExcluirTurma = async (id, nome) => {
    if (window.confirm(`Tem certeza que deseja remover a turma "${nome}"?`)) {
      try {
        await turmasAPI.remover(id)
        await carregarTurmas()
        alert('Turma removida com sucesso!')
      } catch (error) {
        console.error('Erro ao remover turma:', error)
        alert('Erro ao remover turma.')
      }
    }
  }

  const handleSalvarTurma = async (dadosTurma) => {
    try {
      if (editingTurma) {
        await turmasAPI.atualizar(editingTurma.id, dadosTurma)
        alert('Turma atualizada com sucesso!')
      } else {
        await turmasAPI.criar(dadosTurma)
        alert('Turma criada com sucesso!')
      }
      setModalOpen(false)
      await carregarTurmas()
    } catch (error) {
      console.error('Erro ao salvar turma:', error)
      alert('Erro ao salvar turma: ' + error.message)
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Turmas</h1>
          <p className="mt-1 text-sm text-gray-500">
            Gerencie as turmas do sistema
          </p>
        </div>
        <Button onClick={handleNovaTurma} className="flex items-center space-x-2">
          <Plus className="h-4 w-4" />
          <span>Nova Turma</span>
        </Button>
      </div>

      {/* Barra de busca */}
      <div className="flex items-center space-x-4">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
          <Input
            type="text"
            placeholder="Buscar por nome ou código..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>
      </div>

      {/* Lista de turmas */}
      <div className="bg-white shadow rounded-lg">
        {loading ? (
          <div className="p-6 text-center">
            <div className="text-gray-500">Carregando turmas...</div>
          </div>
        ) : filteredTurmas.length === 0 ? (
          <div className="p-6 text-center">
            <div className="text-gray-500">
              {searchTerm ? 'Nenhuma turma encontrada.' : 'Nenhuma turma cadastrada.'}
            </div>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Turma
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Código
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Descrição
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Estatísticas
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
                {filteredTurmas.map((turma) => (
                  <tr key={turma.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {turma.nome}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-500">
                        {turma.codigo}
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="text-sm text-gray-500 max-w-xs truncate">
                        {turma.descricao || '-'}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center space-x-4 text-sm text-gray-500">
                        <div className="flex items-center">
                          <Users className="h-4 w-4 mr-1" />
                          {turma.totalAlunos || 0}
                        </div>
                        <div className="flex items-center">
                          <CheckSquare className="h-4 w-4 mr-1" />
                          {turma.totalPresencas || 0}
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                        turma.ativa 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {turma.ativa ? 'Ativa' : 'Inativa'}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <div className="flex items-center justify-end space-x-2">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleEditarTurma(turma)}
                        >
                          <Edit className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleExcluirTurma(turma.id, turma.nome)}
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

      {/* Modal para criar/editar turma */}
      {modalOpen && (
        <TurmaModal
          turma={editingTurma}
          onSave={handleSalvarTurma}
          onClose={() => setModalOpen(false)}
        />
      )}
    </div>
  )
}

export default TurmasPage

