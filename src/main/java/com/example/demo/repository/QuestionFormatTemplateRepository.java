package com.example.demo.repository;

import com.example.demo.domain.QuestionFormatTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionFormatTemplateRepository extends MongoRepository<QuestionFormatTemplate, String> {
}