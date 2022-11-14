package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.repository.DayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DayService {

    private DayRepo _dayRepo;

    @Autowired
    public DayService(DayRepo dayRepo) {
        this._dayRepo = dayRepo;
    }

    public Day getDay(UUID id) {
        return _dayRepo.findById(id).orElse(null);
    }

    public Day updateDay(Day day) {
        return _dayRepo.save(day);
    }
}
