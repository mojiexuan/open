package com.chenjiabao.open.utils.model.property;

import lombok.*;

/**
 * @author ChenJiaBao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    private boolean enabled = true;
    private String path = "/docs";
}
