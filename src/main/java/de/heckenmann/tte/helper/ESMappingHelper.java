package de.heckenmann.tte.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public static <T> List<T> getDocumentFromResult(final TypeReference<T> typeReference, final Response response) {
        final List<T> missions = new ArrayList<>();
        try {
            final ObjectMapper om = new ObjectMapper();
            final JsonNode result = om.readTree(response.readEntity(String.class));

            for (final JsonNode node : result.findPath("hits").findPath("hits")) {
                missions.add((((T) om.readValue(om.writeValueAsBytes(node), typeReference))));
            }
        } catch (final JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        } catch (final IOException e) {
            LOGGER.error(e.getMessage());
        }
        return missions;
    }

}
