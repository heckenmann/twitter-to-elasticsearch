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

    private static final String ELASTICSEARCH_URL_ENV = System.getenv("TTE_ELASTICSEARCH_URL");
    private static final String ELASTICSEARCH_URL_PROP = System.getProperty("TTE_ELASTICSEARCH_URL");

    public static final String ELASTICSEARCH_URL = ELASTICSEARCH_URL_PROP == null ? ELASTICSEARCH_URL_ENV == null ? "http://localhost:9200" : ELASTICSEARCH_URL_ENV : ELASTICSEARCH_URL_PROP;

    public static void main(String... args) {
        BasicConfigurator.configure();
        LOGGER.info("TwitterToElasticsearch starting...");

        ElasticsearchSettings settings = new ElasticsearchSettings();
        Map<String, Thread> threads = new HashMap<>(10);
        MissionOberserver observer = new MissionOberserver(settings, threads);
        observer.start();
    }

}
