import { type FormEvent, useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { listPlans, toErrorMessage } from '../api/client'
import type { Plan } from '../api/types'
import { useAuth } from '../auth/AuthContext'
import { AuthLayout } from '../components/AuthLayout'

export function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [age, setAge] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [planId, setPlanId] = useState('')
  const [plans, setPlans] = useState<Plan[]>([])
  const [plansError, setPlansError] = useState('')
  const [loadingPlans, setLoadingPlans] = useState(true)
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    let active = true
    listPlans()
      .then((result) => {
        if (!active) {
          return
        }
        setPlans(result)
        setPlanId(result[0]?.id ?? '')
      })
      .catch((caught: unknown) => {
        if (active) {
          setPlansError(toErrorMessage(caught))
        }
      })
      .finally(() => {
        if (active) {
          setLoadingPlans(false)
        }
      })
    return () => {
      active = false
    }
  }, [])

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError('')

    const parsedAge = Number(age)
    if (!name.trim()) {
      setError('Informe o nome.')
      return
    }
    if (age.trim() === '' || !Number.isInteger(parsedAge) || parsedAge < 0) {
      setError('Informe uma idade válida.')
      return
    }
    if (!email.trim() || !email.includes('@')) {
      setError('Informe um e-mail válido.')
      return
    }
    if (!password) {
      setError('Informe a senha.')
      return
    }
    if (password !== confirmPassword) {
      setError('As senhas não coincidem.')
      return
    }
    if (!planId) {
      setError('Selecione um plano.')
      return
    }

    setSubmitting(true)
    try {
      await register({
        name: name.trim(),
        age: parsedAge,
        email: email.trim(),
        password,
        planId,
      })
      navigate('/inicio')
    } catch (caught) {
      setError(toErrorMessage(caught))
    } finally {
      setSubmitting(false)
    }
  }

  const blocked = loadingPlans || plans.length === 0

  return (
    <AuthLayout>
      <h1>Cadastre-se</h1>
      <form className="form" onSubmit={handleSubmit} noValidate>
        {error ? <p className="form-error">{error}</p> : null}
        {plansError ? <p className="form-error">{plansError}</p> : null}
        {!loadingPlans && !plansError && plans.length === 0 ? (
          <p className="form-error">Nenhum plano disponível. Crie um plano na API antes de cadastrar.</p>
        ) : null}
        <label className="sr-only" htmlFor="name">
          Nome
        </label>
        <input
          id="name"
          name="name"
          autoComplete="name"
          placeholder="Nome"
          value={name}
          onChange={(event) => setName(event.target.value)}
        />
        <label className="sr-only" htmlFor="age">
          Idade
        </label>
        <input
          id="age"
          name="age"
          type="number"
          min="0"
          step="1"
          inputMode="numeric"
          placeholder="Idade"
          value={age}
          onChange={(event) => setAge(event.target.value)}
        />
        <label className="sr-only" htmlFor="register-email">
          E-mail
        </label>
        <input
          id="register-email"
          name="email"
          type="email"
          autoComplete="email"
          placeholder="E-mail"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
        />
        <label className="sr-only" htmlFor="register-password">
          Senha
        </label>
        <input
          id="register-password"
          name="password"
          type="password"
          autoComplete="new-password"
          placeholder="Senha"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
        />
        <label className="sr-only" htmlFor="confirm-password">
          Confirmar senha
        </label>
        <input
          id="confirm-password"
          name="confirmPassword"
          type="password"
          autoComplete="new-password"
          placeholder="Confirmar senha"
          value={confirmPassword}
          onChange={(event) => setConfirmPassword(event.target.value)}
        />
        <label className="sr-only" htmlFor="plan">
          Plano
        </label>
        <select
          id="plan"
          name="planId"
          value={planId}
          disabled={loadingPlans || plans.length === 0}
          onChange={(event) => setPlanId(event.target.value)}
        >
          {loadingPlans ? <option value="">Carregando planos...</option> : null}
          {!loadingPlans && plans.length === 0 ? <option value="">Nenhum plano</option> : null}
          {plans.map((plan) => (
            <option key={plan.id} value={plan.id}>
              {plan.name} · {plan.allowedRentalQuantity} aluguéis
            </option>
          ))}
        </select>
        <button className="primary" type="submit" disabled={submitting || blocked}>
          {submitting ? 'Cadastrando...' : 'Cadastrar'}
        </button>
      </form>
      <p className="switch">
        Já tem conta? <Link to="/">Entrar</Link>
      </p>
    </AuthLayout>
  )
}
