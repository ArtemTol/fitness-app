//package com.fitness.gamification;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class SimpleKafkaConsumer {
//
//    @KafkaListener(
//            topics = "${spring.kafka.topic-client}",
//            groupId = "${spring.kafka.consumer.group-id}"
//    )
//    public void listen(String message) {
//        log.info("📥 SIMPLE CONSUMER RECEIVED: {}", message);
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode node = mapper.readTree(message);
//            log.info("✅ Parsed JSON: {}", node);
//        } catch (Exception e) {
//            log.error("❌ Failed to parse: {}", e.getMessage());
//        }
//    }
//}