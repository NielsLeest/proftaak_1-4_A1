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

    /**
     * removes a client from the team and destroys the team if only one person remains
     * @param person
     */
    public void leave(ServerClient person) {
        members.remove(person);
        if (members.size() == 1) {
            members.get(0).leaveTeam();
        }
    }

    /**
     * tries to start the game
     * @return true if the game can be started
     */
    public boolean game() {
        for (ServerClient s : members) {
            if (!s.startgame) {
                return false;
            }

        }
        return true;
    }
}
