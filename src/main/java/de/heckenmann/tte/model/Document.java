package de.heckenmann.tte.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ben on 23.07.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document<T> {

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
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getIndex() {
        return this.index;
    }

    public void setIndex(final String index) {
        this.index = index;
    }

    public boolean isCreated() {
        return this.created;
    }

    public void setCreated(final boolean created) {
        this.created = created;
    }

    public T getSource() {
        return this.source;
    }

    public void setSource(final T source) {
        this.source = source;
    }
}
