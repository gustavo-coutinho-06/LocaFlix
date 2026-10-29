import type { CreateUserInput, Movie, Plan, Rental, TokenResponse, User } from './types'

const API_BASE = import.meta.env.VITE_API_BASE_URL || '/api'

const MESSAGE_IN_PORTUGUESE: Record<string, string> = {
  'Invalid credentials': 'E-mail ou senha incorretos.',
  'Authentication is required': 'É preciso entrar novamente.',
  'email must not be blank': 'Informe o e-mail.',
  'name must not be blank': 'Informe o nome.',
  'age must be greater than or equal to zero': 'A idade precisa ser zero ou mais.',
  'password must not be blank': 'Informe a senha.',
  'planId must not be null': 'Selecione um plano.',
  'Plan not found': 'Plano não encontrado.',
  'User not found': 'Usuário não encontrado.',
  'The request conflicts with existing data': 'Este e-mail já está cadastrado.',
  'User is already renting this movie': 'Você já está alugando este filme.',
  'Rental limit reached for the current plan': 'Você atingiu o limite de aluguéis do seu plano.',
  'Movie not found': 'Filme não encontrado.',
  'Rental not found': 'Aluguel não encontrado.',
  'movieId must not be null': 'Selecione um filme.',
}

export class ApiError extends Error {
  status: number

  constructor(message: string, status: number) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

export function toErrorMessage(error: unknown): string {
  if (error instanceof ApiError) {
    return MESSAGE_IN_PORTUGUESE[error.message] ?? error.message
  }
  if (error instanceof TypeError) {
    return 'Não foi possível conectar à API em localhost.'
  }
  return 'Não foi possível concluir a solicitação.'
}

export function userIdFromToken(token: string): string | null {
  const part = token.split('.')[1]
  if (!part) {
    return null
  }
  try {
    const normalized = part.replace(/-/g, '+').replace(/_/g, '/')
    const padded = normalized + '='.repeat((4 - (normalized.length % 4)) % 4)
    const payload = JSON.parse(atob(padded)) as { sub?: unknown }
    return typeof payload.sub === 'string' ? payload.sub : null
  } catch {
    return null
  }
}

export async function api<T>(path: string, options: RequestInit = {}, token?: string | null): Promise<T> {
  const headers = new Headers(options.headers)
  if (options.body && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json')
  }
  if (token) {
    headers.set('Authorization', `Bearer ${token}`)
  }

  let response: Response
  try {
    response = await fetch(`${API_BASE}${path}`, { ...options, headers })
  } catch (error) {
    if (error instanceof TypeError) {
      throw error
    }
    throw new TypeError('Não foi possível conectar à API em localhost.')
  }

  if (response.status === 204) {
    return undefined as T
  }

  const text = await response.text()
  let data: { message?: unknown } | null = null
  if (text) {
    try {
      data = JSON.parse(text) as { message?: unknown }
    } catch {
      if (!response.ok) {
        throw new ApiError('Não foi possível concluir a solicitação.', response.status)
      }
      throw new ApiError('A API devolveu uma resposta inválida.', response.status)
    }
  }
  if (!response.ok) {
    const message = data && typeof data.message === 'string' ? data.message : 'Não foi possível concluir a solicitação.'
    throw new ApiError(message, response.status)
  }
  return data as T
}

export function loginRequest(email: string, password: string) {
  return api<TokenResponse>('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
  })
}

export function logoutRequest(token: string) {
  return api<void>('/auth/logout', { method: 'POST' }, token)
}

export function createUser(input: CreateUserInput) {
  return api<User>('/users', {
    method: 'POST',
    body: JSON.stringify(input),
  })
}

export function getUser(id: string) {
  return api<User>(`/users/${id}`)
}

export function listPlans() {
  return api<Plan[]>('/plans')
}

export function listMovies() {
  return api<Movie[]>('/movies')
}

export function listRentals(token: string) {
  return api<Rental[]>('/rentals', {}, token)
}

export function rentMovie(token: string, movieId: string) {
  return api<Rental>(
    '/rentals',
    {
      method: 'POST',
      body: JSON.stringify({ movieId }),
    },
    token,
  )
}

export function returnRental(token: string, rentalId: string) {
  return api<void>(`/rentals/${rentalId}`, { method: 'DELETE' }, token)
}
