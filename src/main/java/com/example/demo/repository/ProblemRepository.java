package com.example.demo.repository;

import com.example.demo.domain.Problem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {
    // examId가 없는 문제들(문제은행 원본 데이터)만 가져오기 위함
    List<Problem> findByExamIdIsNull();
    List<Problem> findByExamIdIsNullAndDomainId(String domainId);

    // 채점 시 특정 시험지에 맵핑된 문제들을 조회
    List<Problem> findByExamId(String examId);
}
