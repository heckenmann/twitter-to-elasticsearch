package de.heckenmann.tte;

import de.heckenmann.tte.model.Document;
import de.heckenmann.tte.model.Mission;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ben on 24.07.2016.
 */
public class MissionOberserver {

    private static final long SLEEP = 5000;

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionOberserver.class);
    private static final String NODE_NAME_PREFIX = "<node>" + Parameter.NODE_NAME + "</node>";

    private final ElasticsearchSettings settings;
    private final Map<String, Thread> threads;

    public MissionOberserver(ElasticsearchSettings settings, Map<String, Thread> threads) {
        this.settings = settings;
        this.threads = threads;
    }

    public void start() {
        while (true) {
            List<Document<Mission>> missions = settings.getMissions();
            Set<String> validKeys = missions.stream().map(d -> generateUID(d)).collect(Collectors.toSet());
            // Missionen finden, die noch nicht gestartet wurden.
            for (Document<Mission> d : missions) {
                String uid = generateUID(d);
                if (uid.startsWith(NODE_NAME_PREFIX) && !threads.containsKey(uid)) {
                    LOGGER.info("Found new mission: " + uid);
                    Thread newThread = new Thread(new TwitterClient(d.getSource()));
                    newThread.setName(uid);
                    threads.put(uid, newThread);
                    newThread.start();
                }
            }
            // Gestartete Missionen finden, die gestoppt werden sollen.
            for (String uid : threads.keySet()) {
                if (uid.startsWith(NODE_NAME_PREFIX) && !validKeys.contains(uid) && threads.get(uid).isAlive() && !threads.get(uid).isInterrupted()) {
                    LOGGER.info("Stopping old mission: " + uid);
                    threads.get(uid).interrupt();
                    threads.remove(uid);
                }
            }

            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }

    private static String generateUID(Document<Mission> d) {
        return "<node>" + d.getSource().getNode() + "</node>"
                + "<id>" + d.getId() + "</id>"
                + "<version>" + d.getVersion() + "</version>";
    }
}
