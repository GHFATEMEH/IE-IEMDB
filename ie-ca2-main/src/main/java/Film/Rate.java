package Film;

public class Rate {
    private int userId;
    private int score;

    public Rate(int _userId, int _score) {
        userId = _userId;
        score = _score;
    }

    public void setNewScore(int newScore) {
        score = newScore;
    }

    public int getScore() {
        return score;
    }

    public int getUserId() {
        return userId;
    }
}
