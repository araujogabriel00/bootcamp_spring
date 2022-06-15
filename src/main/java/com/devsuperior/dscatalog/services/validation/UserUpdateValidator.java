package com.devsuperior.dscatalog.services.validation;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

  @Autowired private HttpServletRequest request;

  @Autowired private UserRepository userRepository;

  @Override
  public void initialize(UserUpdateValid constraintAnnotation) {}

  @Override
  public boolean isValid(
      UserUpdateDTO userUpdateDTO, ConstraintValidatorContext constraintValidatorContext) {

    var uriVars =
        (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    long userId = Long.parseLong(uriVars.get("id"));

    List<FieldMessage> list = new ArrayList<>();

    User user = userRepository.findByEmail(userUpdateDTO.getEmail());
    if (user != null && userId != user.getId()) {
      list.add(new FieldMessage("email", "Email já existe"));
    }

    for (FieldMessage e : list) {
      constraintValidatorContext.disableDefaultConstraintViolation();
      constraintValidatorContext
          .buildConstraintViolationWithTemplate(e.getMessage())
          .addPropertyNode(e.getFieldName())
          .addConstraintViolation();
    }

    return list.isEmpty();
  }
}
