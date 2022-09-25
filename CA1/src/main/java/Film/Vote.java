package Film;

import java.util.Map;

public class Vote {
    private String userEmail;
    private int vote;

    public Vote(Map<String, Object> values) {
        userEmail = ((com.fasterxml.jackson.databind.node.TextNode) values.get("userEmail")).asText();
        vote = ((com.fasterxml.jackson.databind.node.IntNode) values.get("vote")).intValue();
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void updateVote(int newVote){
        vote = newVote;
    }

    public int getVote() {
        return vote;
    }
}
