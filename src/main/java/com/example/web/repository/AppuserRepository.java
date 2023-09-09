package com.example.web.repository;

import com.example.web.domain.Appuser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Appuser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppuserRepository extends JpaRepository<Appuser, Long>, JpaSpecificationExecutor<Appuser> {}
