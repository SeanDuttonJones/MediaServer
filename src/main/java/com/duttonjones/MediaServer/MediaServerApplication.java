package com.duttonjones.MediaServer;

import com.duttonjones.MediaServer.IngestEngineV2.core.IngestEngine;
import com.duttonjones.MediaServer.IngestEngineV2.core.IngestService;
import com.duttonjones.MediaServer.IngestEngineV2.services.LocalFileIngestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaServerApplication.class, args);

		// TODO: Remove Later
		// Quick Test of IngestEngine V2
		IngestEngine ingestEngine = IngestEngine.getInstance();

		IngestService s = new LocalFileIngestService("LocalFileIngestService");
		ingestEngine.registerService(s);

		s.start();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		s.stop();
	}
}
