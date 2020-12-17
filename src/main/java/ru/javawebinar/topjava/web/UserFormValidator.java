package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;

@Component
public class UserFormValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass) || UserTo.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        HasId user = (HasId) o;
        if (user.isNew()) {
            if (getUserByEmail(user) != null) {
                addError(errors);
            }
        } else {
            User userFromDB = getUserByEmail(user);
            if (userFromDB != null && !userFromDB.getId().equals(user.getId())) {
                addError(errors);
            }
        }
    }

    private <T extends HasId> User getUserByEmail(T user) {
        String email = user instanceof User ? ((User) user).getEmail() : ((UserTo)user).getEmail();
        return userRepository.getByEmail(email);
    }

    private void addError(Errors errors) {
        errors.rejectValue("email", "email isn't valid", "User with this email already exists");
    }
}
