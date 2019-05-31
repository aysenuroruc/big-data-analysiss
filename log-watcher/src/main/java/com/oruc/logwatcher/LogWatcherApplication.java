package com.oruc.logwatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class LogWatcherApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LogWatcherApplication.class, args);
	}

	LogEvent log;

	@Autowired
	private KafkaTemplate<String, String> template;

	@Override
	public void run(String... args) throws Exception {
		Path dir = Paths.get(System.getenv("RAW_PATH"));
		WatchService watcher = FileSystems.getDefault().newWatchService();

		try {
			WatchKey key = dir.register(watcher,
					StandardWatchEventKinds.ENTRY_CREATE);
			for (;;) {
				try {
					key = watcher.take();

					for (WatchEvent<?> event: key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();

						// This key is registered only
						// for ENTRY_CREATE events,
						// but an OVERFLOW event can
						// occur regardless if events
						// are lost or discarded.
						if (kind == StandardWatchEventKinds.OVERFLOW) {
							continue;
						}

						WatchEvent<Path> ev = (WatchEvent<Path>)event;
						Path filename = ev.context();

						// Verify that the new
						//  file is a text file.
						try {
							// Resolve the filename against the directory.
							// If the filename is "test" and the directory is "foo",
							// the resolved name is "test/foo".
							System.out.println(filename.toString());
							Path child = dir.resolve(filename);
							System.out.println(child);

							// TODO check file extension if it is .log

							File file = child.toFile();
							if ( FilenameUtils.getExtension(filename.toString()).equals("log")){
								FileReader fr = new FileReader(file); 
								BufferedReader br = new BufferedReader(fr);
								String line = "";

								while((line = br.readLine()) != null) {
									System.out.println("Sending => " + line);
									// TODO: send object instead of string
									//this.template.send("log",log.log );
									this.template.send("log", line);
								}
								System.out.println("Finished sending ...");

							}
							else {
								System.out.println("not ending .log");
							}

						} catch (Exception x) {
							System.err.println(x);
							continue;
						}

						boolean valid = key.reset();
						if (!valid) {
							break;
						}
					}
				} catch (InterruptedException x) {
					return;
				}
			}

		} catch (IOException x) {
			System.err.println(x);
		}
	}

}
