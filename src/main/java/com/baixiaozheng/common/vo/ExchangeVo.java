package com.baixiaozheng.common.vo;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 */
public class ExchangeVo implements Serializable{

    private static final long serialVersionUID = -1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 对标币种
     */
    private Integer baseCurrencyId;

    /**
     * 引用币种，即基于这个币种做交易
     */
    private Integer quoteCurrencyId;

    /**
     * 英文缩写
     */
    private String symbol;

    /**
     * 价格精度
     */
    private Integer pricePrecision;

    /**
     * 数量精度
     */
    private Integer amountPrecision;

    /**
     * 交易手续费率
     */
    private BigDecimal exRate;

    /**
     * 交易最小单笔金额
     */
    private BigDecimal minTradeLimit;

    /**
     * 是否显示
     */
    private boolean show = false;

    /**
     * 排序参数
     */
    private Integer orderParameter;

    /**
     * 是否使用第三方的k线
     */
    private Boolean outKline = false;

    /**
     * 是否允许交易
     */
    private Boolean allwowedTrade = false;

    /**
     * 开盘价
     */
    private BigDecimal open;

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    private PartitionType partitionType = PartitionType.MAIN;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public Integer getQuoteCurrencyId() {
        return quoteCurrencyId;
    }

    public void setQuoteCurrencyId(Integer quoteCurrencyId) {
        this.quoteCurrencyId = quoteCurrencyId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getPricePrecision() {
        return pricePrecision;
    }

    public void setPricePrecision(Integer pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    public Integer getAmountPrecision() {
        return amountPrecision;
    }

    public void setAmountPrecision(Integer amountPrecision) {
        this.amountPrecision = amountPrecision;
    }

    public BigDecimal getExRate() {
        return exRate;
    }

    public void setExRate(BigDecimal exRate) {
        this.exRate = exRate;
    }

    public BigDecimal getMinTradeLimit() {
        return minTradeLimit;
    }

    public void setMinTradeLimit(BigDecimal minTradeLimit) {
        this.minTradeLimit = minTradeLimit;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Integer getOrderParameter() {
        return orderParameter;
    }

    public void setOrderParameter(Integer orderParameter) {
        this.orderParameter = orderParameter;
    }

    public Boolean getOutKline() {
        return outKline;
    }

    public void setOutKline(Boolean outKline) {
        this.outKline = outKline;
    }

    public Boolean getAllwowedTrade() {
        return allwowedTrade;
    }

    public void setAllwowedTrade(Boolean allwowedTrade) {
        this.allwowedTrade = allwowedTrade;
    }

    public PartitionType getPartitionType() {
        return partitionType;
    }

    public void setPartitionType(PartitionType partitionType) {
        this.partitionType = partitionType;
    }
}
