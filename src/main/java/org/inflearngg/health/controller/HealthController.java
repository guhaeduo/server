package org.inflearngg.health.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@Validated
public class HealthController {


    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }

}
