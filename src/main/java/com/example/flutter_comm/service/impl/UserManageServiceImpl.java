package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.repository.UserRepository;
import com.example.flutter_comm.service.UserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class UserManageServiceImpl implements UserManageService {
    @Autowired
    UserRepository userRepository;
    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }
}
