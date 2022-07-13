package com.baixiaozheng.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 格式化工具
 */
public class FormatUtil {
  public static String directFormatBigDecimal(BigDecimal bigDecimal, Integer precision) {
    if (precision > 0) {
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append("0.");

      for (int index = 0; index < precision; index++) {
        stringBuffer.append("0");
      }

      DecimalFormat df = new DecimalFormat(stringBuffer.toString());
      return df.format(bigDecimal);
    } else {
      return bigDecimal.intValue() + "";
    }
  }

  public static int getNumberDecimalDigits(String number) {
    int dcimalDigits = 0;
    String balanceStr = new BigDecimal(number).toPlainString();
    int indexOf = balanceStr.indexOf(".");
    if (indexOf > 0) {
      dcimalDigits = balanceStr.length() - 1 - indexOf;
    }
    return dcimalDigits;
  }

  public static String formatPpwTokenKey(String key, Integer id) {
    return key + id;
  }

  public static void main(String[] args) {
    System.out.println(getNumberDecimalDigits("0"));


//        BigDecimal bigDecimal = new BigDecimal("0.4469860000000000000");
//
//        bigDecimal = bigDecimal.setScale(5,BigDecimal.ROUND_DOWN);
//
//        System.out.println(directFormatBigDecimal(bigDecimal,5));


  }

  public static String formatBigDecimalDown(BigDecimal bigDecimal, Integer precision) {
    if (precision > 0) {
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append("0.");

      for (int index = 0; index < precision; index++) {
        stringBuffer.append("0");
      }

      DecimalFormat df = new DecimalFormat(stringBuffer.toString());
      df.setRoundingMode(RoundingMode.DOWN);
      return df.format(bigDecimal);
    } else {
      return bigDecimal.intValue() + "";
    }
  }
}
