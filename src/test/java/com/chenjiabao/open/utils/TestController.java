package com.chenjiabao.open.utils;

import com.chenjiabao.open.utils.annotation.ApiVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiVersion
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity<ApiResponse> test() {
        return ApiResponse.success();
    }

}
