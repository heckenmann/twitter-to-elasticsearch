package de.heckenmann.tte;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.heckenmann.tte.model.Document;
import de.heckenmann.tte.model.Mission;

/**
 * Created by Ben on 24.07.2016.
 */
public class MissionObserver {

    private static final long SLEEP = 5000;

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionObserver.class);
    private static final String NODE_NAME_PREFIX = "<node>" + Parameter.NODE_NAME + "</node>";

    private final ElasticsearchSettings settings;
    private final Map<String, Thread> threads;

    public MissionObserver(final ElasticsearchSettings settings, final Map<String, Thread> threads) {
        this.settings = settings;
        this.threads = threads;
    }

    public void start() {
        while (true) {
            final List<Document<Mission>> missions = this.settings.getMissions();
            final Set<String> validKeys = missions.stream().map(d -> generateUID(d)).collect(Collectors.toSet());
            // Missionen finden, die noch nicht gestartet wurden.
            for (final Document<Mission> d : missions) {
                final String uid = generateUID(d);
                if (uid.startsWith(NODE_NAME_PREFIX) && !this.threads.containsKey(uid)) {
                    LOGGER.info("Found new mission: " + uid);
                    final Thread newThread = new Thread(new TwitterClient(d.getSource()));
                    newThread.setName(uid);
                    this.threads.put(uid, newThread);
                    newThread.start();
                }
            }
            // Gestartete Missionen finden, die gestoppt werden sollen.
            for (final String uid : this.threads.keySet()) {
                if (uid.startsWith(NODE_NAME_PREFIX) && !validKeys.contains(uid) && this.threads.get(uid).isAlive() && !this.threads.get(uid).isInterrupted()) {
                    LOGGER.info("Stopping old mission: " + uid);
                    this.threads.get(uid).interrupt();
                    this.threads.remove(uid);
                }
            }

            try {
                Thread.sleep(SLEEP);
            } catch (final InterruptedException e) {
                LOGGER.warn(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private static String generateUID(final Document<Mission> d) {
        return "<node>" + d.getSource().getNode() + "</node>"
                + "<id>" + d.getId() + "</id>"
                + "<version>" + d.getVersion() + "</version>";
    }
}
