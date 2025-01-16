package dtu.dk.introDistributedProjectApp.data;

import org.jspace.RemoteSpace;

import java.io.IOException;

public class Connection {
    private final static String PLAYERSPACE_URI = "tcp://127.0.0.1:9001/Players?keep";
    private final static String QUESTIONSPACE_URI = "tcp://127.0.0.1:9001/Question?keep";
    private final static String GAMESTATESPACE_URI = "tcp://127.0.0.1:9001/GameState?keep";
    private final static String ANSWERSPACE_URI = "tcp://127.0.0.1:9001/Answers?keep";

    private final RemoteSpace playerSpace;
    private final RemoteSpace questionSpace;
    private final RemoteSpace gameStateSpace;
    private final RemoteSpace answerSpace;
    Connection(){

        try {
            playerSpace = new RemoteSpace(PLAYERSPACE_URI);
            questionSpace = new RemoteSpace(QUESTIONSPACE_URI);
            gameStateSpace = new RemoteSpace(GAMESTATESPACE_URI);
            answerSpace = new RemoteSpace(ANSWERSPACE_URI);
        } catch (IOException e) {
            System.out.println("One of the connections to the spaces failed");
            throw new RuntimeException(e);
        }
    }
    public RemoteSpace getPlayerSpace(){
        return playerSpace;
    }
    public RemoteSpace getQuestionSpace() {
        return questionSpace;
    }
    public RemoteSpace getGameStateSpace() {
        return gameStateSpace;
    }
    public RemoteSpace getAnswerSpace() {
        return answerSpace;
    }
}
