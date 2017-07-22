package de.heckenmann.tte;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import de.heckenmann.tte.helper.ESMappingHelper;
import de.heckenmann.tte.model.Document;
import de.heckenmann.tte.model.Mission;

/**
 * @author heckenmann
 */
public class ElasticsearchSettings {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchSettings.class);
    private final Client client;
    private final WebTarget missionsTarget;

    public ElasticsearchSettings() {
        this.client = ClientBuilder.newClient();
        missionsTarget = this.client.target(Parameter.ELASTICSEARCH_URL).path("tte_settings").path("mission");
    }

    /**
     * Liest die Missionen aus der elasticsearch aus.
     *
     * @return
     */
    public List<Document<Mission>> getMissions() {
        Response r = missionsTarget.path("_search").queryParam("size", 10000).request().accept(MediaType.APPLICATION_JSON).get();
        return ESMappingHelper.getDocumentFromResult(new TypeReference<Document<Mission>>() {
        }, r);
    }

    /**
     * Fügt eine neue Mission hinzu.
     *
     * @param mission
     * @return
     */
    public boolean addMission(Mission mission) {
        Response r = missionsTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(mission, MediaType.APPLICATION_JSON));
        return r.getStatus() >= 200 && r.getStatus() < 300;
    }

    public boolean removeMission(Document<Mission> mission) {
        Response r = missionsTarget.path(mission.getId()).request().accept(MediaType.APPLICATION_JSON).delete();
        return r.getStatus() >= 200 && r.getStatus() < 300;
    }
}
