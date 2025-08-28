import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { api } from '../services/api'

const MentorModal = ({ open, onClose, onSuccess, mentor }) => {
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    tipoMentor: '',
    ativo: true
  })
  const [loading, setLoading] = useState(false)

  const tiposMentor = [
    { value: 'MENTOR', label: 'Mentor' },
    { value: 'MENTOR_TRAINEE', label: 'Mentor-trainee' },
    { value: 'MENTOR_COORDENADOR', label: 'Mentor Coordenador' }
  ]

  useEffect(() => {
    if (mentor) {
      setFormData({
        nome: mentor.nome || '',
        email: mentor.email || '',
        tipoMentor: mentor.tipoMentor || '',
        ativo: mentor.ativo !== undefined ? mentor.ativo : true
      })
    } else {
      setFormData({
        nome: '',
        email: '',
        tipoMentor: '',
        ativo: true
      })
    }
  }, [mentor, open])

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!formData.nome.trim() || !formData.email.trim() || !formData.tipoMentor) {
      alert('Por favor, preencha todos os campos obrigatórios.')
      return
    }

    try {
      setLoading(true)
      
      if (mentor) {
        await api.put(`/mentores/${mentor.id}`, formData)
        alert('Mentor atualizado com sucesso!')
      } else {
        await api.post('/mentores', formData)
        alert('Mentor criado com sucesso!')
      }
      
      onSuccess()
    } catch (error) {
      console.error('Erro ao salvar mentor:', error)
      if (error.response?.status === 400) {
        alert('Erro: Verifique se o email já não está sendo usado por outro mentor.')
      } else {
        alert('Erro ao salvar mentor. Tente novamente.')
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

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>
            {mentor ? 'Editar Mentor' : 'Novo Mentor'}
          </DialogTitle>
        </DialogHeader>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="nome">Nome *</Label>
            <Input
              id="nome"
              value={formData.nome}
              onChange={(e) => handleChange('nome', e.target.value)}
              placeholder="Digite o nome do mentor"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="email">Email *</Label>
            <Input
              id="email"
              type="email"
              value={formData.email}
              onChange={(e) => handleChange('email', e.target.value)}
              placeholder="Digite o email do mentor"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="tipoMentor">Tipo de Mentor *</Label>
            <Select value={formData.tipoMentor} onValueChange={(value) => handleChange('tipoMentor', value)}>
              <SelectTrigger>
                <SelectValue placeholder="Selecione o tipo de mentor" />
              </SelectTrigger>
              <SelectContent>
                {tiposMentor.map((tipo) => (
                  <SelectItem key={tipo.value} value={tipo.value}>
                    {tipo.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          {mentor && (
            <div className="space-y-2">
              <Label htmlFor="ativo">Status</Label>
              <Select 
                value={formData.ativo ? 'true' : 'false'} 
                onValueChange={(value) => handleChange('ativo', value === 'true')}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="true">Ativo</SelectItem>
                  <SelectItem value="false">Inativo</SelectItem>
                </SelectContent>
              </Select>
            </div>
          )}

          <div className="flex justify-end gap-3 pt-4">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancelar
            </Button>
            <Button type="submit" disabled={loading}>
              {loading ? 'Salvando...' : (mentor ? 'Atualizar' : 'Criar')}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  )
}

export default MentorModal

