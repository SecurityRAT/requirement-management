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

import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.RequirementSetRepository;
import org.securityrat.requirementmanagement.service.dto.RequirementSetCriteria;

/**
 * Service for executing complex queries for {@link RequirementSet} entities in the database.
 * The main input is a {@link RequirementSetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RequirementSet} or a {@link Page} of {@link RequirementSet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RequirementSetQueryService extends QueryService<RequirementSet> {

    private final Logger log = LoggerFactory.getLogger(RequirementSetQueryService.class);

    private final RequirementSetRepository requirementSetRepository;

    public RequirementSetQueryService(RequirementSetRepository requirementSetRepository) {
        this.requirementSetRepository = requirementSetRepository;
    }

    /**
     * Return a {@link List} of {@link RequirementSet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RequirementSet> findByCriteria(RequirementSetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RequirementSet> specification = createSpecification(criteria);
        return requirementSetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RequirementSet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RequirementSet> findByCriteria(RequirementSetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RequirementSet> specification = createSpecification(criteria);
        return requirementSetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RequirementSetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RequirementSet> specification = createSpecification(criteria);
        return requirementSetRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<RequirementSet> createSpecification(RequirementSetCriteria criteria) {
        Specification<RequirementSet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RequirementSet_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RequirementSet_.name));
            }
            if (criteria.getShowOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShowOrder(), RequirementSet_.showOrder));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), RequirementSet_.active));
            }
            if (criteria.getAttributeKeyId() != null) {
                specification = specification.and(buildSpecification(criteria.getAttributeKeyId(),
                    root -> root.join(RequirementSet_.attributeKeys, JoinType.LEFT).get(AttributeKey_.id)));
            }
            if (criteria.getSkeletonId() != null) {
                specification = specification.and(buildSpecification(criteria.getSkeletonId(),
                    root -> root.join(RequirementSet_.skeletons, JoinType.LEFT).get(Skeleton_.id)));
            }
            if (criteria.getExtensionKeyId() != null) {
                specification = specification.and(buildSpecification(criteria.getExtensionKeyId(),
                    root -> root.join(RequirementSet_.extensionKeys, JoinType.LEFT).get(ExtensionKey_.id)));
            }
        }
        return specification;
    }
}
