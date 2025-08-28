import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Calendar, Plus, Search, Edit, Trash2, Users, UserCheck, UserX } from 'lucide-react'
import { api } from '../services/api'
import ChamadaMentorModal from './ChamadaMentorModal'

const ChamadasMentoresPage = () => {
  const [chamadas, setChamadas] = useState([])
  const [filteredChamadas, setFilteredChamadas] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editingChamada, setEditingChamada] = useState(null)

  useEffect(() => {
    carregarChamadas()
  }, [])

  useEffect(() => {
    filtrarChamadas()
  }, [chamadas, searchTerm])

  const carregarChamadas = async () => {
    try {
      setLoading(true)
      const response = await api.get('/chamadas-mentores')
      setChamadas(response.data)
    } catch (error) {
      console.error('Erro ao carregar chamadas de mentores:', error)
      alert('Erro ao carregar chamadas. Verifique se o backend está rodando.')
    } finally {
      setLoading(false)
    }
  }

  const filtrarChamadas = () => {
    let filtered = chamadas

    if (searchTerm) {
      filtered = filtered.filter(chamada =>
        chamada.observacoes?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        formatarData(chamada.dataChamada).includes(searchTerm)
      )
    }

    setFilteredChamadas(filtered)
  }

  const formatarData = (data) => {
    return new Date(data + 'T00:00:00').toLocaleDateString('pt-BR')
  }

  const handleEdit = (chamada) => {
    setEditingChamada(chamada)
    setModalOpen(true)
  }

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja remover esta chamada?')) {
      try {
        await api.delete(`/chamadas-mentores/${id}`)
        await carregarChamadas()
        alert('Chamada removida com sucesso!')
      } catch (error) {
        console.error('Erro ao remover chamada:', error)
        alert('Erro ao remover chamada.')
      }
    }
  }

  const handleModalClose = () => {
    setModalOpen(false)
    setEditingChamada(null)
  }

  const handleModalSuccess = () => {
    carregarChamadas()
    handleModalClose()
  }

  const calcularPorcentagemPresenca = (totalPresentes, totalMentores) => {
    if (totalMentores === 0) return 0
    return Math.round((totalPresentes / totalMentores) * 100)
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-lg">Carregando chamadas...</div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Chamadas de Mentores</h1>
          <p className="text-gray-600">Gerencie as chamadas de presença dos mentores</p>
        </div>
        <Button onClick={() => setModalOpen(true)} className="flex items-center gap-2">
          <Plus className="h-4 w-4" />
          Nova Chamada
        </Button>
      </div>

      {/* Filtros */}
      <Card>
        <CardContent className="pt-6">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
                <Input
                  placeholder="Buscar por data ou observações..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Lista de Chamadas */}
      <div className="grid gap-4">
        {filteredChamadas.map((chamada) => (
          <Card key={chamada.id} className="hover:shadow-md transition-shadow">
            <CardHeader className="pb-3">
              <div className="flex items-start justify-between">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-blue-100 rounded-lg">
                    <Calendar className="h-5 w-5 text-blue-600" />
                  </div>
                  <div>
                    <CardTitle className="text-lg">
                      Chamada de {formatarData(chamada.dataChamada)}
                    </CardTitle>
                    {chamada.observacoes && (
                      <p className="text-sm text-gray-600 mt-1">{chamada.observacoes}</p>
                    )}
                  </div>
                </div>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleEdit(chamada)}
                  >
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleDelete(chamada.id)}
                    className="text-red-600 hover:text-red-700 hover:bg-red-50"
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            </CardHeader>
            <CardContent className="pt-0">
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div className="flex items-center gap-2">
                  <Users className="h-4 w-4 text-gray-500" />
                  <div>
                    <p className="text-sm text-gray-600">Total</p>
                    <p className="font-semibold">{chamada.totalMentores || 0}</p>
                  </div>
                </div>
                
                <div className="flex items-center gap-2">
                  <UserCheck className="h-4 w-4 text-green-500" />
                  <div>
                    <p className="text-sm text-gray-600">Presentes</p>
                    <p className="font-semibold text-green-600">{chamada.totalPresentes || 0}</p>
                  </div>
                </div>
                
                <div className="flex items-center gap-2">
                  <UserX className="h-4 w-4 text-red-500" />
                  <div>
                    <p className="text-sm text-gray-600">Ausentes</p>
                    <p className="font-semibold text-red-600">{chamada.totalAusentes || 0}</p>
                  </div>
                </div>
                
                <div className="flex items-center gap-2">
                  <div className="h-4 w-4 rounded-full bg-blue-500"></div>
                  <div>
                    <p className="text-sm text-gray-600">Presença</p>
                    <Badge variant="outline">
                      {calcularPorcentagemPresenca(chamada.totalPresentes, chamada.totalMentores)}%
                    </Badge>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredChamadas.length === 0 && (
        <Card>
          <CardContent className="text-center py-8">
            <Calendar className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">Nenhuma chamada encontrada</h3>
            <p className="text-gray-600 mb-4">
              {searchTerm
                ? 'Tente ajustar os filtros de busca.'
                : 'Comece criando a primeira chamada de mentores.'}
            </p>
            {!searchTerm && (
              <Button onClick={() => setModalOpen(true)}>
                <Plus className="h-4 w-4 mr-2" />
                Criar Chamada
              </Button>
            )}
          </CardContent>
        </Card>
      )}

      {/* Modal */}
      <ChamadaMentorModal
        open={modalOpen}
        onClose={handleModalClose}
        onSuccess={handleModalSuccess}
        chamada={editingChamada}
      />
    </div>
  )
}

export default ChamadasMentoresPage

