package dtu.dk.introDistributedProjectApp.data;

import android.util.Log;

import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.Space;

import java.io.IOException;
import java.util.Map;
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
        /*this.remoteSpace = new RemoteSpace(REMOTE_URI + "?keep");
        this.playerSpace = new RemoteSpace(REMOTE_URI + "Players?keep");
        this.questionSpace = new RemoteSpace(REMOTE_URI + "Question?keep");
        this.gameStateSpace = new RemoteSpace(REMOTE_URI + "GameState?keep");
        this.answerSpace = new RemoteSpace(REMOTE_URI + "Answers?keep");*/

        this.spaces = Map.of(
                SpaceName.ROOT, new RemoteSpace(REMOTE_URI + "?keep"),
                SpaceName.PLAYER, new RemoteSpace(REMOTE_URI + "Players?keep"),
                SpaceName.QUESTION, new RemoteSpace(REMOTE_URI + "Question?keep"),
                SpaceName.GAMESTATE, new RemoteSpace(REMOTE_URI + "GameState?keep"),
                SpaceName.ANSWER, new RemoteSpace(REMOTE_URI + "Answers?keep")
        );
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

    public final void updateTuple(SpaceName spaceName, Object... items) throws InterruptedException {
        Space targetSpace = spaces.get(spaceName);

        if (targetSpace == null) {
            throw new IllegalArgumentException("Invalid SpaceName: " + spaceName.name());
        }

        targetSpace.put("noget");
        Log.i("TupleSpaceConnection", "Tuple added to " + spaceName.name());
    }
}