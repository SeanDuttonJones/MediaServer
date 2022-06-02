package com.duttonjones.MediaServer.IngestEngineV2.core;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class IngestEngine {

    private static IngestEngine ingestEngine = null;

    private final Map<String, IngestService> ingestServices;
    private boolean isRunning;

    private IngestEngine() {
        this.ingestServices = new HashMap<>();
        this.isRunning = false;
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

    public boolean isRunning() {
        return isRunning;
    }
}
