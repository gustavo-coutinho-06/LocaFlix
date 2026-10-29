package locaflix.corporation.locaflix.infrastructure.web.movie;

public record MovieRequest(String name, String imageUrl, String description, int rateStars, int year) {
}
