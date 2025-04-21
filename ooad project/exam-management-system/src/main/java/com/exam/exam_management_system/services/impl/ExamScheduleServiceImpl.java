package com.exam.exam_management_system.services.impl;

import com.exam.exam_management_system.models.ExamSchedule;
import com.exam.exam_management_system.repositories.ExamScheduleRepository;
import com.exam.exam_management_system.services.ExamScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamScheduleServiceImpl implements ExamScheduleService {
    @Autowired
    private ExamScheduleRepository scheduleRepository;

    @Override
    public ExamSchedule saveSchedule(ExamSchedule schedule) {
        return scheduleRepository.save(schedule);
    }
} 