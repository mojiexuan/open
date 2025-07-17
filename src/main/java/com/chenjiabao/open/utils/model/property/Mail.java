package com.chenjiabao.open.utils.model.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ChenJiaBao
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String host;
    private Integer port = 456;
    private Boolean ssl = false;
    private Boolean auth = false;
    private String username;
    private String password;
    private String protocol = "smtp";
}
