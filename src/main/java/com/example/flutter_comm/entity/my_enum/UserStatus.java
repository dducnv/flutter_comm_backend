package com.example.flutter_comm.entity.my_enum;

import java.util.Objects;

public enum UserStatus {
    ACTIVE(1),BLOCKED(-1), UNDEFINED(0), DELETED(-2);
    private final int value;
    UserStatus(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
    public static UserStatus statusEnum(int value){
        for(UserStatus statusEnum : UserStatus.values()){
            if(Objects.equals(statusEnum.getValue(), value)){
                return statusEnum;
            }
        }
        return UNDEFINED;
    }
}
