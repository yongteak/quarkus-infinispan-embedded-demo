package io.model.daml.template;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
/**
 * ContractId기반으로 계약 내용 조회 쿼리
 * templateId필드가 없어도 된다잉..
 */
public class Fetch {
    private String contractId;
    private String templateId;
}

/*

curl --location 'http://ec2-15-165-195-113.ap-northeast-2.compute.amazonaws.com:7575/v1/fetch' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsInNjb3BlIjoiZGFtbF9sZWRnZXJfYXBpIn0.HtipPGij-drbM0xLsfg5jeIMDhq1zejGsxWeq-FxkKo' \
--data '{
  "contractId": "00b0fb35285429b3c47c2db959c4d223bfc7e0d13b3138ac2933e36a9bc0bb0c8cca0112200bf16b5e3a9cd8eb82c8995db5677300c762b14102fe59f439654b7ebf8c3d99",
  "templateId": "d5e9a4ce001773076962d970c8dca2af37a167ea63819fb5209b744fb9c85f6d:Account:AssetHoldingAccount"
}'
 */