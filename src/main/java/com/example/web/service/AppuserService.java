package com.example.web.service;

import com.example.web.service.dto.AppuserDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.example.web.domain.Appuser}.
 */
public interface AppuserService {
    /**
     * Save a appuser.
     *
     * @param appuserDTO the entity to save.
     * @return the persisted entity.
     */
    AppuserDTO save(AppuserDTO appuserDTO);

    /**
     * Updates a appuser.
     *
     * @param appuserDTO the entity to update.
     * @return the persisted entity.
     */
    AppuserDTO update(AppuserDTO appuserDTO);

    /**
     * Partially updates a appuser.
     *
     * @param appuserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppuserDTO> partialUpdate(AppuserDTO appuserDTO);

    /**
     * Get all the appusers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppuserDTO> findAll(Pageable pageable);

    /**
     * Get the "id" appuser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppuserDTO> findOne(Long id);

    /**
     * Get the "plateNumber" appuser.
     *
     * @param plateNumber the id of the entity.
     * @return the entity.
     */
    // QUESTION: Why do I have to use an Optional????
    Optional<AppuserDTO> findByPlateNumber(String plateNumber);

    /**
     * Delete the "id" appuser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
