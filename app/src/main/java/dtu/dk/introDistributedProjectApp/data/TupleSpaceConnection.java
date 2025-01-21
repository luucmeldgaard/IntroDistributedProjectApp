package dtu.dk.introDistributedProjectApp.data;

import android.util.Log;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import dtu.dk.introDistributedProjectApp.data.SpaceName.*;
public class TupleSpaceConnection {
    private static String REMOTE_URI = "tcp://10.0.2.2:9001/"; // Use localhost instead when running server in app
    private Space remoteSpace;
    private Space playerSpace;
    private Space questionSpace;
    private Space gameStateSpace;
    private Space answerSpace;

    private Map<SpaceName, RemoteSpace> spaces;
    private Boolean connected = false;


    public TupleSpaceConnection(String ip) throws IOException, InterruptedException {
        Log.i("TupleSpaceConnection", "Hello from TupleSpaceConnection");

        REMOTE_URI = "tcp://" + ip + ":9001/";

        while (true) {
            ExecutorService executor = null;
            try {
                executor = Executors.newSingleThreadExecutor();
                System.out.println("Attempting to connect to remote space...");

                Future<RemoteSpace> future = executor.submit(() -> new RemoteSpace(REMOTE_URI + "?keep"));

                remoteSpace = future.get(3, TimeUnit.SECONDS);
                Log.i("TupleSpaceConnection", "Successfully connected to remote space");
                connected = true;
                break; // Exit loop on success
            } catch (TimeoutException e) {
                Log.i("TupleSpaceConnection", "Connection attempt timed out. Retrying...");
            } catch (ExecutionException e) {
                Log.i("TupleSpaceConnection", "Failed to connect: " + e.getCause().getMessage());
            } catch (Exception e) {
                Log.i("TupleSpaceConnection", "Unexpected error: " + e.getMessage());
            } finally {
                if (executor != null) {
                    executor.shutdown();
                }
            }

        }

        this.spaces = Map.of(
                SpaceName.PLAYER, new RemoteSpace(REMOTE_URI + "playerConnectionSpace?keep"),
                SpaceName.QUESTION, new RemoteSpace(REMOTE_URI + "questionSpace?keep"),
                SpaceName.GAMESTATE, new RemoteSpace(REMOTE_URI + "gameStateSpace?keep"),
                SpaceName.ANSWER, new RemoteSpace(REMOTE_URI + "answerSpace?keep"),
                SpaceName.SCOREBOARD, new RemoteSpace(REMOTE_URI + "scoreboardSpace?keep")
        );
    }

    public final String queryGameStateAsString(String gameStateName) throws InterruptedException {
        // Retrieve the corresponding space
        //Log.w("TupleSpaceConnection", "Querying game state for: " + gameStateName + ", aka: " + SpaceName.GAMESTATE.getDisplayName());
        Space targetSpace = spaces.get(SpaceName.GAMESTATE);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + SpaceName.GAMESTATE.getDisplayName());
        }

        // Query the tuple from the target space
        String state = (String) targetSpace.query(new ActualField(gameStateName))[0];
        Log.i("queryGameStateAsString", "Got state: " + state);
        return state;
    }

    public void removePlayer(String ID){
        Space playerSpace = spaces.get(SpaceName.PLAYER);
        Space scoreboardSpace = spaces.get(SpaceName.SCOREBOARD);
        Space answerSpace = spaces.get(SpaceName.ANSWER);

        try{
            playerSpace.get(new FormalField(String.class), new ActualField(ID));
            scoreboardSpace.get(new FormalField(Integer.class), new ActualField(ID));
            answerSpace.get(new FormalField(String.class), new ActualField(ID));
            answerSpace.get(new FormalField(String.class), new ActualField(ID), new FormalField(Long.class));
            Log.i("TupleSpaceConnection", "Player removed from game successfully");
        } catch (InterruptedException e){
            Log.e("TupleSpaceConnection", "Player removal was interrupted");
        }
    }



    @SafeVarargs
    public final Object[] queryTuple(SpaceName spaceName, Class<?>... types) throws InterruptedException {
        // Retrieve the corresponding space
        Space targetSpace = spaces.get(spaceName);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + spaceName);
        }

        // Build the FormalField array
        FormalField[] fields = new FormalField[types.length];
        for (int i = 0; i < types.length; i++) {
            fields[i] = new FormalField(types[i]);
        }

        // Query the tuple from the target space
        return targetSpace.query(fields);
    }

    public final int queryScoreUpdate(String id) throws InterruptedException {
        Space targetSpace = spaces.get(SpaceName.SCOREBOARD);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + SpaceName.SCOREBOARD);
        }

        Log.i("TupleSpaceConnection", "Trying to retrieve updated player with id: " + id);
        Object[] result = targetSpace.queryp(new FormalField(Integer.class), new ActualField(id)); //TODO: I got an error here. We should do some error handling on the connection
        if (result != null){
            Log.i("TupleSpaceConnection", "Retrieved updated player: " + (Integer) result[0] + ", with id: " + (String) result[1]);
            // Query the tuple from the target space
            return (int) result[0];
        } else {
            Log.w("TupleSpaceConnection", "Player was not found on scoreboard. Assuming score is 0");
            return 0;
        }
    }

    public final List<Map.Entry<String, Integer>> queryQuestion() throws InterruptedException {
        Space targetSpace = spaces.get(SpaceName.QUESTION);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + SpaceName.QUESTION);
        }


        List<Object[]> request = targetSpace.queryAll(new FormalField(String.class), new FormalField(Integer.class));
        List<Map.Entry<String, Integer>> pairs = new ArrayList<>();

        for (Object[] part : request) {
            String text = (String) part[0];
            int type = (int) part[1];
            pairs.add(Map.entry(text, type));
        }

        Log.i("TupleSpaceConnection", "Retrieved new question");

        // Query the tuple from the target space
        return pairs;
    }

    public final void updateTuple(SpaceName spaceName, Object... items) throws InterruptedException {
        Space targetSpace = spaces.get(spaceName);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + spaceName.name());
        }

        targetSpace.put(items);
        Log.i("TupleSpaceConnection", "Tuple added to " + spaceName.name());
    }

    public Boolean getConnected() {
        return this.connected;
    }

    public final void setPlayerInScoreboard(int score, String id) throws InterruptedException {
        Space targetSpace = spaces.get(SpaceName.SCOREBOARD);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + SpaceName.SCOREBOARD.name());
        }

        targetSpace.put(score, id);
        Log.i("TupleSpaceConnection", "Added player to scoreboard");
    }
}