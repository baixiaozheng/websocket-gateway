package com.baixiaozheng.common.setting.manager.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class City implements Comparable<City>, Cloneable{

    @Getter @Setter
    private String city;

    @Getter @Setter
    private String code;

    public City(String city, String code){
        this.city = city;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof City) {
            City s = (City) o;
            return Objects.equals(this.city, s.city);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.city);
    }

    @Override
    public int compareTo(City o) {
        return this.getCity().compareTo(o.getCity());
    }

    @Override
    public String toString() {
        return this.getCity();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            return null;
        }
    }
}
