export type TokenResponse = {
  token: string
  expiresAt: string
}

export type Plan = {
  id: string
  name: string
  allowedRentalQuantity: number
}

export type User = {
  id: string
  name: string
  age: number
  email: string
  plan: Plan
}

export type CreateUserInput = {
  name: string
  age: number
  password: string
  email: string
  planId: string
}

export type Movie = {
  id: string
  name: string
  imageUrl: string
  description: string
  rateStars: number
  year: number
}

export type Rental = {
  id: string
  userId: string
  movie: Movie
  rentedAt: string
}
