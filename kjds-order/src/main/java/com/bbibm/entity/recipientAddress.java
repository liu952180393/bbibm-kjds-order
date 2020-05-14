package com.bbibm.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class recipientAddress {
    /**
     *
     */
    @ApiModelProperty(value="")
    private String phone;
    /**
     *
     */
    @ApiModelProperty(value="")
    private String name;
    /**
     *
     */
    @ApiModelProperty(value="")
    private String fullAddress;
}
