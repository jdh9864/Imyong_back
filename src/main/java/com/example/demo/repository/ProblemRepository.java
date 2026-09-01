package com.example.demo.repository;

import com.example.demo.domain.Problem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {
    // examId가 없는 문제들(문제은행 원본 데이터)만 가져오기 위함
    List<Problem> findByExamIdIsNull();
    List<Problem> findByExamIdIsNullAndDomainId(String domainId);

    // 채점 시 특정 시험지에 맵핑된 문제들을 조회
    List<Problem> findByExamId(String examId);

    // 1. 특정 도메인의 틀린 문제(false) 또는 아직 안 푼 문제(null) 조회
    @Query("{ 'domain_id': ?0, '$or': [ { 'is_correct': false }, { 'is_correct': null } ] }")
    List<Problem> findReviewTargetsByDomainId(String domainId);

    // 2. 전체 도메인의 틀린 문제(false) 또는 아직 안 푼 문제(null) 조회
    @Query("{ '$or': [ { 'is_correct': false }, { 'is_correct': null } ] }")
    List<Problem> findAllReviewTargets();
}
