//package com.fitness.gamification.config;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//@Configuration
//public class KafkaConfig {
//
//    @Bean
//    public NewTopic workoutCompletedTopic() {
//        return TopicBuilder.name("workout.completed")
//                .partitions(3)
//                .replicas(1)
//                .build();
//    }
//
//    @Bean
//    public NewTopic achievementEarnedTopic() {
//        return TopicBuilder.name("achievement.earned")
//                .partitions(3)
//                .replicas(1)
//                .build();
//    }
//
//    @Bean
//    public NewTopic userCreatedTopic() {
//        return TopicBuilder.name("user.created")
//                .partitions(3)
//                .replicas(1)
//                .build();
//    }
//}