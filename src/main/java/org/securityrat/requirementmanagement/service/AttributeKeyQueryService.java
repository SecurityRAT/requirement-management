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

import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.AttributeKeyRepository;
import org.securityrat.requirementmanagement.service.dto.AttributeKeyCriteria;

import org.securityrat.requirementmanagement.domain.enumeration.AttributeType;

/**
 * Service for executing complex queries for AttributeKey entities in the database.
 * The main input is a {@link AttributeKeyCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AttributeKey} or a {@link Page} of {@link AttributeKey} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttributeKeyQueryService extends QueryService<AttributeKey> {

    private final Logger log = LoggerFactory.getLogger(AttributeKeyQueryService.class);


    private final AttributeKeyRepository attributeKeyRepository;

    public AttributeKeyQueryService(AttributeKeyRepository attributeKeyRepository) {
        this.attributeKeyRepository = attributeKeyRepository;
    }

    /**
     * Return a {@link List} of {@link AttributeKey} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AttributeKey> findByCriteria(AttributeKeyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<AttributeKey> specification = createSpecification(criteria);
        return attributeKeyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AttributeKey} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttributeKey> findByCriteria(AttributeKeyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<AttributeKey> specification = createSpecification(criteria);
        return attributeKeyRepository.findAll(specification, page);
    }

    /**
     * Function to convert AttributeKeyCriteria to a {@link Specifications}
     */
    private Specifications<AttributeKey> createSpecification(AttributeKeyCriteria criteria) {
        Specifications<AttributeKey> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AttributeKey_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AttributeKey_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), AttributeKey_.type));
            }
            if (criteria.getShowOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShowOrder(), AttributeKey_.showOrder));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), AttributeKey_.active));
            }
            if (criteria.getAttributeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAttributeId(), AttributeKey_.attributes, Attribute_.id));
            }
            if (criteria.getRequirementSetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRequirementSetId(), AttributeKey_.requirementSet, RequirementSet_.id));
            }
        }
        return specification;
    }

}
