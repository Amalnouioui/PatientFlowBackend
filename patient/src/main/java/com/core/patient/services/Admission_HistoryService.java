package com.core.patient.services;

import com.core.patient.entities.Admission_History;

import java.util.List;

public interface Admission_HistoryService {
    void createHistory(Admission_History historique);
    List<Admission_History> getHisory(Integer patientKey);
}
