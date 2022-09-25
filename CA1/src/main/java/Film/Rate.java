package Film;

public class Rate {
    private String userEmail;
    private int score;

    public Rate(String _userEmail, int _score) {
        userEmail = _userEmail;
        score = _score;
    }

    public String getEmail() {
        return userEmail;
    }

    public void setNewScore(int newScore) {
        score = newScore;
    }

    public int getScore() {
        return score;
    }
}
