package com.github.robi42.boot.domain.util;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

@Service
public class BootBeanValidator implements BeanValidator {
    private final Validator validator;

    @Inject
    public BootBeanValidator(final Validator validator) {
        this.validator = validator;
    }

    @Override
    public void validate(final Object objectToValidate) throws ValidationException {
        final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(objectToValidate);

        if (!constraintViolations.isEmpty()) {
            throw new ValidationException(buildErrorMessageFrom(constraintViolations).toString());
        }
    }

    private StringBuilder buildErrorMessageFrom(final Set<ConstraintViolation<Object>> constraintViolations) {
        final StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Validation failed: ");
        constraintViolations.parallelStream()
                .forEach(constraintViolation -> format(errorMessage, constraintViolation));
        return errorMessage;
    }

    private void format(final StringBuilder errorMessage, final ConstraintViolation<Object> constraintViolation) {
        errorMessage.append('`');
        errorMessage.append(constraintViolation.getPropertyPath().toString());
        errorMessage.append("` ");
        errorMessage.append(constraintViolation.getMessage());
        errorMessage.append(", value: ");
        errorMessage.append(constraintViolation.getInvalidValue());
        errorMessage.append(';');
    }
}
