package com.core.patient.services;

import com.core.patient.entities.Historique;

public interface HistorqueService {
    void createHistory(Historique historique);
    Historique getHisory(Integer patientKey);
}
