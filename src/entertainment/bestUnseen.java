package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class bestUnseen {
    private final int id;
    private final User user;
    private List<Video> videos;

    public bestUnseen(List<Video> videos, int id, User user, String username) {
        this.id = id;
        this.user = user;
        this.videos = videos;
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        descSort();
        for(Video v : videos) {
            if(!user.getHistory().containsKey(v.getTitle())) {

                return fileWriter.writeFile(id, null, "BestRatedUnseenRecommendation result: "
                                            + v.getTitle());
            }
        }
        return fileWriter.writeFile(id, null, "BestRatedUnseenRecommendation cannot be applied!");
    }
    private void descSort() {
        Comparator<Video> comparator = new Comparator<Video>() {
            @Override
            public int compare(final Video v1, final Video v2) {
                if (v1.calcAvg() != v2.calcAvg()) {
                    return Double.compare(v2.calcAvg(), v1.calcAvg());
                } else {
                    return videos.indexOf(v1) - videos.indexOf(v2);
                }
            }
        };
        Collections.sort(videos, comparator);
    }
}
