package maaanu.springbootsoap.model;

public class RatingResponse {
    private Rating rating;
    private int loanLimit;

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setLoanLimit(int loanLimit) {
        this.loanLimit = loanLimit;
    }

    public Rating getRating() {
        return rating;
    }

    public int getLoanLimit() {
        return loanLimit;
    }

    @Override
    public String toString() {
        return "RatingResponse{" +
                "rating=" + rating +
                ", loanLimit=" + loanLimit +
                '}';
    }
}
