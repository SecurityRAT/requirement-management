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

import org.securityrat.requirementmanagement.domain.Attribute;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.AttributeRepository;
import org.securityrat.requirementmanagement.service.dto.AttributeCriteria;


/**
 * Service for executing complex queries for Attribute entities in the database.
 * The main input is a {@link AttributeCriteria} which get's converted to {@link Specifications},
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
     * Return a {@link List} of {@link Attribute} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Attribute> findByCriteria(AttributeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Attribute> specification = createSpecification(criteria);
        return attributeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Attribute} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Attribute> findByCriteria(AttributeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Attribute> specification = createSpecification(criteria);
        return attributeRepository.findAll(specification, page);
    }

    /**
     * Function to convert AttributeCriteria to a {@link Specifications}
     */
    private Specifications<Attribute> createSpecification(AttributeCriteria criteria) {
        Specifications<Attribute> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Attribute_.id));
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
                specification = specification.and(buildReferringEntitySpecification(criteria.getSkAtExId(), Attribute_.skAtExes, SkAtEx_.id));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getParentId(), Attribute_.parent, Attribute_.id));
            }
            if (criteria.getAttributeKeyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAttributeKeyId(), Attribute_.attributeKey, AttributeKey_.id));
            }
        }
        return specification;
    }

}
