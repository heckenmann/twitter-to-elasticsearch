package de.heckenmann.tte;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Created by Ben on 23.07.2016.
 */
public class TwitterClient implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterClient.class);

    private final Client hosebirdClient;
    private final BlockingQueue<String> msgQueue;
    private final Mission mission;

    public TwitterClient(final Mission mission) {
        this.mission = mission;

        //https://github.com/twitter/hbc
        /**
         * Set up your blocking queues: Be sure to size these properly based on
         * expected TPS of your stream
         */
        this.msgQueue = new LinkedBlockingQueue<>(100000);

        /**
         * Declare the host you want to connect to, the endpoint, and
         * authentication (basic auth or oauth)
         */
        final Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        final StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Optional: set up some followings and track terms
        if (mission.getFollowings() != null) {
            final List<Long> followings = Lists.newArrayList(mission.getFollowings());
            hosebirdEndpoint.followings(followings);
            LOGGER.info("Mission " + mission.getName() + " followings: " + followings.toString());
        }

        if (mission.getTrackterms() != null) {
            final List<String> terms = Lists.newArrayList(mission.getTrackterms());
            hosebirdEndpoint.trackTerms(terms);
            LOGGER.info("Mission " + mission.getName() + " terms: " + terms.toString());
        }

        if (mission.getLanguages() != null) {
            final List<String> languages = Lists.newArrayList(mission.getLanguages());
            hosebirdEndpoint.languages(languages);
            LOGGER.info("Mission " + mission.getName() + " languages: " + languages.toString());
        }

        // These secrets should be read from a config file
        final Authentication hosebirdAuth = new OAuth1(mission.getConsumerKey(), mission.getConsumerSecret(), mission.getToken(), mission.getTokenSecret());

        final ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client: " + mission.getName()) // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(this.msgQueue));

        this.hosebirdClient = builder.build();

        // Attempts to establish a connection.
        this.hosebirdClient.connect();
        LOGGER.info("Client for mission " + mission.getName() + " connected");
    }

    @Override
    public void run() {
        final ElasticsearchInserter i = new ElasticsearchInserter(Parameter.ELASTICSEARCH_URL,
                this.mission.getIndex() == null ? "twitter" : this.mission.getIndex(), "tweet");
        while (!this.hosebirdClient.isDone()) {
            try {
                final String json = this.msgQueue.take();
                i.put(json);
            } catch (final InterruptedException ex) {
                this.hosebirdClient.stop();
                LOGGER.info("Mission stopped: " + this.mission.getName());
                Thread.currentThread().interrupt();
            }
        }
    }
}
