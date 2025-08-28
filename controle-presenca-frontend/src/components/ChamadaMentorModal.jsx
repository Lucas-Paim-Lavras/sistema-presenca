import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Checkbox } from '@/components/ui/checkbox'
import { Badge } from '@/components/ui/badge'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { UserCheck, Users } from 'lucide-react'
import { api } from '../services/api'

const ChamadaMentorModal = ({ open, onClose, onSuccess, chamada }) => {
  const [formData, setFormData] = useState({
    dataChamada: '',
    observacoes: '',
    participantes: []
  })
  const [mentores, setMentores] = useState([])
  const [loading, setLoading] = useState(false)
  const [loadingMentores, setLoadingMentores] = useState(false)

  useEffect(() => {
    if (open) {
      carregarMentores()
      
      if (chamada) {
        // Edição - carregar dados da chamada
        setFormData({
          dataChamada: chamada.dataChamada || '',
          observacoes: chamada.observacoes || '',
          participantes: chamada.participantes || []
        })
      } else {
        // Nova chamada - usar data de hoje
        const hoje = new Date().toISOString().split('T')[0]
        setFormData({
          dataChamada: hoje,
          observacoes: '',
          participantes: []
        })
      }
    }
  }, [open, chamada])

  const carregarMentores = async () => {
    try {
      setLoadingMentores(true)
      const response = await api.get('/mentores/ativos')
      setMentores(response.data)
    } catch (error) {
      console.error('Erro ao carregar mentores:', error)
      alert('Erro ao carregar mentores.')
    } finally {
      setLoadingMentores(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!formData.dataChamada) {
      alert('Por favor, selecione uma data para a chamada.')
      return
    }

    try {
      setLoading(true)
      
      // Preparar dados para envio
      const dadosEnvio = {
        dataChamada: formData.dataChamada,
        observacoes: formData.observacoes,
        participantes: formData.participantes.map(p => ({
          mentorId: p.mentorId || p.mentor?.id,
          presente: p.presente
        }))
      }
      
      if (chamada) {
        await api.put(`/chamadas-mentores/${chamada.id}`, dadosEnvio)
        alert('Chamada atualizada com sucesso!')
      } else {
        await api.post('/chamadas-mentores', dadosEnvio)
        alert('Chamada criada com sucesso!')
      }
      
      onSuccess()
    } catch (error) {
      console.error('Erro ao salvar chamada:', error)
      if (error.response?.status === 400) {
        alert('Erro: Já existe uma chamada para esta data.')
      } else {
        alert('Erro ao salvar chamada. Tente novamente.')
      }
    } finally {
      setLoading(false)
    }
  }

  const handleChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }))
  }

  const handlePresencaChange = (mentorId, presente) => {
    setFormData(prev => {
      const participantesAtualizados = [...prev.participantes]
      const index = participantesAtualizados.findIndex(p => 
        (p.mentorId || p.mentor?.id) === mentorId
      )
      
      if (index >= 0) {
        participantesAtualizados[index] = {
          ...participantesAtualizados[index],
          presente
        }
      } else {
        // Adicionar novo participante
        participantesAtualizados.push({
          mentorId,
          presente
        })
      }
      
      return {
        ...prev,
        participantes: participantesAtualizados
      }
    })
  }

  const marcarTodosPresentes = () => {
    const novosParticipantes = mentores.map(mentor => ({
      mentorId: mentor.id,
      presente: true
    }))
    
    setFormData(prev => ({
      ...prev,
      participantes: novosParticipantes
    }))
  }

  const marcarTodosAusentes = () => {
    const novosParticipantes = mentores.map(mentor => ({
      mentorId: mentor.id,
      presente: false
    }))
    
    setFormData(prev => ({
      ...prev,
      participantes: novosParticipantes
    }))
  }

  const getPresencaStatus = (mentorId) => {
    const participante = formData.participantes.find(p => 
      (p.mentorId || p.mentor?.id) === mentorId
    )
    return participante?.presente || false
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

  const totalPresentes = formData.participantes.filter(p => p.presente).length
  const totalAusentes = formData.participantes.filter(p => !p.presente).length

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-4xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>
            {chamada ? 'Editar Chamada de Mentores' : 'Nova Chamada de Mentores'}
          </DialogTitle>
        </DialogHeader>
        
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="dataChamada">Data da Chamada *</Label>
              <Input
                id="dataChamada"
                type="date"
                value={formData.dataChamada}
                onChange={(e) => handleChange('dataChamada', e.target.value)}
                required
              />
            </div>
            
            <div className="space-y-2">
              <Label>Resumo</Label>
              <div className="flex gap-4 text-sm">
                <span className="text-green-600">Presentes: {totalPresentes}</span>
                <span className="text-red-600">Ausentes: {totalAusentes}</span>
                <span className="text-gray-600">Total: {mentores.length}</span>
              </div>
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="observacoes">Observações</Label>
            <Textarea
              id="observacoes"
              value={formData.observacoes}
              onChange={(e) => handleChange('observacoes', e.target.value)}
              placeholder="Observações sobre a chamada (opcional)"
              rows={3}
            />
          </div>

          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle className="flex items-center gap-2">
                  <Users className="h-5 w-5" />
                  Lista de Mentores
                </CardTitle>
                <div className="flex gap-2">
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    onClick={marcarTodosPresentes}
                  >
                    Marcar Todos Presentes
                  </Button>
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    onClick={marcarTodosAusentes}
                  >
                    Marcar Todos Ausentes
                  </Button>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              {loadingMentores ? (
                <div className="text-center py-4">Carregando mentores...</div>
              ) : (
                <div className="space-y-3 max-h-60 overflow-y-auto">
                  {mentores.map((mentor) => (
                    <div
                      key={mentor.id}
                      className="flex items-center justify-between p-3 border rounded-lg hover:bg-gray-50"
                    >
                      <div className="flex items-center gap-3">
                        <UserCheck className="h-5 w-5 text-gray-400" />
                        <div>
                          <p className="font-medium">{mentor.nome}</p>
                          <p className="text-sm text-gray-600">{mentor.email}</p>
                        </div>
                        <Badge className={getTipoBadgeColor(mentor.tipoMentor)}>
                          {mentor.tipoMentorDescricao}
                        </Badge>
                      </div>
                      
                      <div className="flex items-center gap-4">
                        <div className="flex items-center space-x-2">
                          <Checkbox
                            id={`presente-${mentor.id}`}
                            checked={getPresencaStatus(mentor.id)}
                            onCheckedChange={(checked) => 
                              handlePresencaChange(mentor.id, checked)
                            }
                          />
                          <Label 
                            htmlFor={`presente-${mentor.id}`}
                            className="text-sm font-medium"
                          >
                            Presente
                          </Label>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
              
              {mentores.length === 0 && !loadingMentores && (
                <div className="text-center py-4 text-gray-600">
                  Nenhum mentor ativo encontrado.
                </div>
              )}
            </CardContent>
          </Card>

          <div className="flex justify-end gap-3 pt-4">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancelar
            </Button>
            <Button type="submit" disabled={loading || loadingMentores}>
              {loading ? 'Salvando...' : (chamada ? 'Atualizar' : 'Criar Chamada')}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  )
}

export default ChamadaMentorModal

