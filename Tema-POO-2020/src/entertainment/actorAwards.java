package entertainment;

import actor.ActorsAwards;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class actorAwards {
    private final List<Actor> actors;
    private final List<String> awards;
    private final List<Actor> filteredActors = new ArrayList<Actor>();
    private final int id;
    private final String sortType;

    public  actorAwards(List<Actor> actors, List<String> awards, int id, String sortType) {
        this.actors = actors;
        this.awards = awards;
        this.id = id;
        this.sortType = sortType;
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        setFilteredActors();
        List<String> actorsToPrint = new ArrayList<String>();

        if (sortType.equals("asc")) {
            ascsort();
        } else {
            descsort();
        }
        for(Actor a : filteredActors) {
            actorsToPrint.add(a.getName());
        }
        return fileWriter.writeFile(id, null, "Query result: " + actorsToPrint);
    }
    private void setFilteredActors() {
        for(Actor a : actors) {
            boolean ok = findAwards(a);
            if(ok == true) {
                filteredActors.add(a);
            }
        }
    }
    private boolean findAwards( Actor actor) {
        for(String award : awards) {
            boolean found = false;
            for(Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                if(entry.getKey().toString().equals(award)) {
                    found = true;
                }
            }
            if(found == false) return  false;
        }
        return true;
    }
    private void ascsort() {
        Comparator<Actor> comparator = new Comparator<Actor>() {
            @Override
            public int compare(final Actor a1, final Actor a2) {
                if (a1.getAwardsNumber() != a2.getAwardsNumber()) {
                    return Integer.compare(a1.getAwardsNumber(), a2.getAwardsNumber());
                } else {
                    return a1.getName().compareTo(a2.getName());
                }
            }
        };


        Collections.sort(filteredActors, comparator);
        for(Actor a:filteredActors) {
            System.out.println(a.getName() + "  " + a.getAwardsNumber());
        }
    }
    private void descsort() {
        Comparator<Actor> comparator = new Comparator<Actor>() {
            @Override
            public int compare(final Actor a1, final Actor a2) {
                if (a1.getAwardsNumber() != a2.getAwardsNumber()) {
                    return Integer.compare(a2.getAwardsNumber(), a1.getAwardsNumber());
                } else {
                    return a2.getName().compareTo(a1.getName());
                }
            }
        };

        Collections.sort(filteredActors, comparator);
    }
}
