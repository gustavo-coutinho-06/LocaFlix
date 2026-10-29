import { useState } from 'react'
import type { Movie } from '../api/types'
import { previewText } from './movieText'
import { Stars } from './Stars'

type HeroBannerProps = {
  movie: Movie | null
  rented: boolean
  busy: boolean
  onPrimary: (movie: Movie) => void
  onMore: (movie: Movie) => void
}

export function HeroBanner({ movie, rented, busy, onPrimary, onMore }: HeroBannerProps) {
  const [imageFailed, setImageFailed] = useState(false)

  if (!movie) {
    return (
      <section className="hero hero-empty">
        <div className="hero-copy">
          <h1>Catálogo</h1>
          <p>Nenhum filme disponível no momento.</p>
        </div>
      </section>
    )
  }

  const showImage = Boolean(movie.imageUrl) && !imageFailed

  return (
    <section className="hero">
      {showImage ? (
        <img
          className="hero-image"
          src={movie.imageUrl}
          alt=""
          onError={() => setImageFailed(true)}
        />
      ) : null}
      <div className="hero-shade" />
      <div className="hero-copy">
        <p className="hero-kicker">Em destaque</p>
        <h1>{movie.name}</h1>
        <div className="hero-meta">
          <span>{movie.year}</span>
          <Stars value={movie.rateStars} />
        </div>
        <p className="hero-description">{previewText(movie.description)}</p>
        <div className="hero-actions">
          <button className="catalog-btn" type="button" disabled={busy} onClick={() => onPrimary(movie)}>
            {rented ? 'Devolver' : 'Alugar'}
          </button>
          <button className="catalog-btn ghost" type="button" onClick={() => onMore(movie)}>
            Mais informações
          </button>
        </div>
      </div>
    </section>
  )
}
