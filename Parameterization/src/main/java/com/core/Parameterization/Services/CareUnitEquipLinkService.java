package com.core.Parameterization.Services;

import com.core.Parameterization.Entities.CareUnitEquipLink;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

public interface CareUnitEquipLinkService {
    void deleteEquipement (CareUnitEquipLink careUnitEquipLink);
}
