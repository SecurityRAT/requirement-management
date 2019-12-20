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

import org.securityrat.requirementmanagement.domain.Attribute;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.AttributeRepository;
import org.securityrat.requirementmanagement.service.dto.AttributeCriteria;

/**
 * Service for executing complex queries for {@link Attribute} entities in the database.
 * The main input is a {@link AttributeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Attribute} or a {@link Page} of {@link Attribute} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttributeQueryService extends QueryService<Attribute> {

    private final Logger log = LoggerFactory.getLogger(AttributeQueryService.class);

    private final AttributeRepository attributeRepository;

    public AttributeQueryService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    /**
     * Return a {@link List} of {@link Attribute} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Attribute> findByCriteria(AttributeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Attribute} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Attribute> findByCriteria(AttributeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttributeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.count(specification);
    }

    /**
     * Function to convert {@link AttributeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Attribute> createSpecification(AttributeCriteria criteria) {
        Specification<Attribute> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Attribute_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Attribute_.name));
            }
            if (criteria.getShowOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShowOrder(), Attribute_.showOrder));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Attribute_.active));
            }
            if (criteria.getSkAtExId() != null) {
                specification = specification.and(buildSpecification(criteria.getSkAtExId(),
                    root -> root.join(Attribute_.skAtExes, JoinType.LEFT).get(SkAtEx_.id)));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildSpecification(criteria.getParentId(),
                    root -> root.join(Attribute_.parent, JoinType.LEFT).get(Attribute_.id)));
            }
            if (criteria.getAttributeKeyId() != null) {
                specification = specification.and(buildSpecification(criteria.getAttributeKeyId(),
                    root -> root.join(Attribute_.attributeKey, JoinType.LEFT).get(AttributeKey_.id)));
            }
        }
        return specification;
    }
}
