package de.heckenmann.tte;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ben on 23.07.2016.
 */
public class TwitterToElasticsearch {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterToElasticsearch.class);

    public static void main(String... args) {
        BasicConfigurator.configure();
        LOGGER.info("TwitterToElasticsearch starting...");
        LOGGER.info("node name is: " + Parameter.NODE_NAME);
        if (Parameter.NODE_NAME == null || Parameter.NODE_NAME.equals("undefined")) {
            LOGGER.warn("\"NODE_NAME\" should be defined!");
        }

        ElasticsearchSettings settings = new ElasticsearchSettings();
        Map<String, Thread> threads = new HashMap<>(10);
        MissionObserver observer = new MissionObserver(settings, threads);
        observer.start();
    }

}
