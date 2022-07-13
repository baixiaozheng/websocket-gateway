package com.baixiaozheng.common.setting.manager.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter @Setter
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = 547969783988626410L;

    private String id;

    private String name;

    private String  age;

}
