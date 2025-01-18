package dtu.dk.introDistributedProjectApp.data;

import android.util.Log;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import dtu.dk.introDistributedProjectApp.data.SpaceName.*;
public class TupleSpaceConnection {
    private final static String REMOTE_URI = "tcp://10.0.2.2:9001/";
    private Space remoteSpace;
    private Space playerSpace;
    private Space questionSpace;
    private Space gameStateSpace;
    private Space answerSpace;

    private Map<SpaceName, RemoteSpace> spaces;


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
                SpaceName.PLAYER, new RemoteSpace(REMOTE_URI + "Players?keep"),
                SpaceName.QUESTION, new RemoteSpace(REMOTE_URI + "Question?keep"),
                SpaceName.GAMESTATE, new RemoteSpace(REMOTE_URI + "GameState?keep"),
                SpaceName.ANSWER, new RemoteSpace(REMOTE_URI + "Answers?keep")
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
        // Retrieve the corresponding space
        Space targetSpace = spaces.get(SpaceName.PLAYER);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + SpaceName.PLAYER);
        }

        // Query the tuple from the target space
        return (int) targetSpace.query(new FormalField(String.class), new ActualField(id), new FormalField(Integer.class))[2];
    }

    public final void updateTuple(SpaceName spaceName, Object... items) throws InterruptedException {
        Space targetSpace = spaces.get(spaceName);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + spaceName.name());
        }

        targetSpace.put(items);
        Log.i("TupleSpaceConnection", "Tuple added to " + spaceName.name());
    }
}