import java.util.LinkedList;
import java.util.List;

public class MatchQueue {
    private List<ServerClient> waiting;

    public MatchQueue() {
        waiting = new LinkedList<>();
    }

    public void join(ServerClient person) {
        person.buddyQueue.addAll(waiting);
        waiting.add(person);
    }

    public void leave(ServerClient person) {
        waiting.remove(person);
        person.buddyQueue = new LinkedList<>();
        for (ServerClient serverClient : waiting) {
            serverClient.revokeRequest(person);
        }
    }

    public boolean contains(ServerClient person) {
        return waiting.contains(person);
    }
}
