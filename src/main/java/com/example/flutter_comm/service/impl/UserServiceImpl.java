package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.config.TokenProvider;
import com.example.flutter_comm.config.oauth2.user.OAuth2UserInfo;
import com.example.flutter_comm.dto.CredentialDto;
import com.example.flutter_comm.dto.UserInfoDto;
import com.example.flutter_comm.dto.user.*;
import com.example.flutter_comm.entity.Role;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.entity.my_enum.AuthProvider;
import com.example.flutter_comm.entity.my_enum.UserStatus;
import com.example.flutter_comm.exception.ApiRequestException;
import com.example.flutter_comm.repository.RoleRepository;
import com.example.flutter_comm.repository.UserRepository;
import com.example.flutter_comm.service.UserService;
import com.example.flutter_comm.utils.Generating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "userService")
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final int expireTime = 60 * 1000 * 5;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private TokenProvider jwtTokenUtil;

    private TopicInterestServiceImpl topicInterestService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TokenProvider jwtTokenUtil, TopicInterestServiceImpl topicInterestService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.topicInterestService = topicInterestService;
    }
//    @Autowired
//    AuthenticationManager authenticationManager;

    @Override
    public MyInfoDto myInfo() {
        User user = getUserFromToken();
        topicInterestService.filterTopicForUser(user);
        return toUserDTO(user);
    }

    @Override
    public User registerNewUserForSocial(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        UUID uuid = UUID.randomUUID();
        user.setProvider(authProvider);
        user.setProviderId(oAuth2UserInfo.getId());
        user.setAvatar(oAuth2UserInfo.getImageUrl());
        user.setName(oAuth2UserInfo.getName());
        user.setUuid(uuid);
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setUsername(Generating.generateUsername(oAuth2UserInfo.getName()));
        user.setPassword(passwordEncoder.encode(oAuth2UserInfo.getEmail() + "flutter_comm_since_2023"));
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        return user;
    }

    public void seedUserService(User userSave) {
        User user = new User();
        UUID uuid = UUID.randomUUID();
        user.setProvider(AuthProvider.local);
        user.setAvatar(userSave.getAvatar());
        user.setName(userSave.getName());
        user.setUuid(uuid);
        user.setEmail(userSave.getEmail());
        user.setUsername(Generating.generateUsername(userSave.getName()));
        user.setPassword(passwordEncoder.encode(userSave.getEmail() + "flutter_comm_since_2023"));
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        setRoleForUser(user);
    }

    private OtpResDto registerUser(RegisterDto registerDto) {
        boolean isExistUsername = userRepository.existsByUsername(registerDto.getUserName());
        if (isExistUsername) {
            throw new ApiRequestException("Username này đã tồn tại");
        }
        boolean isExistEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (isExistEmail) {
            throw new ApiRequestException("Email này đã tồn tại");
        }
        User user = new User();
        UUID uuid = UUID.randomUUID();
        user.setUuid(uuid);
        user.setName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUserName());
        User userSave = userRepository.save(user);
        GetOtpDto getOtpDto = new GetOtpDto();
        getOtpDto.setEmail(userSave.getEmail());
        return getOtp(getOtpDto);
    }

    ;

    @Override
    public UserInfoDto findByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        return toUserDetailDTO(user);
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }


    @Override
    public OtpResDto getOtp(GetOtpDto getOtpDto) {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByEmail(getOtpDto.getEmail()));
        if (!user.isPresent()) {
            throw new ApiRequestException("Người dùng chưa có trong hệ thống.");
        }
        User userInfo = user.get();
        Date expTime = new Date(System.currentTimeMillis() + expireTime);
        String password = String.valueOf(Generating.generatePassword(12, true));
        userInfo.setOne_time_password(passwordEncoder.encode(password));
        userInfo.setExpire_time(expTime);
        userRepository.save(userInfo);
        OtpResDto otpResDto = new OtpResDto();
        otpResDto.setOtp(password);
        otpResDto.setExpTime(expTime);
        return otpResDto;
    }

    public CredentialDto loginWithOTP(LoginEmailPasswordDto loginDto) {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByEmail(loginDto.getEmail()));
        if (!user.isPresent()) {
            throw new ApiRequestException("Người dùng chưa có trong hệ thống.");
        }
        User userInfo = user.get();
        boolean checkPasswordIsMatches = passwordEncoder.matches(loginDto.getPassword(), userInfo.getOne_time_password());
        if (!checkPasswordIsMatches) throw new ApiRequestException("Mã Otp không khớp, vui lòng thử lại.");
        boolean checkIsExpireOtpCode = true;
        if (new Date(System.currentTimeMillis()).before(userInfo.getExpire_time())) checkIsExpireOtpCode = false;
        if (checkIsExpireOtpCode) throw new ApiRequestException("OTP đã hết hạn, vui lòng thử lại.");
        String authorities = getAuthority(userInfo).stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        final String token = jwtTokenUtil.generateTokenCustom(userInfo.getEmail(), authorities);
        return CredentialDto.builder()
                .accessToken(token)
                .expiresIn(System.currentTimeMillis() + TokenProvider.ONE_DAY * 7)
                .tokenType("Bearer")
                .scope("basic_info")
                .build();

    }

    public User findUserByUsernameOrEmail(String email, String username) {
        User user = userRepository.findFirstByEmailOrUsername(email, username);
        return user;
    }


    public User setRoleForUser(User user) {
        Role role;
        if (user.getEmail().split("@")[0].equals("ducbe2k2")) {
            role = roleRepository.findByRoleName("ADMIN");
        } else {
            role = roleRepository.findByRoleName("USER");
        }
        user.addRole(role);
        return userRepository.save(user);
    }

    public User getUserFromToken() {
        Object userInfo = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(userInfo);
        return findUserByEmail(userInfo.toString());
    }
    public UserInfoDto toUserDetailDTO(User user) {
        return UserInfoDto.builder()
                .uuid(user.getUuid())
                .avatar(user.getAvatar())
                .name(user.getName())
                .username(user.getUsername())
                .username(user.getUsername())
//                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public MyInfoDto toUserDTO(User user) {
        String authorities = getAuthority(user).stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        final String token = jwtTokenUtil.generateTokenCustom(user.getEmail(), authorities);
        CredentialDto credentialDto = CredentialDto.builder()
                .accessToken(token)
                .expiresIn(System.currentTimeMillis() + TokenProvider.ONE_DAY * 7)
                .tokenType("Bearer")
                .scope("basic_info")
                .build();
        return MyInfoDto.builder()
                .uuid(user.getUuid())
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .updatedAt(user.getUpdatedAt())
                .createdAt(user.getCreatedAt())
                .userStatus(user.getStatus())
                .credential(credentialDto)
                .build();
    }

    public AuthorForPostDto toAuthorForPostDto(User user) {
        return AuthorForPostDto.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        System.out.println(user.getRoles());
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }
}
