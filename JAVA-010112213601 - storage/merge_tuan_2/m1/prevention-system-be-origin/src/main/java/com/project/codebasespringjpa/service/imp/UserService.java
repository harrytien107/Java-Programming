package com.project.codebasespringjpa.service.imp;

import com.project.codebasespringjpa.configuration.security.UserDetailsImpl;
import com.project.codebasespringjpa.dto.user.request.UserRequest;
import com.project.codebasespringjpa.dto.user.request.UserSearch;
import com.project.codebasespringjpa.dto.user.response.UserResponse;
import com.project.codebasespringjpa.entity.MajorEntity;
import com.project.codebasespringjpa.entity.RoleEntity;
import com.project.codebasespringjpa.entity.UserEntity;
import com.project.codebasespringjpa.exception.AppException;
import com.project.codebasespringjpa.exception.ErrorCode;
import com.project.codebasespringjpa.mapper.UserMapper;
import com.project.codebasespringjpa.repository.IMajorRepository;
import com.project.codebasespringjpa.repository.IRoleRepository;
import com.project.codebasespringjpa.repository.IUserRepository;
import com.project.codebasespringjpa.service.interfaces.IUserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService implements IUserService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IRoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserMapper userMapper;
    @Autowired
    IMajorRepository majorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        return new UserDetailsImpl(user);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public UserDetailsImpl getUserInContext() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (userDetails instanceof UserDetailsImpl)
                return (UserDetailsImpl) userDetails;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public UserEntity findEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserResponse create(UserRequest request) {
        UserEntity save = userRepository.save(userMapper.toEntity(request));
        return userMapper.toResponse(save);
    }


    @Override
    public UserResponse findByid(Long id) {
        UserEntity userFind = this.findEntityById(id);
        return userMapper.toResponse(userFind);
    }



}
