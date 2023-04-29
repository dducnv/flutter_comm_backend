package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.RoleDto;
import com.example.flutter_comm.entity.Role;
import com.example.flutter_comm.repository.RoleRepository;
import com.example.flutter_comm.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public boolean saveAll() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

//    @Override
//    public boolean setRolesForUser(List<RoleDto> roleDtos) {
//        return false;
//    }

    @Override
    public RoleDto toDto(Role role) {
        return RoleDto.builder()
                .name(role.getRoleName())
                .description(role.getDescription())
                .build();
    }

}
