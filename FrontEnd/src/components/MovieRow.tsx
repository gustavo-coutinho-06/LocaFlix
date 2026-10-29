import { useRef, useState } from 'react'
import type { Movie } from '../api/types'
import { formatRentedAt } from './movieText'

type MovieRowItem = {
  movie: Movie
  rentedAt?: string
}

type MovieRowProps = {
  title: string
  items: MovieRowItem[]
  empty: string
  busy?: boolean
  onOpen: (movie: Movie) => void
  onReturn?: (movie: Movie) => void
}

export function MovieRow({ title, items, empty, busy = false, onOpen, onReturn }: MovieRowProps) {
  const scroller = useRef<HTMLDivElement>(null)

  function scrollRow(direction: number) {
    const node = scroller.current
    if (!node) {
      return
    }
    node.scrollBy({ left: direction * node.clientWidth * 0.8, behavior: 'smooth' })
  }

  return (
    <section className="movie-row">
      <div className="movie-row-head">
        <h2>{title}</h2>
        {items.length > 0 ? (
          <div className="row-nav">
            <button type="button" aria-label={`Rolar ${title} para a esquerda`} onClick={() => scrollRow(-1)}>
              ‹
            </button>
            <button type="button" aria-label={`Rolar ${title} para a direita`} onClick={() => scrollRow(1)}>
              ›
            </button>
          </div>
        ) : null}
      </div>
      {items.length === 0 ? (
        <p className="row-empty">{empty}</p>
      ) : (
        <div className="row-scroller" ref={scroller}>
          {items.map((item) => (
            <PosterCard
              key={item.movie.id}
              movie={item.movie}
              rentedAt={item.rentedAt}
              onOpen={onOpen}
              onReturn={onReturn}
              busy={busy}
            />
          ))}
        </div>
      )}
    </section>
  )
}

function PosterCard({
  movie,
  rentedAt,
  onOpen,
  onReturn,
  busy = false,
}: {
  movie: Movie
  rentedAt?: string
  onOpen: (movie: Movie) => void
  onReturn?: (movie: Movie) => void
  busy?: boolean
}) {
  const [failed, setFailed] = useState(false)
  const showImage = Boolean(movie.imageUrl) && !failed

  return (
    <article className="poster-card">
      <button className="poster-open" type="button" onClick={() => onOpen(movie)}>
        {showImage ? (
          <img src={movie.imageUrl} alt="" onError={() => setFailed(true)} />
        ) : (
          <span className="poster-fallback">{movie.name}</span>
        )}
        <span className="poster-caption">
          <strong>{movie.name}</strong>
          <small>{rentedAt ? formatRentedAt(rentedAt) : movie.year}</small>
        </span>
      </button>
      {onReturn ? (
        <button className="poster-return" type="button" disabled={busy} onClick={() => onReturn(movie)}>
          Devolver
        </button>
      ) : null}
    </article>
  )
}
