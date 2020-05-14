package com.rjkj.cf.bbibm.kjds.api.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CallPriceVo {

    private String pid;
    private String gid;
    private BigDecimal price;


}
