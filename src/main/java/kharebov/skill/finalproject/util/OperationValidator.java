package kharebov.skill.finalproject.util;

import kharebov.skill.finalproject.entity.Operation;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OperationValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Operation.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //
    }
}
