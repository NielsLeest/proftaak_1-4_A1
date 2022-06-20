import java.util.LinkedList;
import java.util.List;

public class MatchQueue {
    private List<ServerClient> queue;

    public MatchQueue() {
        queue = new LinkedList<>();
    }

    /**
     * adds a person to the queue
     * @param person the person joining
     */
    public void join(ServerClient person) {
        person.buddyQueue.addAll(queue);
        for (ServerClient serverClient : queue) {
            serverClient.buddyQueue.add(person);
        }
        queue.add(person);
    }

    /**
     * removes a person from the queue
     * @param person
     */
    public void leave(ServerClient person) {
        queue.remove(person);
        person.buddyQueue = new LinkedList<>();
        for (ServerClient serverClient : queue) {
            serverClient.revokeRequest(person);
        }
    }

    /**
     * checks whether a person is in the queue
     * @param person the person that needs to be checked for
     * @return true if said person is in the queue
     */
    public boolean contains(ServerClient person) {
        return queue.contains(person);
    }

    /**
     * returns the queue
     * @return a list containing all currently queued clients
     */
    public List<ServerClient> getQueue() {
        return queue;
    }
}
