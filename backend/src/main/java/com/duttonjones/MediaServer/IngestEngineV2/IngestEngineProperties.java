package com.duttonjones.MediaServer.IngestEngineV2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

// TODO: Add this to the IngestEngineV2
public class IngestEngineProperties extends Properties {

    private static IngestEngineProperties ingestEngineProperties = null;
    private IngestEngineProperties() {
        String configFilePath = "src/config.properties";
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(configFilePath);
            super.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static IngestEngineProperties getInstance() {
        if(ingestEngineProperties == null) {
            ingestEngineProperties = new IngestEngineProperties();
        }

        return ingestEngineProperties;
    }
}
