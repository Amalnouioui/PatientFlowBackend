package com.core.patient.services;

import com.core.patient.entities.Historique;

import java.util.List;

public interface HistorqueService {
    void createHistory(Historique historique);
    List<Historique> getHisory(Integer patientKey);
}
