package com.project.codebasespringjpa.service.imp;

import com.project.codebasespringjpa.dto.appointment.request.AppointmentRequest;
import com.project.codebasespringjpa.dto.appointment.request.AppointmentSearch;
import com.project.codebasespringjpa.dto.appointment.request.StatusRequest;
import com.project.codebasespringjpa.dto.appointment.response.AppointmentResponse;
import com.project.codebasespringjpa.dto.course.request.CourseRequest;
import com.project.codebasespringjpa.dto.course.request.CourseSearch;
import com.project.codebasespringjpa.dto.course.response.CourseResponse;
import com.project.codebasespringjpa.dto.user.response.UserResponse;
import com.project.codebasespringjpa.entity.AppointmentEntity;
import com.project.codebasespringjpa.entity.CourseEntity;
import com.project.codebasespringjpa.entity.ObjectEntity;
import com.project.codebasespringjpa.entity.UserEntity;
import com.project.codebasespringjpa.exception.AppException;
import com.project.codebasespringjpa.exception.ErrorCode;
import com.project.codebasespringjpa.mapper.AppointmentMapper;
import com.project.codebasespringjpa.mapper.CourseMapper;
import com.project.codebasespringjpa.repository.IAppointmentRepository;
import com.project.codebasespringjpa.repository.ICourseRepository;
import com.project.codebasespringjpa.repository.IObjectRepository;
import com.project.codebasespringjpa.service.interfaces.IAppointmentService;
import com.project.codebasespringjpa.service.interfaces.ICourseService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentService implements IAppointmentService {
    @Autowired
    IAppointmentRepository appointmentRepository;
    @Autowired
    AppointmentMapper appointmentMapper;
    @Autowired
    private UserService userService;

    @Override
    public AppointmentEntity findEntityById(Long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.APPOINMENT_NOT_FOUND));
    }

    @Override
    public AppointmentResponse create(AppointmentRequest request) {
        AppointmentEntity save = appointmentRepository.save(appointmentMapper.toEntity(request));
        return appointmentMapper.toResponse(save);
    }

    @Override
    public AppointmentResponse update(Long id, AppointmentRequest request) {
        UserResponse user = userService.findByUsername(request.getUsername());
        UserResponse special = userService.findByUsername(request.getSpecialistName());

        AppointmentEntity appUpdate = this.findEntityById(id);
        appUpdate.setUserId(user.getId());
        appUpdate.setSpecialistId(special.getId());
        appUpdate.setDate(request.getDate());
        appUpdate.setHours(request.getHours());
        appUpdate.setDuration(request.getDuration());
        appUpdate.setStatus(appUpdate.getStatus());

        return appointmentMapper.toResponse(appointmentRepository.save(appUpdate));
    }

    @Override
    public AppointmentResponse changeStatus(Long id, StatusRequest request) {
        AppointmentEntity appUpdate = this.findEntityById(id);
        appUpdate.setStatus(request.getStatus());
        return appointmentMapper.toResponse(appointmentRepository.save(appUpdate));
    }

    @Override
    public AppointmentResponse findByid(Long id) {
        AppointmentEntity find = this.findEntityById(id);
        return appointmentMapper.toResponse(find);
    }

    @Override
    public Page<AppointmentResponse> findAll(Pageable pageable, AppointmentSearch appointmentSearch) {
        return appointmentRepository.findAll(appointmentSearch.getUsername(), appointmentSearch.getSpecialistName(), appointmentSearch.getKeyword(), appointmentSearch.getStatus(), appointmentSearch.getDate(),
                  pageable).map(it -> appointmentMapper.toResponse(it));
    }

    @Override
    public void delete(Long id) {
        AppointmentEntity appointmentFind = this.findEntityById(id);
        appointmentFind.setIsDelete(true);
        appointmentRepository.save(appointmentFind);
    }
}
