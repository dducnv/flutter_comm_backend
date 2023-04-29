package com.example.flutter_comm.service;

import com.example.flutter_comm.dto.RoleDto;
import com.example.flutter_comm.entity.Role;
import java.util.List;


public interface RoleService {
    List<RoleDto> findAll();
    boolean save();
    boolean saveAll();
    boolean delete();

//    boolean setRolesForUser(List<RoleDto> roleDtos);

    RoleDto toDto(Role role);
}
