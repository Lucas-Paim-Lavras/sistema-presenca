import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Plus, Search, Edit, Trash2, UserCheck } from 'lucide-react'
import { api } from '../services/api'
import MentorModal from './MentorModal'

const MentoresPage = () => {
  const [mentores, setMentores] = useState([])
  const [filteredMentores, setFilteredMentores] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedTipo, setSelectedTipo] = useState('all')
  const [modalOpen, setModalOpen] = useState(false)
  const [editingMentor, setEditingMentor] = useState(null)

  const tiposMentor = [
    { value: 'MENTOR', label: 'Mentor' },
    { value: 'MENTOR_TRAINEE', label: 'Mentor-trainee' },
    { value: 'MENTOR_COORDENADOR', label: 'Mentor Coordenador' }
  ]

  useEffect(() => {
    carregarMentores()
  }, [])

  useEffect(() => {
    filtrarMentores()
  }, [mentores, searchTerm, selectedTipo])

  const carregarMentores = async () => {
    try {
      setLoading(true)
      const response = await api.get('/mentores')
      setMentores(response.data)
    } catch (error) {
      console.error('Erro ao carregar mentores:', error)
      alert('Erro ao carregar mentores. Verifique se o backend está rodando.')
    } finally {
      setLoading(false)
    }
  }

  const filtrarMentores = () => {
    let filtered = mentores

    if (searchTerm) {
      filtered = filtered.filter(mentor =>
        mentor.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
        mentor.email.toLowerCase().includes(searchTerm.toLowerCase())
      )
    }

    if (selectedTipo !== 'all') {
      filtered = filtered.filter(mentor => mentor.tipoMentor === selectedTipo)
    }

    setFilteredMentores(filtered)
  }

  const handleTipoChange = (value) => {
    setSelectedTipo(value)
  }

  const handleEdit = (mentor) => {
    setEditingMentor(mentor)
    setModalOpen(true)
  }

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja remover este mentor?')) {
      try {
        await api.delete(`/mentores/${id}`)
        await carregarMentores()
        alert('Mentor removido com sucesso!')
      } catch (error) {
        console.error('Erro ao remover mentor:', error)
        alert('Erro ao remover mentor.')
      }
    }
  }

  const handleModalClose = () => {
    setModalOpen(false)
    setEditingMentor(null)
  }

  const handleModalSuccess = () => {
    carregarMentores()
    handleModalClose()
  }

  const getTipoBadgeColor = (tipo) => {
    switch (tipo) {
      case 'MENTOR':
        return 'bg-blue-100 text-blue-800'
      case 'MENTOR_TRAINEE':
        return 'bg-green-100 text-green-800'
      case 'MENTOR_COORDENADOR':
        return 'bg-purple-100 text-purple-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-lg">Carregando mentores...</div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Mentores</h1>
          <p className="text-gray-600">Gerencie os mentores do YouX Lab</p>
        </div>
        <Button onClick={() => setModalOpen(true)} className="flex items-center gap-2">
          <Plus className="h-4 w-4" />
          Novo Mentor
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
                  placeholder="Buscar por nome ou email..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="w-full sm:w-48">
              <Select value={selectedTipo} onValueChange={handleTipoChange}>
                <SelectTrigger>
                  <SelectValue placeholder="Filtrar por tipo" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todos os tipos</SelectItem>
                  {tiposMentor.map((tipo) => (
                    <SelectItem key={tipo.value} value={tipo.value}>
                      {tipo.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Lista de Mentores */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {filteredMentores.map((mentor) => (
          <Card key={mentor.id} className="hover:shadow-md transition-shadow">
            <CardHeader className="pb-3">
              <div className="flex items-start justify-between">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-blue-100 rounded-lg">
                    <UserCheck className="h-5 w-5 text-blue-600" />
                  </div>
                  <div>
                    <CardTitle className="text-lg">{mentor.nome}</CardTitle>
                    <p className="text-sm text-gray-600">{mentor.email}</p>
                  </div>
                </div>
              </div>
            </CardHeader>
            <CardContent className="pt-0">
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">Tipo:</span>
                  <Badge className={getTipoBadgeColor(mentor.tipoMentor)}>
                    {mentor.tipoMentorDescricao}
                  </Badge>
                </div>
                
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">Presenças:</span>
                  <span className="text-sm font-medium">{mentor.totalPresencas || 0}</span>
                </div>
                
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">Status:</span>
                  <Badge variant={mentor.ativo ? "default" : "secondary"}>
                    {mentor.ativo ? 'Ativo' : 'Inativo'}
                  </Badge>
                </div>

                <div className="flex gap-2 pt-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleEdit(mentor)}
                    className="flex-1"
                  >
                    <Edit className="h-4 w-4 mr-1" />
                    Editar
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleDelete(mentor.id)}
                    className="text-red-600 hover:text-red-700 hover:bg-red-50"
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredMentores.length === 0 && (
        <Card>
          <CardContent className="text-center py-8">
            <UserCheck className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">Nenhum mentor encontrado</h3>
            <p className="text-gray-600 mb-4">
              {searchTerm || selectedTipo !== 'all'
                ? 'Tente ajustar os filtros de busca.'
                : 'Comece adicionando o primeiro mentor.'}
            </p>
            {!searchTerm && selectedTipo === 'all' && (
              <Button onClick={() => setModalOpen(true)}>
                <Plus className="h-4 w-4 mr-2" />
                Adicionar Mentor
              </Button>
            )}
          </CardContent>
        </Card>
      )}

      {/* Modal */}
      <MentorModal
        open={modalOpen}
        onClose={handleModalClose}
        onSuccess={handleModalSuccess}
        mentor={editingMentor}
      />
    </div>
  )
}

export default MentoresPage

