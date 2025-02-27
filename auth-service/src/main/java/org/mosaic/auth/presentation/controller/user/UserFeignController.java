package org.mosaic.auth.presentation.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mosaic.auth.application.dtos.user.UserFeignResponse;
import org.mosaic.auth.application.service.user.UserQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/feign/auth")
@RequiredArgsConstructor
@Slf4j
public class UserFeignController {

    private final UserQueryService userQueryService;

    @GetMapping("/{userUuid}")
    public UserFeignResponse getUser(
        @PathVariable String userUuid) {
        log.info("Get User By FeignClient Success!!");
        return userQueryService.getFeignUserResponse(userUuid);
    }
}
