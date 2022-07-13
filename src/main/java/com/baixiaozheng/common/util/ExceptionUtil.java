package com.baixiaozheng.common.util;

import io.netty.util.internal.StringUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ExceptionUtil {

  public static String getLimitedStackTrace(Throwable throwable) {
    return getLimitedStackTrace(throwable, 10);
  }

  public static String getLimitedStackTrace(Throwable throwable, int count) {
    if (throwable != null && throwable.getStackTrace() != null) {
      StackTraceElement[] stackTrace = throwable.getStackTrace();
      if (stackTrace.length >= count) {
        stackTrace = Arrays.copyOf(stackTrace, count);
      }
      return Arrays.stream(stackTrace)
              .map(StackTraceElement::toString)
              .collect(Collectors.joining("\n "));
    }
    return StringUtil.EMPTY_STRING;
  }

}
