import { Link } from 'react-router-dom'

type CatalogHeaderProps = {
  name?: string
  used: number
  limit?: number
  loggingOut: boolean
  onLogout: () => void
}

export function CatalogHeader({ name, used, limit, loggingOut, onLogout }: CatalogHeaderProps) {
  return (
    <header className="catalog-header">
      <Link className="catalog-brand" to="/inicio">
        Locaflix
      </Link>
      <div className="catalog-header-meta">
        {name ? <span className="catalog-user">{name}</span> : null}
        {typeof limit === 'number' ? (
          <span className="catalog-quota">
            {used} de {limit} aluguéis
          </span>
        ) : null}
        <button className="catalog-logout" type="button" onClick={onLogout} disabled={loggingOut}>
          {loggingOut ? 'Saindo...' : 'Sair'}
        </button>
      </div>
    </header>
  )
}
