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
public class Check {
    private Boolean enabled = true;
    private int min = 6;
    private int max = 16;
    private int level = 4;
    private String specialChars = "!@#$%^&*()_+|<>?{}[]=-~";
}
