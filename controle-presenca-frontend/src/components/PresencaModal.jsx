import { useState, useEffect } from 'react'
import { X } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'

const PresencaModal = ({ presenca, turmas, alunos, onSave, onClose }) => {
  const [formData, setFormData] = useState({
    alunoId: '',
    turmaId: '',
    dataPresenca: '',
    horaPresenca: '',
    observacoes: ''
  })
  const [errors, setErrors] = useState({})
  const [loading, setLoading] = useState(false)
  const [alunosFiltrados, setAlunosFiltrados] = useState([])

  useEffect(() => {
    if (presenca) {
      setFormData({
        alunoId: presenca.alunoId?.toString() || '',
        turmaId: presenca.turmaId?.toString() || '',
        dataPresenca: presenca.dataPresenca || '',
        horaPresenca: presenca.horaPresenca || '',
        observacoes: presenca.observacoes || ''
      })
    } else {
      // Para nova presença, definir data e hora atuais
      const agora = new Date()
      const dataAtual = agora.toISOString().split('T')[0]
      const horaAtual = agora.toTimeString().substring(0, 5)
      
      setFormData(prev => ({
        ...prev,
        dataPresenca: dataAtual,
        horaPresenca: horaAtual
      }))
    }
  }, [presenca])

  useEffect(() => {
    // Filtrar alunos pela turma selecionada
    if (formData.turmaId) {
      const alunosDaTurma = alunos.filter(aluno => 
        aluno.turmaId.toString() === formData.turmaId && aluno.ativo
      )
      setAlunosFiltrados(alunosDaTurma)
    } else {
      setAlunosFiltrados([])
    }
  }, [formData.turmaId, alunos])

  const validateForm = () => {
    const newErrors = {}

    if (!formData.alunoId) {
      newErrors.alunoId = 'Aluno é obrigatório'
    }

    if (!formData.turmaId) {
      newErrors.turmaId = 'Turma é obrigatória'
    }

    if (!formData.dataPresenca) {
      newErrors.dataPresenca = 'Data é obrigatória'
    }

    if (!formData.horaPresenca) {
      newErrors.horaPresenca = 'Hora é obrigatória'
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
        alunoId: parseInt(formData.alunoId),
        turmaId: parseInt(formData.turmaId)
      }
      await onSave(dadosParaSalvar)
    } catch (error) {
      console.error('Erro ao salvar presença:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }))
    
    // Se mudou a turma, limpar o aluno selecionado
    if (field === 'turmaId') {
      setFormData(prev => ({
        ...prev,
        alunoId: ''
      }))
    }
    
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
            {presenca ? 'Editar Presença' : 'Nova Presença'}
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

          {/* Aluno */}
          <div>
            <Label htmlFor="alunoId">Aluno *</Label>
            <Select 
              value={formData.alunoId} 
              onValueChange={(value) => handleChange('alunoId', value)}
              disabled={!formData.turmaId}
            >
              <SelectTrigger className={errors.alunoId ? 'border-red-500' : ''}>
                <SelectValue placeholder={formData.turmaId ? "Selecione um aluno" : "Selecione uma turma primeiro"} />
              </SelectTrigger>
              <SelectContent>
                {alunosFiltrados.map((aluno) => (
                  <SelectItem key={aluno.id} value={aluno.id.toString()}>
                    {aluno.nome} ({aluno.matricula})
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            {errors.alunoId && (
              <p className="mt-1 text-sm text-red-600">{errors.alunoId}</p>
            )}
          </div>

          {/* Data */}
          <div>
            <Label htmlFor="dataPresenca">Data da Presença *</Label>
            <Input
              id="dataPresenca"
              type="date"
              value={formData.dataPresenca}
              onChange={(e) => handleChange('dataPresenca', e.target.value)}
              className={errors.dataPresenca ? 'border-red-500' : ''}
            />
            {errors.dataPresenca && (
              <p className="mt-1 text-sm text-red-600">{errors.dataPresenca}</p>
            )}
          </div>

          {/* Hora */}
          <div>
            <Label htmlFor="horaPresenca">Hora da Presença *</Label>
            <Input
              id="horaPresenca"
              type="time"
              value={formData.horaPresenca}
              onChange={(e) => handleChange('horaPresenca', e.target.value)}
              className={errors.horaPresenca ? 'border-red-500' : ''}
            />
            {errors.horaPresenca && (
              <p className="mt-1 text-sm text-red-600">{errors.horaPresenca}</p>
            )}
          </div>

          {/* Observações */}
          <div>
            <Label htmlFor="observacoes">Observações</Label>
            <Textarea
              id="observacoes"
              value={formData.observacoes}
              onChange={(e) => handleChange('observacoes', e.target.value)}
              placeholder="Observações opcionais sobre a presença"
              rows={3}
            />
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
              {loading ? 'Salvando...' : (presenca ? 'Atualizar' : 'Registrar')}
            </Button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default PresencaModal

