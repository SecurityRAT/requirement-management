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

import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.AttributeKeyRepository;
import org.securityrat.requirementmanagement.service.dto.AttributeKeyCriteria;

/**
 * Service for executing complex queries for {@link AttributeKey} entities in the database.
 * The main input is a {@link AttributeKeyCriteria} which gets converted to {@link Specification},
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
     * Return a {@link List} of {@link AttributeKey} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AttributeKey> findByCriteria(AttributeKeyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AttributeKey> specification = createSpecification(criteria);
        return attributeKeyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AttributeKey} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttributeKey> findByCriteria(AttributeKeyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AttributeKey> specification = createSpecification(criteria);
        return attributeKeyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttributeKeyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AttributeKey> specification = createSpecification(criteria);
        return attributeKeyRepository.count(specification);
    }

    /**
     * Function to convert {@link AttributeKeyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AttributeKey> createSpecification(AttributeKeyCriteria criteria) {
        Specification<AttributeKey> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AttributeKey_.id));
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
                specification = specification.and(buildSpecification(criteria.getAttributeId(),
                    root -> root.join(AttributeKey_.attributes, JoinType.LEFT).get(Attribute_.id)));
            }
            if (criteria.getRequirementSetId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequirementSetId(),
                    root -> root.join(AttributeKey_.requirementSet, JoinType.LEFT).get(RequirementSet_.id)));
            }
        }
        return specification;
    }
}
