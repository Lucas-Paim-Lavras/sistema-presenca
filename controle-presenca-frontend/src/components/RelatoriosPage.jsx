import { useState, useEffect } from 'react'
import { Download, FileText, FileSpreadsheet, Calendar, Filter, Users, GraduationCap, CheckSquare } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { turmasAPI, relatoriosAPI, presencasAPI } from '../services/api'

const RelatoriosPage = () => {
  const [turmas, setTurmas] = useState([])
  const [loading, setLoading] = useState(true)
  const [exportando, setExportando] = useState(false)
  const [filtros, setFiltros] = useState({
    turmaId: '',
    dataInicio: '',
    dataFim: ''
  })
  const [preview, setPreview] = useState([])
  const [loadingPreview, setLoadingPreview] = useState(false)

  useEffect(() => {
    carregarTurmas()
  }, [])

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

  const handleFiltroChange = (campo, valor) => {
    setFiltros(prev => ({
      ...prev,
      [campo]: valor
    }))
  }

  const gerarPreview = async () => {
    try {
      setLoadingPreview(true)
      const dados = await presencasAPI.gerarRelatorio(
        filtros.turmaId || null,
        filtros.dataInicio || null,
        filtros.dataFim || null
      )
      setPreview(dados.slice(0, 10)) // Mostrar apenas os primeiros 10 registros
    } catch (error) {
      console.error('Erro ao gerar preview:', error)
      alert('Erro ao gerar preview do relatório.')
    } finally {
      setLoadingPreview(false)
    }
  }

  const exportarRelatorio = async (tipo, formato) => {
    try {
      setExportando(true)
      
      switch (tipo) {
        case 'presencas':
          if (formato === 'csv') {
            await relatoriosAPI.exportarPresencasCSV(
              filtros.turmaId || null,
              filtros.dataInicio || null,
              filtros.dataFim || null
            )
          } else {
            await relatoriosAPI.exportarPresencasExcel(
              filtros.turmaId || null,
              filtros.dataInicio || null,
              filtros.dataFim || null
            )
          }
          break
        case 'alunos':
          if (formato === 'csv') {
            await relatoriosAPI.exportarAlunosCSV(filtros.turmaId || null)
          } else {
            await relatoriosAPI.exportarAlunosExcel(filtros.turmaId || null)
          }
          break
        case 'turmas':
          if (formato === 'csv') {
            await relatoriosAPI.exportarTurmasCSV()
          } else {
            await relatoriosAPI.exportarTurmasExcel()
          }
          break
      }
      
      alert('Relatório exportado com sucesso!')
    } catch (error) {
      console.error('Erro ao exportar relatório:', error)
      alert('Erro ao exportar relatório: ' + error.message)
    } finally {
      setExportando(false)
    }
  }

  const formatarData = (data) => {
    return new Date(data + 'T00:00:00').toLocaleDateString('pt-BR')
  }

  const formatarHora = (hora) => {
    return hora.substring(0, 5)
  }

  const limparFiltros = () => {
    setFiltros({
      turmaId: '',
      dataInicio: '',
      dataFim: ''
    })
    setPreview([])
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Relatórios</h1>
        <p className="mt-1 text-sm text-gray-500">
          Exporte dados do sistema em formato CSV ou Excel
        </p>
      </div>

      {/* Filtros */}
      <div className="bg-white p-6 rounded-lg shadow">
        <h2 className="text-lg font-medium text-gray-900 mb-4">Filtros para Relatórios</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <Label htmlFor="turmaId">Turma</Label>
            <Select 
              value={filtros.turmaId || "all"} 
              onValueChange={(value) => handleFiltroChange("turmaId", value === "all" ? "" : value)}
            >
              <SelectTrigger>
                <SelectValue placeholder="Todas as turmas" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Todas as turmas</SelectItem>
                {turmas.map((turma) => (
                  <SelectItem key={turma.id} value={turma.id.toString()}>
                    {turma.nome} ({turma.codigo})
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
          <div>
            <Label htmlFor="dataInicio">Data Início</Label>
            <Input
              id="dataInicio"
              type="date"
              value={filtros.dataInicio}
              onChange={(e) => handleFiltroChange('dataInicio', e.target.value)}
            />
          </div>
          <div>
            <Label htmlFor="dataFim">Data Fim</Label>
            <Input
              id="dataFim"
              type="date"
              value={filtros.dataFim}
              onChange={(e) => handleFiltroChange('dataFim', e.target.value)}
            />
          </div>
        </div>
        <div className="flex justify-end space-x-2 mt-4">
          <Button variant="outline" onClick={limparFiltros}>
            Limpar Filtros
          </Button>
          <Button onClick={gerarPreview} disabled={loadingPreview}>
            <Filter className="h-4 w-4 mr-2" />
            {loadingPreview ? 'Carregando...' : 'Visualizar'}
          </Button>
        </div>
      </div>

      {/* Preview dos dados */}
      {preview.length > 0 && (
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-medium text-gray-900 mb-4">
            Preview do Relatório ({preview.length} primeiros registros)
          </h2>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Data
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Hora
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Aluno
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Turma
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {preview.map((item, index) => (
                  <tr key={index}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatarData(item.dataPresenca)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {formatarHora(item.horaPresenca)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">{item.alunoNome}</div>
                      <div className="text-sm text-gray-500">{item.alunoMatricula}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">{item.turmaNome}</div>
                      <div className="text-sm text-gray-500">{item.turmaCodigo}</div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Opções de exportação */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Relatório de Presenças */}
        <div className="bg-white p-6 rounded-lg shadow">
          <div className="flex items-center mb-4">
            <CheckSquare className="h-8 w-8 text-blue-500 mr-3" />
            <div>
              <h3 className="text-lg font-medium text-gray-900">Relatório de Presenças</h3>
              <p className="text-sm text-gray-500">Exportar registros de presença</p>
            </div>
          </div>
          <div className="space-y-2">
            <Button
              onClick={() => exportarRelatorio('presencas', 'csv')}
              disabled={exportando}
              className="w-full flex items-center justify-center"
              variant="outline"
            >
              <FileText className="h-4 w-4 mr-2" />
              Exportar CSV
            </Button>
            <Button
              onClick={() => exportarRelatorio('presencas', 'excel')}
              disabled={exportando}
              className="w-full flex items-center justify-center"
            >
              <FileSpreadsheet className="h-4 w-4 mr-2" />
              Exportar Excel
            </Button>
          </div>
        </div>

        {/* Relatório de Alunos */}
        <div className="bg-white p-6 rounded-lg shadow">
          <div className="flex items-center mb-4">
            <GraduationCap className="h-8 w-8 text-green-500 mr-3" />
            <div>
              <h3 className="text-lg font-medium text-gray-900">Relatório de Alunos</h3>
              <p className="text-sm text-gray-500">Exportar dados dos alunos</p>
            </div>
          </div>
          <div className="space-y-2">
            <Button
              onClick={() => exportarRelatorio('alunos', 'csv')}
              disabled={exportando}
              className="w-full flex items-center justify-center"
              variant="outline"
            >
              <FileText className="h-4 w-4 mr-2" />
              Exportar CSV
            </Button>
            <Button
              onClick={() => exportarRelatorio('alunos', 'excel')}
              disabled={exportando}
              className="w-full flex items-center justify-center"
            >
              <FileSpreadsheet className="h-4 w-4 mr-2" />
              Exportar Excel
            </Button>
          </div>
        </div>

        {/* Relatório de Turmas */}
        <div className="bg-white p-6 rounded-lg shadow">
          <div className="flex items-center mb-4">
            <Users className="h-8 w-8 text-purple-500 mr-3" />
            <div>
              <h3 className="text-lg font-medium text-gray-900">Relatório de Turmas</h3>
              <p className="text-sm text-gray-500">Exportar dados das turmas</p>
            </div>
          </div>
          <div className="space-y-2">
            <Button
              onClick={() => exportarRelatorio('turmas', 'csv')}
              disabled={exportando}
              className="w-full flex items-center justify-center"
              variant="outline"
            >
              <FileText className="h-4 w-4 mr-2" />
              Exportar CSV
            </Button>
            <Button
              onClick={() => exportarRelatorio('turmas', 'excel')}
              disabled={exportando}
              className="w-full flex items-center justify-center"
            >
              <FileSpreadsheet className="h-4 w-4 mr-2" />
              Exportar Excel
            </Button>
          </div>
        </div>
      </div>

      {/* Instruções */}
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
        <h3 className="text-sm font-medium text-blue-900 mb-2">Instruções</h3>
        <ul className="text-sm text-blue-700 space-y-1">
          <li>• Use os filtros acima para personalizar os relatórios de presenças e alunos</li>
          <li>• O filtro de turma se aplica aos relatórios de presenças e alunos</li>
          <li>• Os filtros de data se aplicam apenas ao relatório de presenças</li>
          <li>• O relatório de turmas sempre exporta todas as turmas ativas</li>
          <li>• Clique em "Visualizar" para ver uma prévia dos dados antes de exportar</li>
        </ul>
      </div>
    </div>
  )
}

export default RelatoriosPage

