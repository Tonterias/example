package com.example.web.repository;

import com.example.web.domain.Appuser;
import com.example.web.domain.Notification;
import com.example.web.service.dto.AppuserDTO;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * Spring Data JPA repository for the Appuser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppuserRepository extends JpaRepository<Appuser, Long>, JpaSpecificationExecutor<Appuser> {

    @Nullable
    Optional<Appuser> findByPlateNumber(String plateNumber);
}
