package com.rjkj.cf.bbibm.kjds.goods.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CommissionDetailsVo {
    @ApiModelProperty(value="用户名")
    private String userName;
    @ApiModelProperty(value="提成时间")
    private LocalDateTime ctime;
    @ApiModelProperty(value="提成金额")
    private BigDecimal amount;
}
