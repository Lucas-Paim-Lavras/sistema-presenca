import { useState, useEffect } from 'react'
import { Users, GraduationCap, CheckSquare, TrendingUp } from 'lucide-react'
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts'
import { turmasAPI, alunosAPI, presencasAPI } from '../services/api'

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalTurmas: 0,
    totalAlunos: 0,
    totalPresencas: 0,
    presencasHoje: 0
  })
  const [turmasData, setTurmasData] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    carregarDados()
  }, [])

  const carregarDados = async () => {
    try {
      setLoading(true)
      
      // Carregar estatísticas básicas
      const [turmas, alunos, presencas] = await Promise.all([
        turmasAPI.listar(),
        alunosAPI.listar(),
        presencasAPI.listar()
      ])

      // Calcular presenças de hoje
      const hoje = new Date().toISOString().split('T')[0]
      const presencasHoje = presencas.filter(p => p.dataPresenca === hoje).length

      setStats({
        totalTurmas: turmas.length,
        totalAlunos: alunos.length,
        totalPresencas: presencas.length,
        presencasHoje
      })

      // Preparar dados para gráficos
      const dadosGrafico = turmas.map(turma => ({
        nome: turma.nome,
        alunos: turma.totalAlunos || 0,
        presencas: turma.totalPresencas || 0
      }))

      setTurmasData(dadosGrafico)
    } catch (error) {
      console.error('Erro ao carregar dados do dashboard:', error)
    } finally {
      setLoading(false)
    }
  }

  const StatCard = ({ title, value, icon: Icon, color }) => (
    <div className="bg-white overflow-hidden shadow rounded-lg">
      <div className="p-5">
        <div className="flex items-center">
          <div className="flex-shrink-0">
            <Icon className={`h-6 w-6 ${color}`} />
          </div>
          <div className="ml-5 w-0 flex-1">
            <dl>
              <dt className="text-sm font-medium text-gray-500 truncate">
                {title}
              </dt>
              <dd className="text-lg font-medium text-gray-900">
                {loading ? '...' : value}
              </dd>
            </dl>
          </div>
        </div>
      </div>
    </div>
  )

  const COLORS = ['#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6']

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p className="mt-1 text-sm text-gray-500">
          Visão geral do sistema de controle de presença
        </p>
      </div>

      {/* Cards de estatísticas */}
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Total de Turmas"
          value={stats.totalTurmas}
          icon={Users}
          color="text-blue-500"
        />
        <StatCard
          title="Total de Alunos"
          value={stats.totalAlunos}
          icon={GraduationCap}
          color="text-green-500"
        />
        <StatCard
          title="Total de Presenças"
          value={stats.totalPresencas}
          icon={CheckSquare}
          color="text-yellow-500"
        />
        <StatCard
          title="Presenças Hoje"
          value={stats.presencasHoje}
          icon={TrendingUp}
          color="text-purple-500"
        />
      </div>

      {/* Gráficos */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Gráfico de barras - Alunos por turma */}
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-lg font-medium text-gray-900 mb-4">
            Alunos por Turma
          </h3>
          {loading ? (
            <div className="h-64 flex items-center justify-center">
              <div className="text-gray-500">Carregando...</div>
            </div>
          ) : (
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={turmasData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis 
                  dataKey="nome" 
                  angle={-45}
                  textAnchor="end"
                  height={80}
                  interval={0}
                />
                <YAxis />
                <Tooltip />
                <Bar dataKey="alunos" fill="#3B82F6" />
              </BarChart>
            </ResponsiveContainer>
          )}
        </div>

        {/* Gráfico de pizza - Distribuição de presenças */}
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-lg font-medium text-gray-900 mb-4">
            Distribuição de Presenças por Turma
          </h3>
          {loading ? (
            <div className="h-64 flex items-center justify-center">
              <div className="text-gray-500">Carregando...</div>
            </div>
          ) : (
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={turmasData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ nome, presencas }) => `${nome}: ${presencas}`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="presencas"
                >
                  {turmasData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          )}
        </div>
      </div>

      {/* Tabela de resumo por turma */}
      <div className="bg-white shadow rounded-lg">
        <div className="px-4 py-5 sm:p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">
            Resumo por Turma
          </h3>
          {loading ? (
            <div className="text-center py-4">
              <div className="text-gray-500">Carregando...</div>
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
                      Total de Alunos
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Total de Presenças
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Média de Presença
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {turmasData.map((turma, index) => (
                    <tr key={index}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {turma.nome}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {turma.alunos}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {turma.presencas}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {turma.alunos > 0 ? (turma.presencas / turma.alunos).toFixed(1) : '0'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default Dashboard

