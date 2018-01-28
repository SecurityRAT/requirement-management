package org.securityrat.requirementmanagement.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.SkAtExRepository;
import org.securityrat.requirementmanagement.service.dto.SkAtExCriteria;


/**
 * Service for executing complex queries for SkAtEx entities in the database.
 * The main input is a {@link SkAtExCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SkAtEx} or a {@link Page} of {@link SkAtEx} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SkAtExQueryService extends QueryService<SkAtEx> {

    private final Logger log = LoggerFactory.getLogger(SkAtExQueryService.class);


    private final SkAtExRepository skAtExRepository;

    public SkAtExQueryService(SkAtExRepository skAtExRepository) {
        this.skAtExRepository = skAtExRepository;
    }

    /**
     * Return a {@link List} of {@link SkAtEx} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SkAtEx> findByCriteria(SkAtExCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SkAtEx> specification = createSpecification(criteria);
        return skAtExRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SkAtEx} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SkAtEx> findByCriteria(SkAtExCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SkAtEx> specification = createSpecification(criteria);
        return skAtExRepository.findAll(specification, page);
    }

    /**
     * Function to convert SkAtExCriteria to a {@link Specifications}
     */
    private Specifications<SkAtEx> createSpecification(SkAtExCriteria criteria) {
        Specifications<SkAtEx> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SkAtEx_.id));
            }
            if (criteria.getSkeletonId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSkeletonId(), SkAtEx_.skeleton, Skeleton_.id));
            }
            if (criteria.getAttributeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAttributeId(), SkAtEx_.attribute, Attribute_.id));
            }
            if (criteria.getExtensionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExtensionId(), SkAtEx_.extension, Extension_.id));
            }
        }
        return specification;
    }

}
