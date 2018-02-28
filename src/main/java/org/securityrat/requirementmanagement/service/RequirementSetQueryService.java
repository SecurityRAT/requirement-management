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

import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.RequirementSetRepository;
import org.securityrat.requirementmanagement.service.dto.RequirementSetCriteria;


/**
 * Service for executing complex queries for RequirementSet entities in the database.
 * The main input is a {@link RequirementSetCriteria} which get's converted to {@link Specifications},
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
     * Return a {@link List} of {@link RequirementSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RequirementSet> findByCriteria(RequirementSetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<RequirementSet> specification = createSpecification(criteria);
        return requirementSetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RequirementSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RequirementSet> findByCriteria(RequirementSetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<RequirementSet> specification = createSpecification(criteria);
        return requirementSetRepository.findAll(specification, page);
    }

    /**
     * Function to convert RequirementSetCriteria to a {@link Specifications}
     */
    private Specifications<RequirementSet> createSpecification(RequirementSetCriteria criteria) {
        Specifications<RequirementSet> specification = Specifications.where(null);
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
                specification = specification.and(buildReferringEntitySpecification(criteria.getAttributeKeyId(), RequirementSet_.attributeKeys, AttributeKey_.id));
            }
            if (criteria.getSkeletonId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSkeletonId(), RequirementSet_.skeletons, Skeleton_.id));
            }
            if (criteria.getExtensionKeyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExtensionKeyId(), RequirementSet_.extensionKeys, ExtensionKey_.id));
            }
        }
        return specification;
    }

}
