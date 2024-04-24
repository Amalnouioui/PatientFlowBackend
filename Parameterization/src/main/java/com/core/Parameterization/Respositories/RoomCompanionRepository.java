package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.RoomCompanion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomCompanionRepository extends JpaRepository<RoomCompanion,Integer> {
    Optional<RoomCompanion> findByRoomCompanionName(String name);
}