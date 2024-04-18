package io.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import io.enumeration.Route;
import io.exception.DefaultException;
import io.exception.ForbiddenError;
import io.exception.UnauthorizedError;
import io.model.daml.DAMLExeciseResponse;
import io.model.daml.DAMLFetchResponse;
import io.model.daml.DAMLQueryResponse;
import io.model.daml.DAMLResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DAMLRestfulRoute extends RouteBuilder {

        @Override
        public void configure() {
                onException(Exception.class).handled(true).maximumRedeliveries(0)
                                .to(Route.Response.COMMON_RESPONSE_500.label);
                onException(DefaultException.class).handled(true).maximumRedeliveries(0)
                                .to(Route.Response.BAD_RESPONSE_200.label);
                onException(DefaultException.class).handled(true).maximumRedeliveries(0)
                                .to(Route.Response.BAD_RESPONSE_200.label);
                onException(ForbiddenError.class).handled(true).maximumRedeliveries(0)
                                .to(Route.Response.BAD_RESPONSE_200.label);
                onException(JsonValidationException.class).handled(true).maximumRedeliveries(0)
                                .to(Route.Response.BAD_JSONVALIDATOR_200.label);
                onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0)
                                .to(Route.Response.BAD_RESPONSE_200.label);

                from("direct:restful-daml-response").routeId("restful-daml-response").description("DAML Created")
                                .convertBodyTo(String.class)
                                // .log("# daml-response => ${body}")
                                .unmarshal().json(JsonLibrary.Jackson, DAMLResponse.class);
                from("direct:restful-daml-query-response").routeId("restful-daml-query-response")
                                .description("DAML Query")
                                .convertBodyTo(String.class)
                                // .log("# daml-query-result => ${body}")
                                .unmarshal().json(JsonLibrary.Jackson, DAMLQueryResponse.class);
                from("direct:restful-daml-fetch-response").routeId("restful-daml-fetch-response")
                                .description("DAML Fetch")
                                .convertBodyTo(String.class)
                                .log("# daml-fetch-result => ${body}")
                                .unmarshal().json(JsonLibrary.Jackson, DAMLFetchResponse.class);
                from("direct:restful-daml-exercise-response").routeId("restful-daml-exercise-response")
                                .description("DAML Exercise")
                                .convertBodyTo(String.class)
                                // .log("# daml-exercise-result => ${body}")
                                .unmarshal().json(JsonLibrary.Jackson, DAMLExeciseResponse.class);

                from("direct:restful-daml-query-contract").routeId("restful-daml-query-contract")
                                .marshal().json(JsonLibrary.Jackson)
                                .convertBodyTo(String.class)
                                .log("# [${routeId}] daml-query => ${body}")
                                .toD("netty-http:http://{{daml.host}}/v1/query")
                                .to("direct:restful-daml-query-response");
                // .log("### [${routeId}] daml-query => ${body}");

                from("direct:restful-daml-fetch-contract").routeId("restful-daml-fetch-contract")
                                .marshal().json(JsonLibrary.Jackson)
                                .convertBodyTo(String.class)
                                .log("# [${routeId}] daml-query => ${body}")
                                .toD("netty-http:http://{{daml.host}}/v1/fetch")
                                .to("direct:restful-daml-fetch-response");
                from("direct:restful-daml-create-contract").routeId("restful-daml-create-contract")
                                .marshal().json(JsonLibrary.Jackson)
                                .convertBodyTo(String.class)
                                .log("# [${routeId}] daml-contract => ${body}")
                                .process(ex -> {
                                        ex.getIn();
                                })
                                .toD("netty-http:http://{{daml.host}}/v1/create")
                                .to("direct:restful-daml-response");

                from("direct:restful-daml-exercise-contract").routeId("restful-daml-exercise-contract")
                                .marshal().json(JsonLibrary.Jackson)
                                .convertBodyTo(String.class)
                                .log("# [${routeId}] daml-exercise => ${body}")
                                .process(ex -> {
                                        ex.getIn();
                                })
                                .toD("netty-http:http://{{daml.host}}/v1/exercise")
                                .to("direct:restful-daml-exercise-response");
        }
}
