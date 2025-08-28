import { useState } from 'react'
import { Link, useLocation, Outlet } from 'react-router-dom' // ← Adicione Outlet aqui
import { 
  Home, 
  Users, 
  GraduationCap, 
  CheckSquare, 
  FileText, 
  UserCheck,
  Calendar,
  Menu,
  X
} from 'lucide-react'
import { Button } from '@/components/ui/button'

const Layout = () => { // ← Remova o {children}
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const location = useLocation()

  const navigation = [
    { name: 'Dashboard', href: '/', icon: Home },
    { name: 'Turmas', href: '/turmas', icon: Users },
    { name: 'Alunos', href: '/alunos', icon: GraduationCap },
    { name: 'Presenças', href: '/presencas', icon: CheckSquare },
    { name: 'Mentores', href: '/mentores', icon: UserCheck },
    { name: 'Chamadas de Mentores', href: '/chamadas-mentores', icon: Calendar },
    { name: 'Relatórios', href: '/relatorios', icon: FileText },
  ]

  const isActive = (href) => {
    if (!location || !location.pathname) {
      return false
    }
    return location.pathname === href
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* ... (código do sidebar igual) ... */}

      {/* Conteúdo principal */}
      <div className="md:pl-64 flex flex-col flex-1">
        {/* Header mobile */}
        <div className="sticky top-0 z-10 md:hidden pl-1 pt-1 sm:pl-3 sm:pt-3 bg-white border-b border-gray-200">
          <Button
            variant="ghost"
            size="sm"
            className="-ml-0.5 -mt-0.5 h-12 w-12"
            onClick={() => setSidebarOpen(true)}
          >
            <Menu className="h-6 w-6" />
          </Button>
        </div>

        {/* Conteúdo da página */}
        <main className="flex-1">
          <div className="py-6">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 md:px-8">
              <Outlet /> {/* 👈 TROQUE {children} POR <Outlet /> */}
            </div>
          </div>
        </main>
      </div>
    </div>
  )
}

export default Layout