package IEMDB.ProcessOutputs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;


public class OutputGenerator {

//    public String generateGetActorByIdOutput(String actorInfo, ArrayList<String> moviesInfo) throws IOException {
//        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/actor.html");
//        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
//        content = content.replace("$actor_info", actorInfo);
//        content = content.replace("$movies_info", makeMoviesInfo(moviesInfo));
//        return content;
//    }

    public String makeMoviesInfo(ArrayList<String> moviesInfo) {
        String data = "";
        for(String s : moviesInfo)
            data += "        <tr>\n" + s + "\n" +
                    "        </tr>\n";
        return data;
    }

//    public String generateGetWatchListOutput(String userInfo, ArrayList<String> watchList) throws IOException {
//        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/watchList.html");
//        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
//        content = content.replace("$user_info", userInfo);
//        content = content.replace("$movies_info", makeMoviesInfo(watchList));
//        return content;
//    }
//
//    public String generate404ErrorOutput() throws IOException {
//        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/404.html");
//        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
//        return content;
//    }
//
//    public String generate200Output() throws IOException {
//        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/200.html");
//        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
//        return content;
//    }
//
//    public String generate403ErrorOutput() throws IOException {
//        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/403.html");
//        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
//        return content;
//    }
//
//    public String generateGetMovieOutput(String movieInfoList, String commentsInfo) throws IOException {
//        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/movie.html");
//        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
//        content = content.replace("$movie_info", movieInfoList);
//        content = content.replace("$comments_info", commentsInfo);
//        return content;
//    }
}




