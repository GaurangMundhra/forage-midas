package com.jpmc.midascore;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.kafka.KafkaProducer;
import com.jpmc.midascore.repository.UserRepository;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@ComponentScan(basePackages = "com.jpmc.midascore.kafka")
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void setup() {
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafkaBroker.getBrokersAsString());
    }

    @Test
    void debugBeans() {
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
    }

    @Test
    void task_three_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");

        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        Thread.sleep(2000); // Wait for Kafka transactions

        // ✅ Fetch Waldorf's balance
        Optional<UserRecord> waldorf = userRepository.findByName("waldorf");
        if (waldorf.isPresent()) {
            int finalBalance = (int) Math.floor(waldorf.get().getBalance()); // Round down
            logger.info("🚀 Waldorf's final balance (rounded down): {}", finalBalance);
        } else {
            logger.warn("❌ Waldorf not found in DB!");
        }

        logger.info("----------------------------------------------------------");
        logger.info("Use your debugger to find out what Waldorf's balance is after all transactions are processed.");
        logger.info("Kill this test once you find the answer.");

        while (true) {
            Thread.sleep(20000);
            logger.info("...");
        }
    }
}
