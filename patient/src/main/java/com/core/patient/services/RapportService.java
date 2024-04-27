package com.core.patient.services;

import com.core.patient.entities.Rapport;

import java.util.List;

public interface RapportService {
  List<Rapport> getRapport(Integer patientKey);
  void createRapport(Rapport rapport);
  void updatedRapport(Integer rapportKey,Rapport rapport);
  Rapport getById(Integer rapportKey);
  void deleteRapport(Integer rapportKey);
}
