package ProcessInputs;

import Entity.Actor;
import Entity.User;
import Film.Comment;
import Film.Movie;
import com.fasterxml.jackson.databind.node.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CommandHandlerTest {
    private Context context;
    private CommandHandler commandHandler;
    private ArrayList<Actor> actors;
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Comment> comments;
    private Javalin app;

    private void createActorArray() {
        Map<String, Object> actorValues = new HashMap<>();
        actorValues.put("id", new IntNode(1));
        actorValues.put("name", new TextNode("hasan"));
        actorValues.put("birthDate", new TextNode("2000-02-02"));
        actorValues.put("nationality", new TextNode("Iranian"));
        Actor actor = new Actor(actorValues);
        actors = new ArrayList<Actor>();
        actors.add(actor);
    }

    private void createUserArray() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", new TextNode("sajjad@ut.ac.ir"));
        userValues.put("password", new TextNode("123"));
        userValues.put("nickname", new TextNode("sajj"));
        userValues.put("name", new TextNode("sajjad"));
        userValues.put("birthDate", new TextNode("2021-01-01"));
        User user = new User(userValues, 1);
        users = new ArrayList<User>();
        users.add(user);
    }

    private void createMovieArray() {
        //create movie array list
        Map<String, Object> movieValues = new HashMap<>();
        movieValues.put("id", new IntNode(1));
        movieValues.put("name", new TextNode("zahra"));
        movieValues.put("summary", new TextNode("good movie"));
        movieValues.put("releaseDate", new TextNode("2000-01-02"));
        movieValues.put("director", new TextNode("fatemeh"));
        movieValues.put("imdbRate", new DoubleNode(1));
        movieValues.put("duration", new IntNode(1));
        movieValues.put("ageLimit", new IntNode(1));
        movieValues.put("ratingCount", new DoubleNode(1));
        movieValues.put("writers", JsonNodeFactory.instance.arrayNode());
        movieValues.put("genres", createGenre());
        Movie movie = new Movie(movieValues, actors);
        Comment comment = createComment();
        movie.addComment(comment);
        movies = new ArrayList<Movie>();
        movies.add(movie);
        comments = new ArrayList<>();
        comments.add(comment);
    }

    private Comment createComment() {
        Map<String, Object> commentValues = new HashMap<>();
        commentValues.put("userEmail", new TextNode("sajjad@ut.ac.ir"));
        commentValues.put("text", new TextNode("good movie"));
        return new Comment(1, commentValues, "nickname");
    }

    private ArrayNode createGenre() {
        ArrayNode genreArrayNode = JsonNodeFactory.instance.arrayNode();
        genreArrayNode.add("Action");
        return genreArrayNode;
    }

    private void setDataValidatorForCommandHandler() throws NoSuchFieldException, IllegalAccessException {
        DataValidator dataValidator = new DataValidator(actors, users, movies, comments);
        final Field dataValidatorField = commandHandler.getClass().getDeclaredField("dataValidator");
        dataValidatorField.setAccessible(true);
        dataValidatorField.set(commandHandler, dataValidator);
    }

    private void setMovieForCommandHandler() throws NoSuchFieldException, IllegalAccessException {
        final Field moviesField = commandHandler.getClass().getDeclaredField("movies");
        moviesField.setAccessible(true);
        moviesField.set(commandHandler, movies);
    }

    private void setCommentsForCommandHandler() throws NoSuchFieldException, IllegalAccessException {
        final Field commentsField = commandHandler.getClass().getDeclaredField("comments");
        commentsField.setAccessible(true);
        commentsField.set(commandHandler, comments);
    }

    private void setUsersCommandHandler() throws NoSuchFieldException, IllegalAccessException {
        final Field usersField = commandHandler.getClass().getDeclaredField("users");
        usersField.setAccessible(true);
        usersField.set(commandHandler, users);
    }

    @Before
    public void setUp() throws Exception {
        createActorArray();
        createUserArray();
        createMovieArray();
        commandHandler = new CommandHandler();
    }

    @Test
    //403
    public void testInvalidRateScoreRateMovie() throws IOException, NoSuchFieldException, IllegalAccessException {
        setDataValidatorForCommandHandler();
        String output = commandHandler.handleRateMovie("1", "1", "11");
        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/403.html");
        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
        assertEquals(content, output);
    }

    @Test
    //404
    public void testInvalidUserRateMovie() throws IOException, NoSuchFieldException, IllegalAccessException {
        setDataValidatorForCommandHandler();
        String output = commandHandler.handleRateMovie("100", "1", "1");
        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/404.html");
        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
        assertEquals(content, output);
    }

    @Test
    //404
    public void testInvalidMovieRateMovie() throws IOException, NoSuchFieldException, IllegalAccessException {
        setDataValidatorForCommandHandler();
        String output = commandHandler.handleRateMovie("1", "100" , "1");
        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/404.html");
        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
        assertEquals(content, output);
    }

    @Test
    public void testRateMovie() throws NoSuchFieldException, IllegalAccessException, IOException {
        setDataValidatorForCommandHandler();
        setMovieForCommandHandler();
        setUsersCommandHandler();
        String output = commandHandler.handleRateMovie("1", "1", "1");
        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/200.html");
        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
        assertEquals(content, output);
    }

    //404
    @Test
    public void testUserInvalidWatchList() throws IOException, NoSuchFieldException, IllegalAccessException {
        setDataValidatorForCommandHandler();
        String output = commandHandler.handleGetWatchList("2");
        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/404.html");
        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
        assertEquals(content, output);
    }

    @Test
    public void testWatchList() throws IOException, NoSuchFieldException, IllegalAccessException {
        setDataValidatorForCommandHandler();
        setUsersCommandHandler();
        String output = commandHandler.handleGetWatchList("1");
        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/watchListTest.html");
        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
        assertEquals(content, output);
    }

    @Test
    public void testSearchByYear() throws NoSuchFieldException, IllegalAccessException, IOException {
        setMovieForCommandHandler();
        String output = commandHandler.handleSearchByYear("1900", "2001");
        File htmlTemplateFile = new File("./src/main/java/ProcessOutputs/templates/searchByYearTest.html");
        String content = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
        assertEquals(content, output);
    }

}