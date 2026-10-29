export function Stars({ value }: { value: number }) {
  return (
    <span className="stars" aria-label={`${value} de 5 estrelas`}>
      {Array.from({ length: 5 }, (_, index) => (
        <span key={index} className={index < value ? 'star is-on' : 'star'}>
          ★
        </span>
      ))}
    </span>
  )
}
