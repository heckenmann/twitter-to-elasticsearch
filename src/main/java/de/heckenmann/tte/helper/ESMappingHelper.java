package de.heckenmann.tte.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.heckenmann.tte.model.Document;
import de.heckenmann.tte.model.Mission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 24.07.2016.
 */
public class ESMappingHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESMappingHelper.class);

    /**
     * Bereitet die Antwort der ES auf.
     * @param typeReference
     * @param response
     * @param <T>
     * @return
     */
    public static <T> List<T> getDocumentFromResult(TypeReference<T> typeReference, Response response) {
        List<T> missions = new ArrayList<T>();
        try {
            ObjectMapper om = new ObjectMapper();
            JsonNode result = om.readTree(response.readEntity(String.class));

            for (JsonNode node : result.findPath("hits").findPath("hits")) {
                missions.add((((T) om.readValue(om.writeValueAsBytes(node), typeReference))));
            }
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        } finally {
            return missions;
        }
    }

}
