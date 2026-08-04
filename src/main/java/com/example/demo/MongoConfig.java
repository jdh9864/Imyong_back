package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        // application.properties와 환경 변수를 모두 무시하고, 이 주소를 최우선으로 강제 연결합니다.
        String uri = "mongodb+srv://jdh98649874_db_user:LGz4WlwpIRWQ7zmK@cluster0.o4dn6fl.mongodb.net/biology_exam_db?appName=Cluster0";
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create(uri), "biology_exam_db"));
    }
}
