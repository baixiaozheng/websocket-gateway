package com.baixiaozheng.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class  KlineVo {

    private Long time;

    private BigDecimal high;

    private BigDecimal low;

    private BigDecimal open;

    private BigDecimal close;

    private BigDecimal volume;
}
