package com.duttonjones.MediaServer.IngestEngineV2.core;

public abstract class IngestService {

    private final String name;
    private boolean isRunning;

    public IngestService(String name) {
        this.name = name;
        this.isRunning = false;
    }

    public void start() {
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    public String getName() {
        return name;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
