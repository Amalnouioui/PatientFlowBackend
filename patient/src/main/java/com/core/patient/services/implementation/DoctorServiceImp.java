package com.core.patient.services.implementation;

import com.core.patient.entities.Doctor;
import com.core.patient.repositories.DoctorRepository;
import com.core.patient.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorServiceImp  implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Override
    public Doctor getDoctor(Integer doctorKey){
        Optional<Doctor>OptionalDoctor=doctorRepository.findById(doctorKey);
        if(OptionalDoctor.isPresent()){
            return OptionalDoctor.get();
        }
        throw new IllegalStateException("Aucun doctor avec cet ID");
    }
}
