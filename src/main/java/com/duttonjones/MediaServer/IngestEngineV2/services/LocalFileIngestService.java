package com.duttonjones.MediaServer.IngestEngineV2.services;

import com.duttonjones.MediaServer.IngestEngineV2.core.IngestService;

public class LocalFileIngestService extends IngestService {

    //TODO: Implement this Service
    public LocalFileIngestService(String name) {
        super(name);
    }

    @Override
    public void start() {
        super.start();
        System.out.println("LocalFileIngestService: starting...");
    }

    @Override
    public void stop() {
        super.stop();
        System.out.println("LocalFileIngestService: stopping...");
    }
}
