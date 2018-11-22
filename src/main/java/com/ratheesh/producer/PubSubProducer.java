package com.ratheesh.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ratheesh.Application.PubsubOutboundGateway;
import org.springframework.boot.ApplicationRunner;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class PubSubProducer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PubSubProducer.class);
    @Autowired
    PubsubOutboundGateway pubsubOutboundGateway;
    private Random r = new Random();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting app");

        // Instantiate a Google Cloud Storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Get specific file from specified bucket
        byte[] gsFileContent = storage.readAllBytes(BlobId.of("bucketname", "csvfile.csv"));

        File localCopy = new File("pathname");
        FileUtils.writeByteArrayToFile(localCopy, gsFileContent);

        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        //CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
        CsvMapper csvMapper = new CsvMapper();
        // Read data from CSV file
        List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(localCopy).readAll();
        ObjectMapper mapper = new ObjectMapper();

        //Thread.sleep(5000);

        readAll.forEach(line -> {
            try {
                //Thread.sleep(r.nextInt(1500) + 500);
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(line));
                pubsubOutboundGateway.sendToPubsub(line.toString());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
