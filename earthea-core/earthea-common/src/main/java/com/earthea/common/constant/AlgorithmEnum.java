package com.earthea.common.constant;

import lombok.Getter;

/**
 * 算法枚举
 */
@Getter
public enum AlgorithmEnum {
    MD5("MD5"), SHA_1("SHA-1"), SHA_256("SHA-256"), SHA_384("SHA-384"), SHA_512("SHA-512"),
    ;

    private final String algorithm;

    AlgorithmEnum(String algorithm) {
        this.algorithm = algorithm;
    }
}
