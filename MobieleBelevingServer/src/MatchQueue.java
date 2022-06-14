import java.util.LinkedList;
import java.util.List;

public class MatchQueue {
    private List<ServerClient> queue;

    public MatchQueue() {
        queue = new LinkedList<>();
    }

    public void join(ServerClient person) {
        person.buddyQueue.addAll(queue);
        for (ServerClient serverClient : queue) {
            serverClient.buddyQueue.add(person);
        }
        queue.add(person);
    }

    public void leave(ServerClient person) {
        queue.remove(person);
        person.buddyQueue = new LinkedList<>();
        for (ServerClient serverClient : queue) {
            serverClient.revokeRequest(person);
        }
    }

    public boolean contains(ServerClient person) {
        return queue.contains(person);
    }

    public List<ServerClient> getQueue() {
        return queue;
    }
}
