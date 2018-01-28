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

import org.securityrat.requirementmanagement.domain.Skeleton;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.SkeletonRepository;
import org.securityrat.requirementmanagement.service.dto.SkeletonCriteria;


/**
 * Service for executing complex queries for Skeleton entities in the database.
 * The main input is a {@link SkeletonCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Skeleton} or a {@link Page} of {@link Skeleton} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SkeletonQueryService extends QueryService<Skeleton> {

    private final Logger log = LoggerFactory.getLogger(SkeletonQueryService.class);


    private final SkeletonRepository skeletonRepository;

    public SkeletonQueryService(SkeletonRepository skeletonRepository) {
        this.skeletonRepository = skeletonRepository;
    }

    /**
     * Return a {@link List} of {@link Skeleton} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Skeleton> findByCriteria(SkeletonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Skeleton> specification = createSpecification(criteria);
        return skeletonRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Skeleton} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Skeleton> findByCriteria(SkeletonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Skeleton> specification = createSpecification(criteria);
        return skeletonRepository.findAll(specification, page);
    }

    /**
     * Function to convert SkeletonCriteria to a {@link Specifications}
     */
    private Specifications<Skeleton> createSpecification(SkeletonCriteria criteria) {
        Specifications<Skeleton> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Skeleton_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Skeleton_.name));
            }
            if (criteria.getShowOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShowOrder(), Skeleton_.showOrder));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Skeleton_.active));
            }
            if (criteria.getSkAtExId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSkAtExId(), Skeleton_.skAtExes, SkAtEx_.id));
            }
            if (criteria.getRequirementSetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRequirementSetId(), Skeleton_.requirementSet, RequirementSet_.id));
            }
        }
        return specification;
    }

}
