package com.example.flutter_comm.entity.my_enum;

import java.util.Objects;

public enum ItemType {
    LINK(1), EMBED(2), TEXT_BLOCK(3), HEAD_TEXT(4), UNDEFINED(-1);
    private final int value;
    ItemType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
    public static ItemType itemType(int value){
        for(ItemType itemType : ItemType.values()){
            if(Objects.equals(itemType.getValue(), value)){
                return itemType;
            }
        }
        return LINK;
    }
}
