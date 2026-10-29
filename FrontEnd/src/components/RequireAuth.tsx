import type { ReactNode } from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { LoadingScreen } from './AuthLayout'

export function RequireAuth({ children }: { children: ReactNode }) {
  const { token, loading } = useAuth()

  if (loading) {
    return <LoadingScreen />
  }
  if (!token) {
    return <Navigate to="/" replace />
  }
  return children
}

export function GuestOnly({ children }: { children: ReactNode }) {
  const { token, loading } = useAuth()

  if (loading) {
    return <LoadingScreen />
  }
  if (token) {
    return <Navigate to="/inicio" replace />
  }
  return children
}
