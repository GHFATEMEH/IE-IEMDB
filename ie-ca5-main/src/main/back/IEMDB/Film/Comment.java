package IEMDB.Film;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

public class Comment {
    private int id;
    private LocalDate data;
    private LocalTime time;
    private String nickname;
    private String text;
    private int likes;
    private int disLikes;

    private ArrayList<Vote> votes;


    public Comment(int _id, Map<String, Object> values, String _nickname){
        id = _id;
        data = LocalDate.now();
        time = LocalTime.now();
        nickname = _nickname;
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

    public boolean shouldVoteUpdate(int user_id, int vote){
        for(Vote v : votes){
            if(v.getUserId() == user_id){
                updateLike(vote, v.getVote());
                v.updateVote(vote);
                return true;
            }
        }
        return false;
    }

    private void updateLike(int newVote, int oldVote) {
        if(newVote == oldVote)
            return;
        else {
            if(newVote == -1) {
                likes--;
                disLikes++;
            }
            else{
                likes++;
                disLikes--;
            }
        }
    }

    public String getCommentInfo() {
        String data = "      <tr>\n" +
                "        <td>@" + nickname + "</td>\n" +
                "        <td>" + text + "</td>\n";
        data += "        <td>\n" +
                "          <form action=\"\" method=\"POST\">\n" +
                "            <label for=\"\">" + likes + "</label>\n" +
                "            <input\n" +
                "              id=\"form_comment_id\"\n" +
                "              type=\"hidden\"\n" +
                "              name=\"comment_id\"\n" +
                "              value=\"" + id + "\"\n/>" +
                "            <button type=\"submit\" name=\"action\" , value=\"like\">like</button>\n" +
                "          </form>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "          <form action=\"\" method=\"POST\">\n" +
                "            <label for=\"\">" + disLikes + "</label>\n" +
                "            <input\n" +
                "              id=\"form_comment_id\"\n" +
                "              type=\"hidden\"\n" +
                "              name=\"comment_id\"\n" +
                "              value=\"" + id + "\"\n/>" +
                "             <button type=\"submit\" name=\"action\" , value=\"dislike\" >dislike</button>\n" +
                "          </form>\n" +
                "        </td>\n";
        data += "      </tr>\n";
        return data;
    }
}
