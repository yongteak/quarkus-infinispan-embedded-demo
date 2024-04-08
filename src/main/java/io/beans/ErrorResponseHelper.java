package io.beans;

import java.util.ArrayList;

import io.model.message.ErrorResponse;
import io.model.message.Error;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * Error Response helper bean
 *
 */
@ApplicationScoped
@Named("errorResponseHelper")
@RegisterForReflection // Lets Quarkus register this class for reflection during the native build
@Slf4j
public class ErrorResponseHelper {

    public ErrorResponse generateErrorResponse(String id, String description, String message, Object body) {
        ArrayList<String> messages = new ArrayList<String>(0);

        if (body instanceof io.model.daml.Error) {
            messages.addAll(((io.model.daml.Error) body).getErrors());
        }

        if (message != null) {
            messages.add(message);
        }
        Error error = new Error(id, description, messages);
        log.error("=>", message);
        return new ErrorResponse(error);
    }

}
