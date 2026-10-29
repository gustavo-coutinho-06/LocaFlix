import { type FormEvent, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { toErrorMessage } from '../api/client'
import { useAuth } from '../auth/AuthContext'
import { AuthLayout } from '../components/AuthLayout'

export function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [remember, setRemember] = useState(false)
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError('')

    if (!email.trim() || !password) {
      setError('Informe o e-mail e a senha.')
      return
    }

    setSubmitting(true)
    try {
      await login(email, password, remember)
      navigate('/inicio')
    } catch (caught) {
      setError(toErrorMessage(caught))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <AuthLayout>
      <h1>Entrar</h1>
      <form className="form" onSubmit={handleSubmit} noValidate>
        {error ? <p className="form-error">{error}</p> : null}
        <label className="sr-only" htmlFor="email">
          E-mail
        </label>
        <input
          id="email"
          name="email"
          type="email"
          autoComplete="email"
          placeholder="E-mail"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
        />
        <label className="sr-only" htmlFor="password">
          Senha
        </label>
        <input
          id="password"
          name="password"
          type="password"
          autoComplete="current-password"
          placeholder="Senha"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
        />
        <button className="primary" type="submit" disabled={submitting}>
          {submitting ? 'Entrando...' : 'Entrar'}
        </button>
        <label className="remember">
          <input
            type="checkbox"
            checked={remember}
            onChange={(event) => setRemember(event.target.checked)}
          />
          Lembre-se de mim
        </label>
      </form>
      <p className="switch">
        Novo por aqui? <Link to="/cadastro">Cadastre-se</Link>
      </p>
    </AuthLayout>
  )
}
