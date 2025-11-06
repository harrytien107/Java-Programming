package com.project.codebasespringjpa;

import com.project.codebasespringjpa.controller.ProgramController;
import com.project.codebasespringjpa.controller.StaticController;
import com.project.codebasespringjpa.controller.SurveyController;
import com.project.codebasespringjpa.controller.UserController;
import com.project.codebasespringjpa.dto.appointment.request.AppointmentRequest;
import com.project.codebasespringjpa.dto.course.request.CourseRequest;
import com.project.codebasespringjpa.dto.program.request.ProgramRequest;
import com.project.codebasespringjpa.dto.program.response.ProgramResponse;
import com.project.codebasespringjpa.dto.statics.DashboardResponse;
import com.project.codebasespringjpa.dto.survey.request.SurveyResultRequest;
import com.project.codebasespringjpa.dto.user.response.UserResponse;
import com.project.codebasespringjpa.entity.*;
import com.project.codebasespringjpa.mapper.*;
import com.project.codebasespringjpa.repository.IProgramRepository;
import com.project.codebasespringjpa.repository.ISurveyResultRepository;
import com.project.codebasespringjpa.repository.IUserRepository;
import com.project.codebasespringjpa.service.imp.ProgramService;
import com.project.codebasespringjpa.service.imp.UserService;
import com.project.codebasespringjpa.service.interfaces.*;
import com.project.codebasespringjpa.util.UtilConst;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Draff {
    int id;
//    IProgramService
//    IProgramService
//    StaticController
//    StaticController
}
