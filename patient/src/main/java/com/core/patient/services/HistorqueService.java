package com.core.patient.services;

import com.core.patient.entities.Transfer;

import java.util.List;

public interface HistorqueService {
    void createHistory(Transfer transfer);
    List<Transfer> getHisory(Integer patientKey);
}
