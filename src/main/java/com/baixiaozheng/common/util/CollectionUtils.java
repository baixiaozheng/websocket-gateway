package com.baixiaozheng.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtils extends org.springframework.util.CollectionUtils{
    private static Logger log = LoggerFactory.getLogger(CollectionUtils.class);

    private final static String split = ",";

    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    public static <T> List<List<T>> splitList(List<T> source,int size) throws Exception {
        if(CollectionUtils.isEmpty(source)){
            throw new Exception("CollectionUtils.splitList() ERROR：source is empty");
        }
        if(size<1){
            throw new Exception("CollectionUtils.splitList() ERROR：size < 1");
        }
        List<List<T>> result = new ArrayList<>();
        int count = source.size();
        int index = size;
        for(int i=0;i<(count>index&&count%index==0?count/index:count/index+1);i++){
            int offset = (i*index+index)>count?count:(i*index+index);
            List<T> sub = source.subList(i*index,offset);
            result.add(sub);
        }
        return result;
    }

    public static String toString(@Nullable Collection<?> collection, String split) {
        if(StringUtils.isBlank(split)){
            split = CollectionUtils.split;
        }
        String result = "";
        if(CollectionUtils.isEmpty(collection)){
            return result;
        }
        for(Object o:collection){
            if(StringUtils.isBlank(result)){
                result = o.toString();
            }else{
                result = result + split + o.toString();
            }
        }
        return result;
    }

    public static String toString(@Nullable Collection<?> collection) {
        String result = "";
        if(CollectionUtils.isEmpty(collection)){
            return result;
        }
        for(Object o:collection){
            if(StringUtils.isBlank(result)){
                result = o.toString();
            }else{
                result = result + CollectionUtils.split + o.toString();
            }
        }
        return result;
    }

    public static <T> List<T> getFieldList(@Nullable Collection<?> collection, String fieldName) throws Exception {
        List<T> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(collection)){
            return result;
        }
        for(Object object:collection){
            Field[] fields = object.getClass().getDeclaredFields();
            for(Field field:fields){
                if(field.getName().equals(fieldName)){
                    field.setAccessible(true);
                    Object value = null;
                    try {
                        value = field.get(object);
                    } catch (IllegalAccessException e) {
                        log.error("获取对象属性集合异常【反射获取属性值异常】："+e.getMessage(),e);
                        throw new Exception("获取对象属性集合异常【反射获取属性值异常】："+e.getMessage());
                    }
                    try {
                        if(value!=null) {
                            result.add((T) value);
                        }
                    } catch (Exception e) {
                        log.error("获取对象属性集合异常【类型转换异常】："+e.getMessage(),e);
                        throw new Exception("获取对象属性集合异常【类型转换异常】："+e.getMessage());
                    }
                }
            }
        }
        return result;
    }

    public static <T,E> Map<T,E> getFieldMap(@Nullable Collection<E> collection, String fieldName) throws Exception {
        Map<T,E> result = new HashMap<>();
        if (CollectionUtils.isEmpty(collection)) {
            return result;
        } else {
            for (E object : collection) {
                Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals(fieldName)) {
                        field.setAccessible(true);
                        Object value = null;
                        try {
                            value = field.get(object);
                        } catch (IllegalAccessException e) {
                            log.error("获取对象属性集合异常【反射获取属性值异常】：" + e.getMessage(), e);
                            throw new Exception("获取对象属性集合异常【反射获取属性值异常】：" + e.getMessage());
                        }
                        try {
                            if(value!=null) {
                                result.put((T) value, object);
                            }
                        } catch (Exception e) {
                            log.error("获取对象属性集合异常【类型转换异常】：" + e.getMessage(), e);
                            throw new Exception("获取对象属性集合异常【类型转换异常】：" + e.getMessage());
                        }
                    }
                }
            }
            return result;
        }
    }

    public static <T,E> Map<T,List<E>> getFieldMapWithList(@Nullable Collection<E> collection, String fieldName) throws Exception {
        Map<T,List<E>> result = new HashMap<>();
        if (CollectionUtils.isEmpty(collection)) {
            return result;
        } else {
            for (E object : collection) {
                Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals(fieldName)) {
                        field.setAccessible(true);
                        T value = null;
                        try {
                            value = (T)field.get(object);
                        } catch (IllegalAccessException e) {
                            log.error("获取对象属性集合异常【反射获取属性值异常】：" + e.getMessage(), e);
                            throw new Exception("获取对象属性集合异常【反射获取属性值异常】：" + e.getMessage());
                        }
                        try {
                            if(value!=null) {
                                List<E> list = result.get(value);
                                if (list == null) {
                                    list = new ArrayList<>();
                                }
                                list.add(object);
                                result.put(value, list);
                            }
                        } catch (Exception e) {
                            log.error("获取对象属性集合异常【类型转换异常】：" + e.getMessage(), e);
                            throw new Exception("获取对象属性集合异常【类型转换异常】：" + e.getMessage());
                        }
                    }
                }
            }
            return result;
        }
    }

    public static <T> List<T> asList(T...params){
        List<T> list = new ArrayList<>();
        for(T t:params){
            list.add(t);
        }
        return list;
    }

    public static Map<String,String> asMap(String...params){
        Map<String,String> map = new HashMap<>();
        for(String param:params){
            String[] entry = param.split("::");
            map.put(entry[0],entry[1]);
        }
        return map;
    }

    public static Map<String,Object> asMap(List<String> keyList,List<Object> valueList){
        Map<String,Object> map = new HashMap<>();
        for(int i=0;i<keyList.size();i++){
            map.put(keyList.get(i),valueList.get(i));
        }
        return map;
    }

    public static List<Long> asLongList(String source){
        List<Long> list = new ArrayList<>();
        if(StringUtils.isBlank(source)){
            return list;
        }
        String[] strs = source.split(",");
        for(String s:strs){
            list.add(Long.valueOf(s));
        }
        return list;
    }

    public static List<Integer> asIntegerList(String source){
        List<Integer> list = new ArrayList<>();
        if(StringUtils.isBlank(source)){
            return list;
        }
        String[] strs = source.split(",");
        for(String s:strs){
            list.add(Integer.valueOf(s));
        }
        return list;
    }

    public static List<String> asStringList(String source){
        List<String> list = new ArrayList<>();
        if(StringUtils.isBlank(source)){
            return list;
        }
        String[] strs = source.split(",");
        for(String s:strs){
            list.add(s);
        }
        return list;
    }

    /**
     * 转换String类型的List为Long类型的List
     * @param stringList String类型的List
     * @return Long类型的List
     */
    public static List<Long> asLongList(List<String> stringList){
        if(isEmpty(stringList)){
            return new ArrayList<Long>();
        }
        //map 进行转换
        return stringList.stream().map(str -> Long.parseLong(str)).collect(Collectors.toList());
    }
}
