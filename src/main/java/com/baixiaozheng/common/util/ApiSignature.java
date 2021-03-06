package com.baixiaozheng.common.util;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class ApiSignature {
  static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
  static final ZoneId ZONE_GMT = ZoneId.of("Z");
  public static String op = "op";
  public static String opValue = "auth";
  public static String accessKeyId = "AccessKeyId";
  public static String signatureMethod = "SignatureMethod";
  public static String signatureMethodValue = "HmacSHA256";
  public static String signatureVersion = "SignatureVersion";
  public static String signatureVersionValue = "2";
  public static String timestamp = "Timestamp";
  public static String signature = "Signature";
  final Logger log = LoggerFactory.getLogger(getClass());

  /**
   * 使用标准URL Encode编码。注意和JDK默认的不同，空格被编码为%20而不是+。
   *
   * @param s String字符串
   * @return URL编码后的字符串
   */
  public static String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException("UTF-8 encoding not supported!");
    }
  }

  /**
   * 创建一个有效的签名。该方法为客户端调用，将在传入的params中添加AccessKeyId、Timestamp、SignatureVersion、SignatureMethod、Signature参数。
   *
   * @param appKey       AppKeyId.
   * @param appSecretKey AppKeySecret.
   * @param method       请求方法，"GET"或"POST"
   * @param host         请求域名，例如"be.huobi.com"
   * @param uri          请求路径，注意不含?以及后的参数，例如"/v1/api/info"
   * @param params       原始请求参数，以Key-Value存储，注意Value不要编码
   */
  public void createSignature(String appKey, String appSecretKey, String method, String host,
                              String uri, Map<String, String> params) {
    StringBuilder sb = new StringBuilder(1024);
    sb.append(method.toUpperCase()).append('\n') // GET
            .append(host.toLowerCase()).append('\n') // Host
            .append(uri).append('\n'); // /path
    params.remove("Signature");
    params.put("AccessKeyId", appKey);
    params.put("SignatureVersion", "2");
    params.put("SignatureMethod", "HmacSHA256");
    params.put("Timestamp", gmtNow());
    // build signature:
    SortedMap<String, String> map = new TreeMap<>(params);
    for (Map.Entry<String, String> entry : map.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      sb.append(key).append('=').append(urlEncode(value)).append('&');
    }
    // remove last '&':
    sb.deleteCharAt(sb.length() - 1);
    log.error(sb.toString());
    // sign:
    Mac hmacSha256 = null;
    try {
      hmacSha256 = Mac.getInstance("HmacSHA256");
      SecretKeySpec secKey =
              new SecretKeySpec(appSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      hmacSha256.init(secKey);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("No such algorithm: " + e.getMessage());
    } catch (InvalidKeyException e) {
      throw new RuntimeException("Invalid key: " + e.getMessage());
    }
    String payload = sb.toString();
    byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
    String actualSign = Base64.getEncoder().encodeToString(hash);
    params.put("Signature", actualSign);
    //PrivateSignature
    byte[] signData = null;
//        try {
//            signData = sign(actualSign.getBytes(), privateKey);
//            String privateSignature = Base64.getEncoder().encodeToString(signData);
//            params.put("PrivateSignature", privateSignature);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }

    if (log.isDebugEnabled()) {
      log.debug("Dump parameters:");
      for (Map.Entry<String, String> entry : params.entrySet()) {
        log.debug("  key: " + entry.getKey() + ", value: " + entry.getValue());
      }
    }
  }

  /**
   * @param data
   * @param privateKey
   * @return
   * @throws Exception
   */
  public byte[] sign(byte[] data, String privateKeyStr) throws Exception {
    Security.addProvider(new BouncyCastleProvider());

    byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
    ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);

//        byte[] server_sec1 = DatatypeConverter.parseBase64Binary(privateKeyStr);
//        ASN1Sequence seq = ASN1Sequence.getInstance(server_sec1);
//        org.bouncycastle.asn1.sec.ECPrivateKey pKey = org.bouncycastle.asn1.sec.ECPrivateKey.getInstance(seq);
//        AlgorithmIdentifier algId = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, pKey.getParameters());
//        byte[] server_pkcs8 = new PrivateKeyInfo(algId, pKey).getEncoded();
//        KeyFactory fact = KeyFactory.getInstance ("EC","BC");
//        PrivateKey privateKey = fact.generatePrivate (new PKCS8EncodedKeySpec(server_pkcs8));

    Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", new BouncyCastleProvider());
    ecdsaSign.initSign(privateKey);
    ecdsaSign.update(data);
    byte[] signData = ecdsaSign.sign();
    return signData;
  }

  /**
   * Return epoch seconds
   */
  long epochNow() {
    return Instant.now().getEpochSecond();
  }

  String gmtNow() {
    return Instant.ofEpochSecond(epochNow()).atZone(ZONE_GMT).format(DT_FORMAT);
  }

}
