package com.baixiaozheng.common.setting.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisClient {

  @Autowired
  private RedisTemplate<Serializable, Object> redisTemplate;

  /**
   * 批量删除对应的value
   *
   * @param keys
   */
  public void remove(final String... keys) {
    for (String key : keys) {
      remove(key);
    }
  }
  /**
   * 获取指定key中链表的所有数据
   * @param
   */
  public List<Object> getListValue(String key) {
    ListOperations<Serializable, Object> vo = redisTemplate.opsForList();
    return vo.range(key, 0,-1);

  }
  /**
   * 批量删除key
   *
   * @param pattern
   */
  public void removePattern(final String pattern) {
    Set<Serializable> keys = redisTemplate.keys(pattern);
    if (keys.size() > 0) {
      redisTemplate.delete(keys);
    }
  }

  /**
   * 删除对应的value
   *
   * @param key
   */
  public void remove(final String key) {
    if (exists(key)) {
      redisTemplate.delete(key);
    }
  }

  /**
   * 获取一个hash
   *
   * @param cacheKey
   * @return
   */
  public Map<String, Object> getCacheMap(String cacheKey) {
    BoundHashOperations<Serializable, String, Object> bound = redisTemplate.boundHashOps(cacheKey);
    return bound.entries();
  }

  /**
   * 从hash里获取一个值
   *
   * @param cacheKey
   * @param key
   * @return
   */
  public Object getDataFromCacheMap(String cacheKey, Object key) {
    BoundHashOperations<Serializable, Object, Object> bound = redisTemplate.boundHashOps(cacheKey);
    return bound.get(key);
  }

  /**
   * 向hash放进一个键值
   *
   * @param cacheKey
   * @param key
   * @param value
   */
  public void setDataFromCacheMap(String cacheKey, Object key, Object value) {
    BoundHashOperations<Serializable, Object, Object> bound = redisTemplate.boundHashOps(cacheKey);
    bound.put(key, value);
  }

  /**
   * 删除hash一个键值
   *
   * @param cacheKey
   * @param key
   */
  public void removeDataFromCacheMap(String cacheKey, Object key) {
    BoundHashOperations<Serializable, Object, Object> bound = redisTemplate.boundHashOps(cacheKey);
    bound.delete(key);
  }

  /**
   * 判断缓存中是否有对应的value
   *
   * @param key
   * @return
   */
  public boolean exists(final String key) {
    return redisTemplate.hasKey(key);
  }

  /**
   * 读取缓存
   *
   * @param key
   * @return
   */
  public Object get(final String key) {
    Object result = null;
    ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
    result = operations.get(key);
    return result;
  }

  /**
   * 写入缓存
   *
   * @param key
   * @param value
   * @return
   */
  public boolean set(final String key, Object value) {
    boolean result = false;
    try {
      ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
      operations.set(key, value);
      result = true;
    } catch (Exception e) {
      log.error("set cache error", e);
    }
    return result;
  }

  /**
   * 写入缓存
   *
   * @param key
   * @param value
   * @return
   */
  public boolean set(final String key, Object value, Long expireTime) {
    boolean result = false;
    try {
      ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
      operations.set(key, value);
      redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);

      result = true;
    } catch (Exception e) {
      log.error("set cache error", e);
    }
    return result;
  }

  /**
   * HashSet方式写入缓存
   *
   * @param key
   * @param map
   * @return
   */
  public boolean hmset(String key, Map<String, Object> map) {
    try {
      redisTemplate.opsForHash().putAll(key, map);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 获取map的value
   *
   * @param key
   * @param hashKey
   * @return
   */
  public Object getMapValue(String key, String hashKey) {
    try {
      Object obj = redisTemplate.opsForHash().get(key, hashKey);
      return obj;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public long increment(final String key, long delta) {
    return redisTemplate.opsForValue().increment(key, delta);
  }

  public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  // ============================set=============================

  /**
   * 根据key获取Set中的所有值
   *
   * @param key 键
   * @return
   */
  public Set<Object> sGet(String key) {
    try {
      return redisTemplate.opsForSet().members(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 根据value从一个set中查询,是否存在
   *
   * @param key   键
   * @param value 值
   * @return true 存在 false不存在
   */
  public boolean sHasKey(String key, Object value) {
    try {
      return redisTemplate.opsForSet().isMember(key, value);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 将数据放入set缓存
   *
   * @param key    键
   * @param values 值 可以是多个
   * @return 成功个数
   */
  public long sSet(String key, Object... values) {
    try {
      return redisTemplate.opsForSet().add(key, values);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 获取set缓存的长度
   *
   * @param key 键
   * @return
   */
  public long sGetSetSize(String key) {
    try {
      return redisTemplate.opsForSet().size(key);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 移除值为value的
   *
   * @param key    键
   * @param values 值 可以是多个
   * @return 移除的个数
   */
  public long sRemove(String key, Object... values) {
    try {
      Long count = redisTemplate.opsForSet().remove(key, values);
      return count;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  // ===============================list=================================

  /**
   * 获取list缓存的内容
   *
   * @param key   键
   * @param start 开始
   * @param end   结束 0 到 -1代表所有值
   * @return
   */
  public List<Object> lGet(String key, long start, long end) {
    try {
      return redisTemplate.opsForList().range(key, start, end);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取list缓存的长度
   *
   * @param key 键
   * @return
   */
  public long lGetListSize(String key) {
    try {
      return redisTemplate.opsForList().size(key);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 通过索引 获取list中的值
   *
   * @param key   键
   * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
   * @return
   */
  public Object lGetIndex(String key, long index) {
    try {
      return redisTemplate.opsForList().index(key, index);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @return
   */
  public boolean lSet(String key, Object value) {
    try {
      redisTemplate.opsForList().rightPush(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @return
   */
  public boolean lSet(String key, List<Object> value) {
    try {
      redisTemplate.opsForList().rightPushAll(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 根据索引修改list中的某条数据
   *
   * @param key   键
   * @param index 索引
   * @param value 值
   * @return
   */
  public boolean lUpdateIndex(String key, long index, Object value) {
    try {
      redisTemplate.opsForList().set(key, index, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 移除N个值为value
   *
   * @param key   键
   * @param count 移除多少个
   * @param value 值
   * @return 移除的个数
   */
  public long lRemove(String key, long count, Object value) {
    try {
      Long remove = redisTemplate.opsForList().remove(key, count, value);
      return remove;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public boolean zSetAdd(String key, Object value, double score){
    try {
      redisTemplate.opsForZSet().add(key, value, score);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  public boolean zSetAdd(String key, Object value, double score, long second){
    try {
      redisTemplate.opsForZSet().add(key, value, score);
      redisTemplate.expire(key, second, TimeUnit.SECONDS);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean zSetRemove(String key, Object... values){
    try {
      redisTemplate.opsForZSet().remove(key, values);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Set<Object> zSetRange(String key, long start, long end){
      return redisTemplate.opsForZSet().range(key, start, end);
  }

  public Set<Object> zSetReverseRange(String key, long start, long end){
    return redisTemplate.opsForZSet().reverseRange(key, start, end);
  }

  public Long zSetLength(String key){
    return redisTemplate.opsForZSet().zCard(key);
  }


  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @return
   */
  public boolean lPush(String key, Object value) {
    try {
      redisTemplate.opsForList().leftPush(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 删除list首尾，只保留 [start, end] 之间的值
   *
   * @param key
   * @param start
   * @param end
   */
  public void lTrim(String key, Integer start, Integer end) {
    redisTemplate.opsForList().trim(key, start, end);
  }
}
