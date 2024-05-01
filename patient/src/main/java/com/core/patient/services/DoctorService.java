package com.core.patient.services;

import com.core.patient.entities.Doctor;

public interface DoctorService {
    Doctor getDoctor(Integer doctorKey);
    void createDoctor(Doctor doctor);
}
