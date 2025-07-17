package com.chenjiabao.open.utils.model.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Arrays;
import java.util.List;

/**
 * @author ChenJiaBao
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File {
    // 支持的文件格式
    private List<String> format = Arrays.asList(".png", ".jpg", ".jpeg", ".bmp");
    private Long maxSize = 1024 * 1024 * 10L;
    private String path = "/public/upload";

    public void setPath(String path) {
        if (path != null) {
            this.path = path.replaceAll("[/\\\\]+$", "");
        } else {
            this.path = null;
        }
    }
}
