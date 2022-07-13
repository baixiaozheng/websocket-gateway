package com.baixiaozheng.common.vo;

//import com.baixiaozheng.cct.common.constants.KlinePeriod;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;



@Data
@Accessors(chain = true)
public class KlineTicker {

    private String id;

    /**
     * 总成交数量
     */
    private BigDecimal amount;

    /**
     * 成交笔数
     */
    private Integer count;

    /**
     * 开盘价
     */
    private BigDecimal open;

    /**
     * 收盘价
     */
    private BigDecimal close;

    /**
     * 最高价
     */
    private BigDecimal high;

    /**
     * 最低价
     */
    private BigDecimal low;

    /**
     * 总成交价值=（成交数量*成交价）的加和
     */
    private BigDecimal volume;

    /**
     * 交易币种ID
     */
    private Integer coinId;

    /**
     * 支付币种ID
     */
    private Integer payCoinId;

    /**
     * 交易对id
     */
    private Integer financeCoinTradeInfoId;

    /**
     * k线开始时间
     */
    private Long start;

    /**
     * k线结束时间
     */
    private Long end;

    /**
     * K线步长
     */
//    private KlinePeriod period;
}
