package com.baixiaozheng.common.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class ZipUtil {

  public static String getDecompressed(Buffer data, String encoding) {
    String line = null;
    try {
      GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(data.getBytes()));
      InputStreamReader isr = new InputStreamReader(gunzip, Charset.forName(encoding));
      BufferedReader br = new BufferedReader(isr);
      line = br.readLine();
    } catch (IOException e) {
      System.out.println("unzip error: " + e.getMessage());
    }

    return line;
  }

  public static JsonObject getDecompressedJson(Buffer data, String encoding) {
    return new JsonObject(getDecompressed(data, encoding));
  }

  public Buffer compress(String str, String encoding) {
    if (str == null || str.length() == 0) {
      return null;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    GZIPOutputStream gzip;
    try {
      gzip = new GZIPOutputStream(out);
      gzip.write(str.getBytes(encoding));
      gzip.close();
    } catch (IOException e) {
      log.error("gzip compress error {}", e);
    }
    return Buffer.buffer(out.toByteArray());
  }

}
