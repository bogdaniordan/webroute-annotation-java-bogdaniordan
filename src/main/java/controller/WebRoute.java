package controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebRoute {
    String path() default "";
    Method method() default Method.GET;

    enum Method {
        GET, POST;

        public boolean equals(String otherMethod) {
            return this.name().equalsIgnoreCase(otherMethod);
        }
    }
}
