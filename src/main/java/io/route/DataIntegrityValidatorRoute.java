package io.route;

import org.apache.camel.builder.RouteBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataIntegrityValidatorRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:data-integrity-validator").description("DAML ContractID, HASH값을 사용하여 서버에 저장된 데이터와 일치하는지 체크")
        .log("DataIntegrityValidatorRoute called");
    }
    
}
