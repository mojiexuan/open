package com.chenjiabao.open.utils.model.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * @author ChenJiaBao
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Api {
    private boolean enabled = true;
    private String prefix = "server";
    private String accessControlAllowOrigin = "*";
    private List<String> accessControlAllowMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private List<String> accessControlAllowHeaders = List.of("Content-Type", "Authorization");
//    private boolean accessControlAllowCredentials = true;
    private int accessControlMaxAge = 86400;

    public String getAccessControlAllowHeaders() {
        return String.join(",", accessControlAllowHeaders);
    }

    public String getAccessControlAllowMethods() {
        return String.join(",", accessControlAllowMethods);
    }
}
