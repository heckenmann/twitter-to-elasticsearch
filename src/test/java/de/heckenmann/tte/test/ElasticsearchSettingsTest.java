package de.heckenmann.tte.test;

import de.heckenmann.tte.ElasticsearchSettings;
import de.heckenmann.tte.model.Document;
import de.heckenmann.tte.model.Mission;
import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Ben on 23.07.2016.
 */
public class ElasticsearchSettingsTest {

    public ElasticsearchSettingsTest() {
        BasicConfigurator.configure();
    }

    @Test
    public void testAddAndDelete() {
        ElasticsearchSettings settings = new ElasticsearchSettings();
        String[] trackterms = {"Munich", "Berlin"};

        Mission m = new Mission();
        m.setName("Testmission");
        m.setTrackterms(trackterms);
        m.setNode("localhost");
        Assert.assertEquals(true, settings.addMission(m));

        sleep(1000);
        List<Document<Mission>> missions = settings.getMissions();
        Assert.assertFalse(missions.isEmpty());
        // missions.forEach(setting -> Assert.assertTrue(settings.removeMission(setting)));
        sleep(1000);
        Assert.assertTrue(settings.getMissions().isEmpty());
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }
}
