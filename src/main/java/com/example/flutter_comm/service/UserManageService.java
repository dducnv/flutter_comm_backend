package com.example.flutter_comm.service;

import com.example.flutter_comm.entity.User;

import java.util.List;

public interface UserManageService {
    List<User> findAllUser();
}
