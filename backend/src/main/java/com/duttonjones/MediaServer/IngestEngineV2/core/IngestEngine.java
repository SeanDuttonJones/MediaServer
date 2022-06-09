package com.duttonjones.MediaServer.IngestEngineV2.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

public class IngestEngine {

    private static IngestEngine ingestEngine = null;

    private final Map<String, IngestService> ingestServices;
    private final Properties properties;
    private boolean isRunning;

    private IngestEngine() {
        this.ingestServices = new HashMap<>();
        this.isRunning = false;

        // Load Configuration file
        this.properties = new Properties();

        // TODO: Think about a way we can dynamically load the config file. Possibly from a global config file?
        String configFilePath = "src/config.properties";
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(configFilePath);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static IngestEngine getInstance() {
        if(ingestEngine == null) {
            ingestEngine = new IngestEngine();
        }

        return ingestEngine;
    }

    public void start() {
        this.isRunning = true;

        for(IngestService s : ingestServices.values()) {
            s.start();
        }
    }

    public void stop() {
        this.isRunning = false;

        for(IngestService s : ingestServices.values()) {
            s.stop();
        }
    }

    public void registerService(IngestService ingestService) {
        ingestServices.put(ingestService.getName(), ingestService);
    }

    public IngestService obtainServiceByName(String ingestServiceName) {
        if(ingestServices.containsKey(ingestServiceName)) {
            return ingestServices.get(ingestServiceName);
        }

        throw new NoSuchElementException("The IngestService '" + ingestServiceName + "' is not registered!");
    }

    public Properties getProperties() {
        return properties;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
