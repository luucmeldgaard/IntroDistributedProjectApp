package dtu.dk.introDistributedProjectApp.data;

import android.util.Log;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import dtu.dk.introDistributedProjectApp.server.*;

import dtu.dk.introDistributedProjectApp.data.SpaceName.*;
public class TupleSpaceConnection {
    private final static String REMOTE_URI = "tcp://10.0.2.2:9001/"; // Use localhost instead when running server in app
    private Space remoteSpace;
    private Space playerSpace;
    private Space questionSpace;
    private Space gameStateSpace;
    private Space answerSpace;

    private Map<SpaceName, RemoteSpace> spaces;
    private Boolean connected = false;


    public TupleSpaceConnection() throws IOException, InterruptedException {
        Log.i("TupleSpaceConnection", "Hello from TupleSpaceConnection");

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
        Space targetSpace = spaces.get(SpaceName.GAMESTATE);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + SpaceName.GAMESTATE);
        }

        // Query the tuple from the target space
        return (String) targetSpace.query(new ActualField(gameStateName))[0];
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
        Space targetSpace = spaces.get(SpaceName.PLAYER);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + SpaceName.PLAYER);
        }


        Object[] result = targetSpace.query(new FormalField(String.class), new ActualField(id), new FormalField(Integer.class));

        Log.i("TupleSpaceConnection", "Retrieved updated player: " + (String) result[0] + ", with id: " + (String) result[1] + ", and score: " + (int) result[2]);

        // Query the tuple from the target space
        return (int) targetSpace.query(new FormalField(String.class), new ActualField(id), new FormalField(Integer.class))[2];
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

        Log.i("TupleSpaceConnection", "Retrieved new question: ");

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
}