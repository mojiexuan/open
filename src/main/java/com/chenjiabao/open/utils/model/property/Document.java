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
public class Document {
    private boolean enabled = true;
    private String path = "/docs";
}
