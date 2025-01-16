package dtu.dk.introDistributedProjectApp.data;

import org.jspace.*;

public class test {
    private Space inbox;

    public void Test() {
        this.inbox = new SequentialSpace();
    }

    public void putMessage(String message) throws InterruptedException {
        inbox.put(message);
    }

    public String getMessage() throws InterruptedException {
        Object[] tuple = inbox.get(new FormalField(String.class));
        return (String) tuple[0];
    }

}