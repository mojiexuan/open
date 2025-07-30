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
public class Machine {
    private boolean enabled = true;
    // 分布式机器id
    private Long id = 1L;
}
