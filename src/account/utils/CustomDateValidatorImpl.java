package account.utils;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class CustomDateValidatorImpl implements ConstraintValidator<CustomDateValidator, String> {
    private Logger logger = LoggerFactory.getLogger(CustomDateValidatorImpl.class);

    private String pattern;

    @Override
    public void initialize(CustomDateValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate.parse(value, dateTimeFormatter);
            return true;
        } catch (Exception exception) {
            logger.info("Exception in dto date validation with exception:", exception);
        }
        return false;
    }
}
