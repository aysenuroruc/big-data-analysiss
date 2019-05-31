package com.oruc.logcreator;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogCreatorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LogCreatorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String[] cities = new String[] { "Istanbul", "London", "Moscow", "Pekin" };
		String[] debugLevels = new String[] { "FATAL", "ERROR", "WARN", "INFO", "DEBUG" };
		StringBuilder sb = new StringBuilder();
		int logfileCounter = 0;
		while(true) {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			sb.append(format.format(new Date()));
			sb.append(" ");
			sb.append(debugLevels[new Random().nextInt(debugLevels.length)]);
			sb.append(" ");
			String city = cities[new Random().nextInt(cities.length)];
			sb.append(city);
			sb.append(" ");
			sb.append("Hello from - ");
			sb.append(city);
			sb.append("\n");
			//System.out.println(sb.toString());
			System.out.println(sb.toString().length());
			System.out.println(sb.toString().getBytes("UTF-8").length);
			double mb = new Integer(sb.toString().getBytes("UTF-8").length) / 1024.00;
			System.out.println(mb);
			if (mb > 2) {
				File file = new File(System.getenv("RAW_PATH") + "/raw-" + logfileCounter++ + ".log");
				FileWriter fw = new FileWriter(file);
				fw.write(sb.toString());
				fw.flush();
				fw.close();
				
				sb = new StringBuilder();
			}
			Thread.sleep(1000);
		}
	}

}
