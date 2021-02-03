package oolloo.gitmc.gitcli.anns;

import org.kohsuke.args4j.spi.OptionHandler;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD,METHOD,PARAMETER})
public @interface Argument {
    String usage() default "";
    String metaVar() default "";
    boolean required() default false;
    boolean hidden() default false;
    Class<? extends OptionHandler> handler() default OptionHandler.class;
    int index() default 0;
    boolean multiValued() default false;
}