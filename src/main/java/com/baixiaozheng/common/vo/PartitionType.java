package com.baixiaozheng.common.vo;

/**
 * 分区类型
 */
public enum PartitionType {
  MAIN(){
    @Override
    public String toString() {
      return "主分区";
    }
  },

  INNOVATION(){
    @Override
    public String toString() {
      return "创新区";
    }
  },
  TECHNPLOGY(){
    @Override
    public String toString() {
      return "科技区";
    }
  },
  OPTIMIZATION() {
    @Override
    public String toString() {
      return "优选区";
    }
  };


  public static PartitionType fromString(String typeString){
    for(PartitionType type: PartitionType.values()){
      if(type.name().equals(typeString)){
        return type;
      }
    }
    return null;
  }
}