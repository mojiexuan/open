package com.chenjiabao.open.utils.model.property;

import lombok.*;

/**
 * @author ChenJiaBao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Check {
    private Boolean enabled = true;
    private int min = 6;
    private int max = 16;
    private int level = 4;
    private String specialChars = "!@#$%^&*()_+|<>?{}[]=-~";
}
