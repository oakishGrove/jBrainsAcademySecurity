package account.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomDateValidatorImpl.class)
public @interface CustomDateValidator {
    String pattern() default "MMM-YYYY";

    String message() default "Wrong date!";

    Class<?>[] groups() default {};

    Class<?extends Payload> [] payload() default {};

}
