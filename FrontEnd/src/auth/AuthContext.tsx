import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { ApiError, createUser, getUser, loginRequest, logoutRequest, userIdFromToken } from '../api/client'
import type { CreateUserInput, User } from '../api/types'

const TOKEN_KEY = 'locaflix.token'

type AuthContextValue = {
  token: string | null
  user: User | null
  loading: boolean
  login: (email: string, password: string, remember: boolean) => Promise<void>
  register: (input: CreateUserInput) => Promise<void>
  logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | null>(null)

function readStoredToken() {
  return localStorage.getItem(TOKEN_KEY) ?? sessionStorage.getItem(TOKEN_KEY)
}

function storeToken(token: string, remember: boolean) {
  localStorage.removeItem(TOKEN_KEY)
  sessionStorage.removeItem(TOKEN_KEY)
  const storage = remember ? localStorage : sessionStorage
  storage.setItem(TOKEN_KEY, token)
}

function clearStoredToken() {
  localStorage.removeItem(TOKEN_KEY)
  sessionStorage.removeItem(TOKEN_KEY)
}

async function loadUser(token: string) {
  const userId = userIdFromToken(token)
  if (!userId) {
    throw new ApiError('Sessão inválida.', 401)
  }
  return getUser(userId)
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(readStoredToken)
  const [user, setUser] = useState<User | null>(null)
  const [loading, setLoading] = useState(() => readStoredToken() !== null)

  useEffect(() => {
    if (!token) {
      return
    }

    let active = true
    loadUser(token)
      .then((loaded) => {
        if (active) {
          setUser(loaded)
        }
      })
      .catch((error: unknown) => {
        if (!active) {
          return
        }
        if (error instanceof ApiError && (error.status === 401 || error.status === 404)) {
          clearStoredToken()
          setToken(null)
          setUser(null)
        }
      })
      .finally(() => {
        if (active) {
          setLoading(false)
        }
      })

    return () => {
      active = false
    }
  }, [token])

  const login = useCallback(async (email: string, password: string, remember: boolean) => {
    const response = await loginRequest(email.trim(), password)
    storeToken(response.token, remember)
    setToken(response.token)
    setUser(await loadUser(response.token))
  }, [])

  const register = useCallback(async (input: CreateUserInput) => {
    await createUser(input)
    await login(input.email, input.password, true)
  }, [login])

  const logout = useCallback(async () => {
    const current = token
    try {
      if (current) {
        await logoutRequest(current)
      }
    } catch {
      // A sessão local é encerrada mesmo quando a API não responde.
    } finally {
      clearStoredToken()
      setToken(null)
      setUser(null)
    }
  }, [token])

  const value = useMemo(
    () => ({ token, user, loading, login, register, logout }),
    [token, user, loading, login, register, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth precisa estar dentro de AuthProvider')
  }
  return context
}
