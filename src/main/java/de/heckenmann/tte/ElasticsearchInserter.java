package de.heckenmann.tte;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author heckenmann
 */
public class ElasticsearchInserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchInserter.class);

    private final String url;
    private final String index;
    private final String type;
    private final ClientConfig config;
    private final Client client;
    private final WebTarget webTarget;

    public ElasticsearchInserter(String url, String index, String type) {
        this.url = url;
        this.index = index;
        this.type = type;
        config = new ClientConfig();
        client = ClientBuilder.newClient(config);
        webTarget = client.target(url).path(index).path(type);
    }

    public boolean put(String json) {
        Response r = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(json, MediaType.APPLICATION_JSON));
        if (r.getStatus() < 200 || r.getStatus() >= 300) {
            LOGGER.error(r.readEntity(String.class));
        }
        return r.getStatus() >= 200 && r.getStatus() < 300;
    }

}
