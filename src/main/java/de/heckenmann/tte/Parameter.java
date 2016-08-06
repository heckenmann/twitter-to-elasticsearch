package de.heckenmann.tte;

/**
 *
 * @author heckenmann
 */
public class Parameter {

    // elasticsearch url
    private static final String ELASTICSEARCH_URL_ENV = System.getenv("TTE_ELASTICSEARCH_URL");
    private static final String ELASTICSEARCH_URL_PROP = System.getProperty("TTE_ELASTICSEARCH_URL");
    public static final String ELASTICSEARCH_URL = ELASTICSEARCH_URL_PROP == null ? ELASTICSEARCH_URL_ENV == null ? "http://localhost:9200" : ELASTICSEARCH_URL_ENV : ELASTICSEARCH_URL_PROP;

    // node name
    private static final String NODE_NAME_ENV = System.getenv("NODE_NAME");
    private static final String NODE_NAME_PROP = System.getProperty("NODE_NAME");
    public static final String NODE_NAME = NODE_NAME_PROP == null ? NODE_NAME_ENV == null ? "undefined" : NODE_NAME_ENV : NODE_NAME_PROP;
}
