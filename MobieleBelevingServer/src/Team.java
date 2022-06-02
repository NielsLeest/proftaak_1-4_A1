import java.util.LinkedList;
import java.util.List;

public class Team {

    private List<ServerClient> members;

    public Team() {
        members = new LinkedList<>();
    }

    public void join(ServerClient person) {
        members.add(person);
    }

    public void leave(ServerClient person) {
        members.remove(person);
        if (members.size() <= 1) {
            members.remove(0);
        }
    }
}
