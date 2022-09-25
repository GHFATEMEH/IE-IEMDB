package ProcessInputs;

import Entity.User;
import Film.Comment;
import Film.Movie;
import com.fasterxml.jackson.databind.node.*;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;
import static org.junit.Assert.assertEquals;

public class CommandHandlerTest {

    CommandHandler commandHandler;
    ArrayList<User> users;
    ArrayList<Movie> movies;
    ArrayList<Comment> comments;

    private void createUserArray(){
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", new TextNode("sajjad@ut.ac.ir"));
        userValues.put("password", new TextNode("123"));
        userValues.put("nickname", new TextNode("sajj"));
        userValues.put("name", new TextNode("sajjad"));
        userValues.put("birthDate", new TextNode("2021-01-01"));
        User user = new User(userValues);
        users = new ArrayList<User>();
        users.add(user);
    }

    private void createMovieArray(){
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
        Movie movie = new Movie(movieValues,null);
        Comment comment = createComment();
        movie.addComment(comment);
        movies = new ArrayList<Movie>();
        movies.add(movie);
        comments = new ArrayList<>();
        comments.add(comment);
    }

    private  ArrayNode createGenre(){
        ArrayNode genreArrayNode = JsonNodeFactory.instance.arrayNode();
        genreArrayNode.add("Action");
        return genreArrayNode;
    }

    private Comment createComment(){
        Map<String, Object> commentValues = new HashMap<>();
        commentValues.put("userEmail", new TextNode("sajjad@ut.ac.ir"));
        commentValues.put("text", new TextNode("good movie"));
        return new Comment(1, commentValues);
    }

    private Map<String, Object>  createCommandRateMovie(int movieId){
        Map<String, Object> values = new HashMap<>();
        values.put("userEmail", new TextNode("sajjad@ut.ac.ir"));
        values.put("movieId", new IntNode(movieId));
        values.put("score", new IntNode(8));
        return values;
    }

    private void setDataValidatorForCommandHandler() throws NoSuchFieldException, IllegalAccessException {
        DataValidator dataValidator = new DataValidator(null, users, movies, comments);
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

    private Map<String, Object>  createOutputRateMovie() {
        Map<String, Object> expectedValue = new HashMap<>();
        expectedValue.put("success", true);
        expectedValue.put("data", "movie rated successfully");
        return expectedValue;
    }

    private Map<String, Object>  createVoteCommentCommand(){
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("userEmail", new TextNode("sajjad@ut.ac.ir"));
        values.put("commentId", new IntNode(1));
        values.put("vote", new IntNode(1));
        return values;
    }

    private Map<String, Object>  createVoteCommentOutput(){
        Map<String, Object> expectedValue = new LinkedHashMap<>();
        expectedValue.put("success", true);
        expectedValue.put("data", "comment voted successfully");
        return expectedValue;
    }

    private  Map<String, Object> createGetMovieByGenreCommand(){
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("genre", new TextNode("Action"));
        return values;
    }

    private Map<String, Object> createGetMovieByGenreOutput(){
        Map<String, Object> expectedValue = new LinkedHashMap<>();
        expectedValue.put("success", true);
        expectedValue.put("data", "comment voted successfully");
        return expectedValue;
    }

    private  Map<String, Object> createAddToWatchListCommand(){
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("userEmail", new TextNode("sajjad@ut.ac.ir"));
        values.put("movieId", new IntNode(1));
        return values;
    }

    private Map<String, Object> createAddToWatchListOutput(){
        Map<String, Object> expectedValue = new LinkedHashMap<>();
        expectedValue.put("success", true);
        expectedValue.put("data", "movie added to watchlist successfully");
        return expectedValue;
    }

    @Before
    public void setUp() throws Exception {
        commandHandler = new CommandHandler();
        createUserArray();
        createMovieArray();
    }

    @Test
    public void testCommandRateUserNotFound() {
        Map<String, Object> values = createCommandRateMovie(2);
        try {
            commandHandler.handleRateMovie(values).toString();
        }
        catch (Exception e) {
            assertEquals( "UserNotFound", e.getMessage() );
        }
    }

    @Test
    public void testCommandRateMovieNotFound() throws Exception {
        Map<String, Object> values = createCommandRateMovie(2);
        setDataValidatorForCommandHandler();

        try {
            commandHandler.handleRateMovie(values);
        }
        catch (Exception e) {
            assertEquals( "MovieNotFound", e.getMessage());
        }
    }

    @Test
    public void testCommandRateMovie() throws Exception {
        Map<String, Object> values = createCommandRateMovie(1);
        setDataValidatorForCommandHandler();
        setMovieForCommandHandler();
        Map<String, Object> expectedValue = createOutputRateMovie();
        assertEquals( expectedValue, commandHandler.handleRateMovie(values));
    }

    @Test
    public void testVoteComment() throws Exception {
        Map<String, Object>  values = createVoteCommentCommand();
        setDataValidatorForCommandHandler();
        setMovieForCommandHandler();
        setCommentsForCommandHandler();
        Map<String, Object> expectedValue = createVoteCommentOutput();
        assertEquals( expectedValue, commandHandler.handleVoteComment(values));
    }

    @Test
    public void testGetMovieByGenre() throws NoSuchFieldException, IllegalAccessException, JSONException{
        Map<String, Object>  values = createGetMovieByGenreCommand();
        setMovieForCommandHandler();
        String expectedValue = "{success=true, data={\"MoviesListByGenre\":[{\"movieId\":1,\"name\":\"zahra\",\"director\":\"fatemeh\",\"genres\":[\"Action\"],\"rating\":null}]}}";
        assertEquals( expectedValue, commandHandler.handleGetMoviesByGenre(values).toString());
    }

    @Test
    public void testAddToWatchList() throws Exception {
        Map<String, Object>  values = createAddToWatchListCommand();
        setUsersCommandHandler();
        setDataValidatorForCommandHandler();
        setMovieForCommandHandler();
        Map<String, Object> expectedValue = createAddToWatchListOutput();
        assertEquals( expectedValue, commandHandler.handleAddToWatchList(values));
    }

    @After
    public void tearDown() throws Exception {
        commandHandler = null;
        users = null;
        movies = null;
        comments = null;
    }
}