import java.util.LinkedList;
import java.util.List;

public class MatchQueue {
    private List<ServerClient> queue;

    public MatchQueue() {
        queue = new LinkedList<>();
    }

    public void join(ServerClient person) {
        queue.add(person);
    }

    public void leave(ServerClient person) {
        queue.remove(person);
    }

    public boolean contains(ServerClient person) {
        return queue.contains(person);
    }
}
