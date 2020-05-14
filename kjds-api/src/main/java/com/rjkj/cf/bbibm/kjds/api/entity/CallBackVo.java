package com.rjkj.cf.bbibm.kjds.api.entity;

import lombok.Data;

@Data
public class CallBackVo {
    private String pid;
    private String uid;
    private String gid;
    private String errorMsg;
    private String itemId;
    private int rackStatus;
}
