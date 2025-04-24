package com.flat.swiss.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flat.swiss.dao.GeoName;
import com.flat.swiss.dao.Switzerland;
import com.flat.swiss.helper.GeoHelper;
import com.flat.swiss.repository.SwitzerlandRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.flat.swiss.SwissApplication.log;

@Service
public class GeoConfig {
    @Value("${geonames.username}")
    private String username;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SwitzerlandRepository switzerlandRepository;

    private static final int MAX_ROWS = 1000;
    private static final int RATE_LIMIT_DELAY_MS = 2000; // 2 seconds
    private static final String FILE_PATH = "src/main/resources/static/swiss_cities.json";

    public GeoConfig(final SwitzerlandRepository switzerlandRepository) {
        this.switzerlandRepository = switzerlandRepository;
    }

    public void fetchAndSaveSwissCities() {
        List<Object> allCities = new ArrayList<>();
        List<String> cantons = List.of("AG", "AI", "AR", "BE", "BL", "BS", "FR", "GE", "GL", "GR", "JU", "LU", "NE", "NW", "OW", "SG", "SH", "SO", "SZ", "TG", "TI", "UR", "VD", "VS", "ZG", "ZH");

        for (String canton : cantons) {
            int startRow = 0;
            log.info("Fetching cities for canton: " + canton);

            while (true) {
                String url = UriComponentsBuilder.fromHttpUrl("http://api.geonames.org/searchJSON")
                        .queryParam("username", username)
                        .queryParam("country", "CH")
                        .queryParam("featureClass", "P")
                        .queryParam("adminCode1", canton)
                        .queryParam("startRow", startRow)
                        .queryParam("maxRows", MAX_ROWS)
                        .toUriString();

                GeoHelper response = restTemplate.getForObject(url, GeoHelper.class);

                if (response != null && response.getGeonames() != null) {
                    allCities.addAll(response.getGeonames());
                    startRow += MAX_ROWS;
                    log.info("Retrieved " + allCities.size() + " cities so far.");
                    try {
                        Thread.sleep(RATE_LIMIT_DELAY_MS); // Delay to handle rate limit
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
        }

        // Save the results to a JSON file in src/main/resources/static
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, allCities);
            log.info("Swiss cities data saved to 'src/main/resources/static/swiss_cities.json'");
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Saving Swiss cities to database.");

        saveCitiesToDatabase();

        log.info("Cities saved to database");

    }

    public void saveCitiesToDatabase() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists() && file.length() > 0) {
                List<GeoName> geoNames = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, GeoName.class));
                List<Switzerland> swissCities = new ArrayList<>();

                for (GeoName geoName : geoNames) {
                    Switzerland city = Switzerland.builder()
                            .city(geoName.getCity())
                            .canton(geoName.getCanton())
                            .postcode(geoName.getPostcode())
                            .coordX(geoName.getCoordX())
                            .coordY(geoName.getCoordY())
                            .build();
                    swissCities.add(city);
                }

                switzerlandRepository.saveAll(swissCities);
                log.info("Saved " + swissCities.size() + " cities to the database");
            } else {
                log.info("File does not exist or is empty, nothing to save to the database.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFileEmptyOrNonexistent() {
        File file = new File(FILE_PATH);
        return !file.exists() || file.length() == 0;
    }
}
