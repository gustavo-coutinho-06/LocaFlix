import { useCallback, useEffect, useMemo, useState } from 'react'
import { listMovies, listRentals, rentMovie, returnRental, toErrorMessage } from '../api/client'
import type { Movie, Rental } from '../api/types'
import { useAuth } from '../auth/AuthContext'
import { CatalogHeader } from '../components/CatalogHeader'
import { HeroBanner } from '../components/HeroBanner'
import { MovieDialog } from '../components/MovieDialog'
import { MovieRow } from '../components/MovieRow'
import '../catalog.css'

const LIMIT_MESSAGE = 'Você atingiu o limite de aluguéis do seu plano.'

export function HomePage() {
  const { token, user, logout } = useAuth()
  const [movies, setMovies] = useState<Movie[]>([])
  const [rentals, setRentals] = useState<Rental[]>([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')
  const [selected, setSelected] = useState<Movie | null>(null)
  const [busy, setBusy] = useState(false)
  const [actionError, setActionError] = useState('')
  const [loggingOut, setLoggingOut] = useState(false)

  useEffect(() => {
    if (!token) {
      return
    }

    let active = true
    Promise.all([listMovies(), listRentals(token)])
      .then(([movieList, rentalList]) => {
        if (!active) {
          return
        }
        setMovies(Array.isArray(movieList) ? movieList : [])
        setRentals(Array.isArray(rentalList) ? rentalList : [])
      })
      .catch((error: unknown) => {
        if (active) {
          setLoadError(toErrorMessage(error))
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

  const rentalByMovie = useMemo(() => {
    const map = new Map<string, Rental>()
    for (const rental of rentals) {
      map.set(rental.movie.id, rental)
    }
    return map
  }, [rentals])

  const featured = useMemo(() => {
    if (movies.length === 0) {
      return null
    }
    return movies.reduce((best, movie) => (movie.rateStars > best.rateStars ? movie : best))
  }, [movies])

  const openMovie = useCallback((movie: Movie) => {
    setActionError('')
    setSelected(movie)
  }, [])

  const closeMovie = useCallback(() => {
    setSelected(null)
    setActionError('')
  }, [])

  const rent = useCallback(
    async (movie: Movie) => {
      if (!token) {
        return
      }
      if (user && rentals.length >= user.plan.allowedRentalQuantity) {
        setSelected(movie)
        setActionError(LIMIT_MESSAGE)
        return
      }

      setBusy(true)
      setActionError('')
      try {
        const created = await rentMovie(token, movie.id)
        setRentals((current) => [created, ...current.filter((item) => item.movie.id !== movie.id)])
        setSelected(movie)
      } catch (error) {
        setSelected(movie)
        setActionError(toErrorMessage(error))
      } finally {
        setBusy(false)
      }
    },
    [rentals.length, token, user],
  )

  const giveBack = useCallback(
    async (movie: Movie) => {
      if (!token) {
        return
      }
      const rental = rentalByMovie.get(movie.id)
      if (!rental) {
        return
      }

      setBusy(true)
      setActionError('')
      try {
        await returnRental(token, rental.id)
        setRentals((current) => current.filter((item) => item.id !== rental.id))
      } catch (error) {
        setSelected(movie)
        setActionError(toErrorMessage(error))
      } finally {
        setBusy(false)
      }
    },
    [rentalByMovie, token],
  )

  async function handleLogout() {
    setLoggingOut(true)
    await logout()
  }

  const selectedRental = selected ? rentalByMovie.get(selected.id) : undefined
  const featuredRental = featured ? rentalByMovie.get(featured.id) : undefined

  return (
    <div className="catalog">
      <CatalogHeader
        name={user?.name}
        used={rentals.length}
        limit={user?.plan.allowedRentalQuantity}
        loggingOut={loggingOut}
        onLogout={handleLogout}
      />
      {loading ? (
        <p className="catalog-status">Carregando catálogo...</p>
      ) : (
        <>
          {loadError ? (
            <p className="catalog-status catalog-status-error">{loadError}</p>
          ) : (
            <>
          <HeroBanner
            key={featured?.id ?? 'empty'}
            movie={featured}
            rented={Boolean(featuredRental)}
            busy={busy}
            onPrimary={(movie) => {
              if (rentalByMovie.has(movie.id)) {
                void giveBack(movie)
                return
              }
              void rent(movie)
            }}
            onMore={openMovie}
          />
          {actionError && !selected ? <p className="catalog-toast">{actionError}</p> : null}
          <MovieRow
            title="Catálogo"
            items={movies.map((movie) => ({ movie }))}
            empty="Nenhum filme disponível."
            onOpen={openMovie}
          />
          <MovieRow
            title="Meus aluguéis"
            items={rentals.map((rental) => ({ movie: rental.movie, rentedAt: rental.rentedAt }))}
            empty="Você ainda não alugou nenhum filme."
            busy={busy}
            onOpen={openMovie}
            onReturn={(movie) => {
              void giveBack(movie)
            }}
          />
            </>
          )}
        </>
      )}
      {selected ? (
        <MovieDialog
          key={selected.id}
          movie={selected}
          rentedAt={selectedRental?.rentedAt}
          busy={busy}
          error={actionError}
          onClose={closeMovie}
          onRent={(movie) => {
            void rent(movie)
          }}
          onReturn={(movie) => {
            void giveBack(movie)
          }}
        />
      ) : null}
    </div>
  )
}
