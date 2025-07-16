package com.example.common.enums;

public enum StatusEnum {
    CHECKING("审核中"),
    CHECK_FINISH("审核通过"),
    CHECK_FAIL("审核不通过"),
    ;

    public String status;

    StatusEnum(String status) {
        this.status = status;
    }
}
