package Film;

import Utils.Functions;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

public class Comment {
    private int id;
    private LocalDate data;
    private LocalTime time;
    private String userEmail;
    private String text;
    private int likes;
    private int disLikes;

    private ArrayList<Vote> votes;

    public Comment(int _id, Map<String, Object> values){
        id = _id;
        data = LocalDate.now();
        time = LocalTime.now();
        userEmail = ((com.fasterxml.jackson.databind.node.TextNode) values.get("userEmail")).asText();
        text = ((com.fasterxml.jackson.databind.node.TextNode) values.get("text")).asText();
        votes = new ArrayList<>();

    }

    public int getId() {
        return id;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
        if(vote.getVote() == 1)
            likes++;
        else if(vote.getVote() == -1)
            disLikes++;
    }

    public boolean shouldVoteUpdate(Map<String, Object> values){
        String userEmail = ((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText();
        int voteValue = ((com.fasterxml.jackson.databind.node.IntNode) values.get("vote")).intValue();
        for(Vote vote : votes){
            if(vote.getUserEmail().equals(userEmail)){
                vote.updateVote(voteValue);
                return true;
            }
        }
        return false;
    }

    public JSONObject getCommentInfo() throws JSONException {
        Functions functions = new Functions();
        JSONObject info = functions.createJSONObjectWithOrder();
        info.put("commentId", id);
        info.put("userEmail", userEmail);
        info.put("text", text);
        info.put("like", likes);
        info.put("dislike", disLikes);
        return info;
    }
}
