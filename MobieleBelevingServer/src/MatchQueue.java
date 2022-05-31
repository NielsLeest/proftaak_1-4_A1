import java.util.LinkedList;
import java.util.List;

public class MatchQueue {
    private List<Person> queue;

    public MatchQueue() {
        queue = new LinkedList<>();
    }

    public void join(Person person) {
        queue.add(person);
    }

    public void leave(Person person) {
        queue.remove(person);
    }
}
