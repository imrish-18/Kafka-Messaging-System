package com.test.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
		// Number of status events to generate
//		int numEvents = 1000;
//
//		// Instantiate Faker to generate random data
//		Faker faker = new Faker();
//
//		// List to hold all the generated events
//		List<Map<String, Object>> events = new ArrayList<>();
//
//		// Generate data for 'numEvents' StatusEvent objects
//		for (int i = 0; i < numEvents; i++) {
//			// Generate a unique source ID
//			String sourceId = "APAC_MAP_" + faker.number().randomDigit() + "/APAC_BW_" + faker.number().randomDigit();
//
//			// Generate a StatusEvent
//			Map<String, Object> event = generateStatusEvent(faker, sourceId);
//			events.add(event);
//		}
//
//		// Serialize the data to a JSON file
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			// Write the JSON data to a file
//			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("status_events_test_data.json"), events);
//			System.out.println("Test data generated and saved to status_events_test_data.json");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static Map<String, Object> generateStatusEvent(Faker faker, String sourceId) {
//		Map<String, Object> statusEvent = new HashMap<>();
//
//		// Generate random values for the event
//		String state = faker.options().option("ACTIVE", "INACTIVE", "PENDING", "ERROR");
//		String description = faker.company().buzzword() + " service is " + state.toLowerCase();
//		String timestamp = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toInstant().toString();
//		String component = faker.company().industry();
//		String event = faker.options().option("RUNNING", "STOPPED", "PAUSED", "ERROR");
//		String server = faker.internet().domainName();
//		String port = faker.number().numberBetween(8000, 8999) + "";
//
//		// Create properties map for the additional data
//		Map<String, String> properties = new HashMap<>();
//		properties.put("rest path", "http://" + server + ":" + port + "/rda-vcp-service");
//		properties.put("HGBQ State", "HGBQ: Size=" + faker.number().randomDigit() + ", Head=" + faker.number().randomDigit() +
//				", InProgress=" + faker.number().randomDigit() + ", Blocked=" + faker.number().randomDigit() +
//				", Completed=" + faker.number().randomDigit());
//
//		// Add data to the status event map
//		statusEvent.put("id", sourceId);
//		statusEvent.put("state", state);
//		statusEvent.put("description", description);
//		statusEvent.put("timestamp", timestamp);
//		statusEvent.put("component", component);
//		statusEvent.put("event", event);
//		statusEvent.put("source", sourceId);
//		statusEvent.put("server", server);
//		statusEvent.put("port", port);
//		statusEvent.put("properties", properties);
//
//		return statusEvent;
//
//	}
		int recordCount = 1000; // Number of records to generate
		String outputFileName = "bulk_test_data.json";

		// Create a Jackson ObjectMapper
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();

		// Generate test data records
		for (int i = 1; i <= recordCount; i++) {
			arrayNode.add(generateRecord(mapper, i));
		}

		// Write to a JSON file
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFileName), arrayNode);
			System.out.println("Generated " + recordCount + " records and saved to " + outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to write JSON file.");
		}
	}

	private static ObjectNode generateRecord(ObjectMapper mapper, int index) {
		Random random = new Random();

		// Generate random string for description
		String description = generateRandomString(random, random.nextInt(200) + 100); // Length between 100 and 300

		// Create a JSON object for a single record
		ObjectNode record = mapper.createObjectNode();
		record.put("code", String.format("OP.OSP%03d", index));
		record.put("name", "Test Rule " + index);
		record.put("description", description);
		record.putArray("referenceType").add("RULE");
		record.put("sourceType", "RULE");
		record.put("systemName", random.nextBoolean() ? "OnStreetParking" : "OffStreetParking");
		record.put("isActive", random.nextBoolean());
		record.put("leEnabled", random.nextBoolean());
		record.put("severity", random.nextInt(6)); // Severity between 0 and 5
		record.put("condition", String.format("($ParkingRuleDate.description.head.text.length<=%d)", description.length()));
		record.put("action", randomAction(random));
		record.put("applicability", randomApplicability(random));
		record.put("inputType", "com.here.map.osp.schema.model.ParkingRuleDate");

		return record;
	}

	private static String generateRandomString(Random random, int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}

		return sb.toString();
	}

	private static String randomAction(Random random) {
		String[] actions = {"Log Valid Entry", "Throw Error", "Validate Entry"};
		return actions[random.nextInt(actions.length)];
	}

	private static String randomApplicability(Random random) {
		String[] applicabilityOptions = {"Global", "Regional", "Specific"};
		return applicabilityOptions[random.nextInt(applicabilityOptions.length)];
	}
}
