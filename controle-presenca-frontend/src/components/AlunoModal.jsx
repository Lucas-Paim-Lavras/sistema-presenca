import { useState, useEffect } from 'react'
import { X } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'

const AlunoModal = ({ aluno, turmas, onSave, onClose }) => {
  const [formData, setFormData] = useState({
    nome: '',
    matricula: '',
    email: '',
    turmaId: '',
    ativo: true
  })
  const [errors, setErrors] = useState({})
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (aluno) {
      setFormData({
        nome: aluno.nome || '',
        matricula: aluno.matricula || '',
        email: aluno.email || '',
        turmaId: aluno.turmaId?.toString() || '',
        ativo: aluno.ativo !== undefined ? aluno.ativo : true
      })
    }
  }, [aluno])

  const validateForm = () => {
    const newErrors = {}

    if (!formData.nome.trim()) {
      newErrors.nome = 'Nome é obrigatório'
    }

    if (!formData.matricula.trim()) {
      newErrors.matricula = 'Matrícula é obrigatória'
    }

    if (!formData.email.trim()) {
      newErrors.email = 'Email é obrigatório'
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Email deve ter um formato válido'
    }

    if (!formData.turmaId) {
      newErrors.turmaId = 'Turma é obrigatória'
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!validateForm()) {
      return
    }

    setLoading(true)
    try {
      const dadosParaSalvar = {
        ...formData,
        turmaId: parseInt(formData.turmaId)
      }
      await onSave(dadosParaSalvar)
    } catch (error) {
      console.error('Erro ao salvar aluno:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }))
    
    // Limpar erro do campo quando o usuário começar a digitar
    if (errors[field]) {
      setErrors(prev => ({
        ...prev,
        [field]: ''
      }))
    }
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-md w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">
            {aluno ? 'Editar Aluno' : 'Novo Aluno'}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {/* Nome */}
          <div>
            <Label htmlFor="nome">Nome Completo *</Label>
            <Input
              id="nome"
              type="text"
              value={formData.nome}
              onChange={(e) => handleChange('nome', e.target.value)}
              placeholder="Ex: João Silva"
              className={errors.nome ? 'border-red-500' : ''}
            />
            {errors.nome && (
              <p className="mt-1 text-sm text-red-600">{errors.nome}</p>
            )}
          </div>

          {/* Matrícula */}
          <div>
            <Label htmlFor="matricula">Matrícula *</Label>
            <Input
              id="matricula"
              type="text"
              value={formData.matricula}
              onChange={(e) => handleChange('matricula', e.target.value)}
              placeholder="Ex: 2024001"
              className={errors.matricula ? 'border-red-500' : ''}
            />
            {errors.matricula && (
              <p className="mt-1 text-sm text-red-600">{errors.matricula}</p>
            )}
          </div>

          {/* Email */}
          <div>
            <Label htmlFor="email">Email *</Label>
            <Input
              id="email"
              type="email"
              value={formData.email}
              onChange={(e) => handleChange('email', e.target.value)}
              placeholder="Ex: joao.silva@email.com"
              className={errors.email ? 'border-red-500' : ''}
            />
            {errors.email && (
              <p className="mt-1 text-sm text-red-600">{errors.email}</p>
            )}
          </div>

          {/* Turma */}
          <div>
            <Label htmlFor="turmaId">Turma *</Label>
            <Select 
              value={formData.turmaId} 
              onValueChange={(value) => handleChange('turmaId', value)}
            >
              <SelectTrigger className={errors.turmaId ? 'border-red-500' : ''}>
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
            {errors.turmaId && (
              <p className="mt-1 text-sm text-red-600">{errors.turmaId}</p>
            )}
          </div>

          {/* Status */}
          <div className="flex items-center space-x-2">
            <input
              id="ativo"
              type="checkbox"
              checked={formData.ativo}
              onChange={(e) => handleChange('ativo', e.target.checked)}
              className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
            />
            <Label htmlFor="ativo">Aluno ativo</Label>
          </div>

          {/* Buttons */}
          <div className="flex justify-end space-x-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={onClose}
              disabled={loading}
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              disabled={loading}
            >
              {loading ? 'Salvando...' : (aluno ? 'Atualizar' : 'Criar')}
            </Button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default AlunoModal

