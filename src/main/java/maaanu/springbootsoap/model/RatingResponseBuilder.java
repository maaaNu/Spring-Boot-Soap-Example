package maaanu.springbootsoap.model;

public class RatingResponseBuilder {
    private Rating rating;
    private int loanLimit;

    public static RatingResponseBuilder builder() {
        return new RatingResponseBuilder();
    }

    public RatingResponseBuilder withRating(Rating rating) {
        this.rating = rating;
        return this;
    }

    public RatingResponseBuilder withLimit(int loanLimit) {
        this.loanLimit = loanLimit;
        return this;
    }

    public RatingResponse create() {
        RatingResponse ratingResponse = new RatingResponse();
        ratingResponse.setLoanLimit(loanLimit);
        ratingResponse.setRating(rating);
        return ratingResponse;
    }

}
