Bio-Exam AI Server (생명과학 임용고시 AI 채점 및 문제 관리 서버)
🛠 기술 스택
Language: Java 17

Framework: Spring Boot, Spring Data JPA

Database: Relational Database (H2 / MySQL)

AI Integration: Google Gemini API (GeminiService)

Concurrency: CompletableFuture, ExecutorService, ConcurrentHashMap

🧠 RAG(Retrieval-Augmented Generation)를 도입하지 않은 이유
정형화된 채점 기준과 모범 답안의 존재: 본 시스템은 임용고시 출제 단원(DOMAIN_LIST)과 양식 템플릿(QuestionFormatTemplate)을 바탕으로 문제, 모범 답안(referenceAnswer), 그리고 세부 채점 기준(rubric)을 데이터베이스에 정형 데이터로 영구 저장합니다.

결정론적(Deterministic) 평가 보장: RAG는 대규모 비정형 문서(PDF, 텍스트 청크)에서 벡터 유사도로 문맥을 검색해 답변을 생성하는 데 특화되어 있으나, 임용고시 주관식 답안 채점은 저장된 명확한 모범 답안과 엄격한 채점 기준표와의 1:1 대조 평가가 필수적입니다. 따라서 Vector DB 검색 레이턴시와 확률적 검색 오류(Hallucination)를 배제하고, DB에 적재된 정밀한 메타데이터를 직접 프롬프트 컨텍스트로 주입하는 방식이 가장 정확한 채점을 보장합니다.

⚙️ 주요 아키텍처 및 핵심 로직
비동기 대량 문제 생성: CompletableFuture와 커스텀 ExecutorService 스레드 풀을 활용하여 다수의 단원 문항을 병렬로 Gemini AI에 요청하고 생성하는 Non-blocking 구조를 구현하여 응답 대기 시간을 단축했습니다.

비동기 일괄 채점 및 Job 관리: 대규모 시험지 제출 시 타임아웃을 방지하기 위해 submitExamAsync를 통해 고유 jobId를 즉시 발급하고, 백그라운드 스레드에서 채점을 수행한 뒤 ConcurrentHashMap을 통해 스레드 세이프하게 진행 상태(PROCESSING, COMPLETED, FAILED)를 관리합니다.

엄격한 JSON 파싱 방어 코드: LLM이 마크다운이나 불필요한 텍스트를 응답에 포함할 경우를 대비해 정규식 기반의 문자열 정제(replaceAll)와 Spring JSON Parser를 조합하여 데이터 파싱 안정성을 확보했습니다.