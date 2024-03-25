package org.inflearngg.health.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.aop.dto.Region;
import org.inflearngg.health.request.RequestDto;
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


    @PostMapping("/test/valid/post")

    public String postValidCheck(@Valid  @RequestBody RequestDto.ValidCheck validCheck)
    {
        log.info("region : {}", validCheck.getRegion());
//        log.info("age : {}", validCheck.getAge());
        return "OK";
    }

    @GetMapping("/test/valid/param")
    public String getParamValidCheck(@Valid  RequestDto.ValidCheck validCheck)
    {
        log.info("region : {}", validCheck.getRegion());
//        log.info("age : {}", validCheck.getAge());
        return "OK";
    }


    @GetMapping("/test/valid/header")
    public String getHeaderValidCheck(@RequestHeader(name = "password") @Size(min = 3, max = 4) String validCheck)
    {
        log.info("header : {}", validCheck);
        return "OK";
    }

    @Validated
    @GetMapping("/test/valid/{id}")
    public String getPathValidCheck(@PathVariable("id") Integer validCheck)
    {
        log.info("path : {}", validCheck);
        return "OK";
    }
}
