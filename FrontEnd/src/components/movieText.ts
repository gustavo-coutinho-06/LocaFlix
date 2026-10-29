export function formatRentedAt(value: string) {
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  }).format(date)
}

export function previewText(text: string, max = 180) {
  const clean = text.trim()
  if (clean.length <= max) {
    return clean
  }
  return `${clean.slice(0, max).trimEnd()}…`
}
