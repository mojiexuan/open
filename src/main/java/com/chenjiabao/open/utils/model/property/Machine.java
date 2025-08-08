package com.chenjiabao.open.utils.model.property;

import lombok.*;

/**
 * @author ChenJiaBao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Machine {
    private boolean enabled = true;
    // 分布式机器id
    private Long id = 1L;
    // CPU核心数
    private Integer cpu = 1;
}
