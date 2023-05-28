package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_user_vip")
public class UserVip {
    @TableId("id")
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("start_time")
    private Date startTime;
    @TableField("end_time")
    private Date endTime;
    @TableField("vip_amount")
    private BigDecimal vipAmount;
}

