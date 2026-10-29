import type { ReactNode } from 'react'
import { Link } from 'react-router-dom'

const POSTERS = [
  { title: 'Noite Roxa', color: '#4c1d95' },
  { title: 'Órbita', color: '#1e1b4b' },
  { title: 'Maré Alta', color: '#312e81' },
  { title: 'Vértice', color: '#581c87' },
  { title: 'Lume', color: '#6d28d9' },
  { title: 'Atlas', color: '#3730a3' },
  { title: 'Sombra', color: '#3b0764' },
  { title: 'Eco', color: '#5b21b6' },
  { title: 'Nébula', color: '#1e3a8a' },
  { title: 'Trilha', color: '#4a044e' },
  { title: 'Aurora', color: '#6b21a8' },
  { title: 'Pulso', color: '#312e81' },
  { title: 'Véu', color: '#4c1d95' },
  { title: 'Costa', color: '#1e1b4b' },
  { title: 'Íris', color: '#7e22ce' },
  { title: 'Bruma', color: '#3730a3' },
  { title: 'Quasar', color: '#581c87' },
  { title: 'Linha', color: '#3b0764' },
  { title: 'Farol', color: '#5b21b6' },
  { title: 'Relva', color: '#1e3a8a' },
  { title: 'Cume', color: '#4c1d95' },
  { title: 'Norte', color: '#312e81' },
  { title: 'Vale', color: '#6d28d9' },
  { title: 'Raio', color: '#4a044e' },
]

export function AuthLayout({ children }: { children: ReactNode }) {
  return (
    <div className="screen">
      <div className="poster-wall" aria-hidden="true">
        {POSTERS.map((poster) => (
          <div key={poster.title} className="poster" style={{ background: poster.color }}>
            <span>{poster.title}</span>
          </div>
        ))}
      </div>
      <div className="veil" />
      <Link className="brand" to="/">
        Locaflix
      </Link>
      <main className="auth-shell">
        <section className="card">{children}</section>
      </main>
    </div>
  )
}

export function LoadingScreen() {
  return (
    <div className="screen boot">
      <p>Carregando...</p>
    </div>
  )
}
