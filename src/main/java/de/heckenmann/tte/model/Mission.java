package de.heckenmann.tte.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

/**
 * Created by Ben on 23.07.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mission {

    /**
     * Host, auf dem die Mission ausgeführt werden soll.
     */
    private String node;

    /**
     * In welchen Index sollen die Daten geschrieben werden?
     */
    private String index;

    /**
     * Name der Mission.
     */
    private String name;

    private String consumerKey;

    private String consumerSecret;

    private String token;

    private String tokenSecret;

    /**
     * Erstellungsdatum.
     */
    private Date creationDate;

    private String[] trackterms;
    private String[] locations;
    private String[] languages;
    private Long[] followings;

    /**
     * Konstruktor.
     */
    public Mission() {
        this.creationDate = new Date();
    }

    public String[] getTrackterms() {
        return trackterms;
    }

    public void setTrackterms(String[] trackterms) {
        this.trackterms = trackterms;
    }

    public String[] getLocations() {
        return locations;
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }

    public Long[] getFollowings() {
        return followings;
    }

    public void setFollowings(Long[] followings) {
        this.followings = followings;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

}
