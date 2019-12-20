package org.securityrat.requirementmanagement.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.securityrat.requirementmanagement.domain.Skeleton;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.SkeletonRepository;
import org.securityrat.requirementmanagement.service.dto.SkeletonCriteria;

/**
 * Service for executing complex queries for {@link Skeleton} entities in the database.
 * The main input is a {@link SkeletonCriteria} which gets converted to {@link Specification},
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
     * Return a {@link List} of {@link Skeleton} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Skeleton> findByCriteria(SkeletonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Skeleton> specification = createSpecification(criteria);
        return skeletonRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Skeleton} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Skeleton> findByCriteria(SkeletonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Skeleton> specification = createSpecification(criteria);
        return skeletonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SkeletonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Skeleton> specification = createSpecification(criteria);
        return skeletonRepository.count(specification);
    }

    /**
     * Function to convert {@link SkeletonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Skeleton> createSpecification(SkeletonCriteria criteria) {
        Specification<Skeleton> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Skeleton_.id));
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
                specification = specification.and(buildSpecification(criteria.getSkAtExId(),
                    root -> root.join(Skeleton_.skAtExes, JoinType.LEFT).get(SkAtEx_.id)));
            }
            if (criteria.getRequirementSetId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequirementSetId(),
                    root -> root.join(Skeleton_.requirementSet, JoinType.LEFT).get(RequirementSet_.id)));
            }
        }
        return specification;
    }
}
