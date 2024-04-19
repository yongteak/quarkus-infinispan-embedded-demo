package io.beans;

import java.io.IOException;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.http.common.HttpMethods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.model.daml.DAMLExeciseResponse;
import io.model.daml.DAMLFetchResponse;
import io.model.daml.DAMLQueryResponse;
import io.model.daml.DAMLResponse;
import io.model.daml.Exercise;
import io.model.daml.Request;
import io.model.daml.template.Fetch;
import io.model.daml.template.Query;
import io.utils.TokenUtils;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class DamlLedger {
    private final String OPERATOR = "admin";

    public DAMLResponse create(Exchange exchange, Request request) {
        return create(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(), request);
    }

    public DAMLResponse create(Exchange exchange, Request request, String userid) {
        return create(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(), request,
                userid);
    }

    public DAMLResponse create(FluentProducerTemplate template, Map<String, Object> heders, Request request) {
        return create(template, heders, request, OPERATOR);
    }

    public DAMLResponse create(FluentProducerTemplate template, Map<String, Object> heders, Request request,
            String userid) {
        return template.withHeaders(heders)
                .withHeader("Authorization", "Bearer " + TokenUtils.generateDamlTokenString(userid))
                .withHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                .withBody(request)
                .to("direct:restful-daml-create-contract")
                .request(DAMLResponse.class);
    }

    /**
     * execise
     * 
     * @param exchange
     * @param exercise
     * @return
     */
    public DAMLExeciseResponse execise(Exchange exchange, Exercise exercise) {
        return execise(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(), exercise);
    }

    public DAMLExeciseResponse execise(Exchange exchange, Exercise exercise, String userid) {
        return execise(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(), exercise,
                userid);
    }

    public DAMLExeciseResponse execise(FluentProducerTemplate template, Map<String, Object> heders, Exercise exercise) {
        return execise(template, heders, exercise, OPERATOR);
    }

    public DAMLExeciseResponse execise(FluentProducerTemplate template, Map<String, Object> heders, Exercise exercise,
            String userid) {
        return template.withHeaders(heders)
                .withHeader("Authorization", "Bearer " + TokenUtils.generateDamlTokenString(userid))
                .withHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                .withBody(exercise)
                .to("direct:restful-daml-exercise-contract")
                .request(DAMLExeciseResponse.class);
    }

    /**
     * getActiveContractsByQuery
     * 
     * @param exchange
     * @param query
     * @return
     */
    public DAMLQueryResponse getActiveContractsByQuery(Exchange exchange, Query query) {
        return getActiveContractsByQuery(exchange.getContext().createFluentProducerTemplate(),
                exchange.getIn().getHeaders(), query);
    }

    public DAMLQueryResponse getActiveContractsByQuery(Exchange exchange, Query query, String userid) {
        return getActiveContractsByQuery(exchange.getContext().createFluentProducerTemplate(),
                exchange.getIn().getHeaders(), query, userid);
    }

    public DAMLQueryResponse getActiveContractsByQuery(FluentProducerTemplate template, Map<String, Object> heders,
            Query query) {
        return getActiveContractsByQuery(template, heders, query, OPERATOR);
    }

    public DAMLQueryResponse getActiveContractsByQuery(FluentProducerTemplate template, Map<String, Object> heders,
            Query query, String userid) {
        return template.withHeaders(heders)
                .withHeader("Authorization", "Bearer " + TokenUtils.generateDamlTokenString(userid))
                .withHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                .withBody(query)
                .to("direct:restful-daml-query-contract")
                .request(DAMLQueryResponse.class);
    }

    /**
     * fetchContractById
     */
    public DAMLFetchResponse fetchContractById(FluentProducerTemplate template, Fetch query) throws IOException {
        String responseString = template
                .withHeader("Authorization", "Bearer " + TokenUtils.generateDamlTokenString("admin"))
                .withHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                .withBody(query)
                .to("direct:restful-daml-fetch-contract")
                .request(String.class); // 응답을 String 형태로 받음

        // 로그를 추가하여 반환된 문자열 확인
        log.info("Response String: " + responseString);

        try {
            DAMLFetchResponse response = new ObjectMapper().readValue(responseString, DAMLFetchResponse.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseString);
            JsonNode resultNode = rootNode.path("result");

            // DAMLFetchResponse response = new ObjectMapper().readValue(responseString, DAMLFetchResponse.class);
            response.setOriginalData(resultNode); // 원본 데이터 설정
            return response;
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 오류", e);
            throw new IOException("JSON 파싱 중 오류 발생", e);
        }
    }
}