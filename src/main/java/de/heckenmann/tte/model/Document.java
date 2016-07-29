package de.heckenmann.tte.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Ben on 23.07.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document <T> {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("_type")
    private String type;

    @JsonProperty("_version")
    private String version;

    @JsonProperty("_index")
    private String index;

    @JsonProperty("created")
    private boolean created;

    @JsonProperty("_source")
    private T source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        created = created;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}
