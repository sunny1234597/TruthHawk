# TruthHawk - 댓글알바를 잡는 진실의 매
네이버 뉴스에서 조선족 댓글을 탐지하는 모니터링 소프트웨어입니다.
셀레니움과 KOMORAN, Jsoup을 이용하여 개발되었으며 조선족 댓글알바 퇴치와 증거 확보를 위한 모니터링을 제공합니다.

목표 기능
========
 * 입력한 키워드와 관련된 댓글을 인식하여 좋아요와 싫어요, URL, 내용을 로그로 저장.
 * 좋아요 수와 싫어요 수 비율이 비상식적으로 많거나 조선족들이 자주 사용하는 용어가 있는 댓글을 NLP(자연어처리)를 이용하여 상세한 내용을 확인 가능.

개발중인 기능
========
 * 자연어 처리를 이용한 이상 댓글 분석
 * 좋아요 수와 싫어요 수의 비율을 이용한 댓글 분석

 개발된 기능
========
 * 셀레니움을 이용한 댓글 리스트화(Object Array)