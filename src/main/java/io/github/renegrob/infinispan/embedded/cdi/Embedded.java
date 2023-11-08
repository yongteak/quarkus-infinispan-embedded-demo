package io.github.renegrob.infinispan.embedded.cdi;

import jakarta.enterprise.util.AnnotationLiteral;
import org.infinispan.commons.api.CacheContainerAdmin.AdminFlag;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface Embedded {
    /**
     * The embedded cache name. If no value is provided the default cache is assumed.
     */
    String name() default "";

    String template() default "";

    AdminFlag adminFlag() default AdminFlag.VOLATILE;

    class Literal extends AnnotationLiteral<Embedded> implements Embedded {

        public static Literal of(String name, String template, AdminFlag adminFlag) {
            return new Literal(name, template, adminFlag);
        }

        private final String name;
        private final String template;
        private final AdminFlag adminFlag;

        public Literal(String value, String template, AdminFlag adminFlag) {
            this.name = value;
            this.template = template;
            this.adminFlag = adminFlag;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String template() {
            return template;
        }

        @Override
        public AdminFlag adminFlag() {
            return adminFlag;
        }


    }
}
