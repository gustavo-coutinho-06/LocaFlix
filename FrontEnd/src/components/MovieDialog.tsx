import { useEffect, useState } from 'react'
import type { Movie } from '../api/types'
import { formatRentedAt } from './movieText'
import { Stars } from './Stars'

type MovieDialogProps = {
  movie: Movie
  rentedAt?: string
  busy: boolean
  error: string
  onClose: () => void
  onRent: (movie: Movie) => void
  onReturn: (movie: Movie) => void
}

export function MovieDialog({ movie, rentedAt, busy, error, onClose, onRent, onReturn }: MovieDialogProps) {
  const [failed, setFailed] = useState(false)

  useEffect(() => {
    const previous = document.body.style.overflow
    document.body.style.overflow = 'hidden'

    function onKey(event: KeyboardEvent) {
      if (event.key === 'Escape') {
        onClose()
      }
    }

    window.addEventListener('keydown', onKey)
    return () => {
      document.body.style.overflow = previous
      window.removeEventListener('keydown', onKey)
    }
  }, [onClose])

  const showImage = Boolean(movie.imageUrl) && !failed

  return (
    <div className="dialog-backdrop" onClick={onClose}>
      <div
        className="dialog-panel"
        role="dialog"
        aria-modal="true"
        aria-labelledby="movie-dialog-title"
        onClick={(event) => event.stopPropagation()}
      >
        <div className="dialog-art">
          {showImage ? (
            <img src={movie.imageUrl} alt="" onError={() => setFailed(true)} />
          ) : (
            <span className="poster-fallback">{movie.name}</span>
          )}
        </div>
        <div className="dialog-body">
          <button className="dialog-close" type="button" onClick={onClose} aria-label="Fechar">
            ×
          </button>
          <h2 id="movie-dialog-title">{movie.name}</h2>
          <div className="hero-meta">
            <span>{movie.year}</span>
            <Stars value={movie.rateStars} />
            {rentedAt ? <span>Alugado em {formatRentedAt(rentedAt)}</span> : null}
          </div>
          <p className="dialog-description">{movie.description}</p>
          {error ? <p className="form-error">{error}</p> : null}
          <div className="hero-actions">
            {rentedAt ? (
              <button className="catalog-btn ghost" type="button" disabled={busy} onClick={() => onReturn(movie)}>
                {busy ? 'Devolvendo...' : 'Devolver'}
              </button>
            ) : (
              <button className="catalog-btn" type="button" disabled={busy} onClick={() => onRent(movie)}>
                {busy ? 'Alugando...' : 'Alugar'}
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
