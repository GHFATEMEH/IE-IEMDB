package Film;

public class Vote {
    private int userId;
    private int vote;

    public Vote(int _userId, int _vote) {
        userId = _userId;
        vote = _vote;
    }

    public int getUserId(){
        return userId;
    }

    public void updateVote(int newVote){
        vote = newVote;
    }

    public int getVote() {
        return vote;
    }
}
