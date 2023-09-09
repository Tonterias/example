package com.example.web.service;

import com.example.web.domain.*; // for static metamodels
import com.example.web.domain.Appuser;
import com.example.web.repository.AppuserRepository;
import com.example.web.service.criteria.AppuserCriteria;
import com.example.web.service.dto.AppuserDTO;
import com.example.web.service.mapper.AppuserMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Appuser} entities in the database.
 * The main input is a {@link AppuserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppuserDTO} or a {@link Page} of {@link AppuserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppuserQueryService extends QueryService<Appuser> {

    private final Logger log = LoggerFactory.getLogger(AppuserQueryService.class);

    private final AppuserRepository appuserRepository;

    private final AppuserMapper appuserMapper;

    public AppuserQueryService(AppuserRepository appuserRepository, AppuserMapper appuserMapper) {
        this.appuserRepository = appuserRepository;
        this.appuserMapper = appuserMapper;
    }

    /**
     * Return a {@link List} of {@link AppuserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppuserDTO> findByCriteria(AppuserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Appuser> specification = createSpecification(criteria);
        return appuserMapper.toDto(appuserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppuserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppuserDTO> findByCriteria(AppuserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Appuser> specification = createSpecification(criteria);
        return appuserRepository.findAll(specification, page).map(appuserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppuserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Appuser> specification = createSpecification(criteria);
        return appuserRepository.count(specification);
    }

    /**
     * Function to convert {@link AppuserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Appuser> createSpecification(AppuserCriteria criteria) {
        Specification<Appuser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Appuser_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Appuser_.date));
            }
            if (criteria.getPlateNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlateNumber(), Appuser_.plateNumber));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Appuser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Appuser_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Appuser_.email));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Appuser_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getNotificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotificationId(),
                            root -> root.join(Appuser_.notifications, JoinType.LEFT).get(Notification_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
