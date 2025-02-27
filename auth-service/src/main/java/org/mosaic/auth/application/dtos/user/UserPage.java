package org.mosaic.auth.application.dtos.user;

import lombok.Getter;
import lombok.ToString;
import org.mosaic.auth.domain.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

@Getter
@ToString
public class UserPage extends PagedModel<User> {

  public UserPage(Page<User> userPage) {
    super(new PageImpl<>(
          userPage.getContent(),
          userPage.getPageable(),
          userPage.getTotalElements()));
  }
}
