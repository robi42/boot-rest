package com.github.robi42.boot.domain.util;

import javax.validation.ValidationException;

public interface BeanValidator {
    void validate(Object objectToValidate) throws ValidationException;
}
