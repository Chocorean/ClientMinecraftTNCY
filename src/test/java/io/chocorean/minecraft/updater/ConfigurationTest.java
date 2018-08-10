package io.chocorean.minecraft.updater;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigurationTest {

    @Test
    void testCorrectConfiguration() {
        Configuration conf = Configuration.getInstance();
        assertNotNull(conf, "The configuration should be not null");
        testProperties(conf.getProfile(), conf.getForgeVersion(), conf.getModsUrl(), conf.getVersion(),
            conf.getForgeUrl(), conf.getStylesUrl(), conf.getVersionUrl(), conf.getChangelogUrl(),
            conf.getGithubUrl());
        assertNotNull(conf.toString());
    }

    @Test
    void testBuggyConfiguration() {
        HashMap<String, String> props = new HashMap<>();
        props.put("forgeUrl", "#todo");
        Configuration conf = new Configuration(props);
        assertNotNull(conf, "The configuration should be not null");
        assertNull(conf.getForgeUrl());
    }

    private void testProperties(Object ...properties) {
        for(Object o: properties)
            assertNotNull(o, "None property should be null");
    }
}
