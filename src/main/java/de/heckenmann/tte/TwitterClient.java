package de.heckenmann.tte;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import de.heckenmann.tte.model.Mission;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ben on 23.07.2016.
 */
public class TwitterClient implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterClient.class);

    private final Client hosebirdClient;
    private final BlockingQueue<String> msgQueue;
    private final Mission mission;

    public TwitterClient(Mission mission) {
        this.mission = mission;

        //https://github.com/twitter/hbc
        /**
         * Set up your blocking queues: Be sure to size these properly based on
         * expected TPS of your stream
         */
        msgQueue = new LinkedBlockingQueue<>(100000);

        /**
         * Declare the host you want to connect to, the endpoint, and
         * authentication (basic auth or oauth)
         */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Optional: set up some followings and track terms
        if (mission.getFollowings() != null) {
            List<Long> followings = Lists.newArrayList(mission.getFollowings());
            hosebirdEndpoint.followings(followings);
            LOGGER.info("Mission " + mission.getName() + " followings: " + followings.toString());
        }

        if (mission.getTrackterms() != null) {
            List<String> terms = Lists.newArrayList(mission.getTrackterms());
            hosebirdEndpoint.trackTerms(terms);
            LOGGER.info("Mission " + mission.getName() + " terms: " + terms.toString());
        }

        if (mission.getLanguages() != null) {
            List<String> languages = Lists.newArrayList(mission.getLanguages());
            hosebirdEndpoint.languages(languages);
            LOGGER.info("Mission " + mission.getName() + " languages: " + languages.toString());
        }

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(mission.getConsumerKey(), mission.getConsumerSecret(), mission.getToken(), mission.getTokenSecret());

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client: " + mission.getName()) // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        hosebirdClient = builder.build();

        // Attempts to establish a connection.
        hosebirdClient.connect();
        LOGGER.info("Client for mission " + mission.getName() + " connected");
    }

    public void run() {
        ElasticsearchInserter i = new ElasticsearchInserter(TwitterToElasticsearch.ELASTICSEARCH_URL, mission.getIndex() == null ? "twitter" : mission.getIndex(), "tweet");
        while (!hosebirdClient.isDone()) {
            try {
                String json = msgQueue.take();
                i.put(json);
            } catch (InterruptedException ex) {
                hosebirdClient.stop();
                LOGGER.info("Mission stopped: " + mission.getName());
            }
        }
    }
}
