package com.project.codebasespringjpa.service.interfaces;

import com.project.codebasespringjpa.dto.appointment.request.AppointmentRequest;
import com.project.codebasespringjpa.dto.appointment.request.AppointmentSearch;
import com.project.codebasespringjpa.dto.appointment.request.StatusRequest;
import com.project.codebasespringjpa.dto.appointment.response.AppointmentResponse;
import com.project.codebasespringjpa.dto.course.request.CourseRequest;
import com.project.codebasespringjpa.dto.course.request.CourseSearch;
import com.project.codebasespringjpa.dto.course.response.CourseResponse;
import com.project.codebasespringjpa.entity.AppointmentEntity;
import com.project.codebasespringjpa.entity.CourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAppointmentService {
    AppointmentEntity findEntityById(Long id);
    AppointmentResponse create(AppointmentRequest request);
    AppointmentResponse update(Long id, AppointmentRequest request);
    AppointmentResponse changeStatus(Long id, StatusRequest request);
    AppointmentResponse findByid(Long id);
    Page<AppointmentResponse> findAll(Pageable pageable, AppointmentSearch appointmentSearch);
    void delete(Long id);
}
