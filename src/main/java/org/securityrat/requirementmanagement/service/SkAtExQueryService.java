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

import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.SkAtExRepository;
import org.securityrat.requirementmanagement.service.dto.SkAtExCriteria;

/**
 * Service for executing complex queries for {@link SkAtEx} entities in the database.
 * The main input is a {@link SkAtExCriteria} which gets converted to {@link Specification},
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
     * Return a {@link List} of {@link SkAtEx} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SkAtEx> findByCriteria(SkAtExCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SkAtEx> specification = createSpecification(criteria);
        return skAtExRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SkAtEx} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SkAtEx> findByCriteria(SkAtExCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SkAtEx> specification = createSpecification(criteria);
        return skAtExRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SkAtExCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SkAtEx> specification = createSpecification(criteria);
        return skAtExRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<SkAtEx> createSpecification(SkAtExCriteria criteria) {
        Specification<SkAtEx> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SkAtEx_.id));
            }
            if (criteria.getSkeletonId() != null) {
                specification = specification.and(buildSpecification(criteria.getSkeletonId(),
                    root -> root.join(SkAtEx_.skeleton, JoinType.LEFT).get(Skeleton_.id)));
            }
            if (criteria.getAttributeId() != null) {
                specification = specification.and(buildSpecification(criteria.getAttributeId(),
                    root -> root.join(SkAtEx_.attribute, JoinType.LEFT).get(Attribute_.id)));
            }
            if (criteria.getExtensionId() != null) {
                specification = specification.and(buildSpecification(criteria.getExtensionId(),
                    root -> root.join(SkAtEx_.extension, JoinType.LEFT).get(Extension_.id)));
            }
        }
        return specification;
    }
}
