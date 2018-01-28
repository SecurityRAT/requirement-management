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

import org.securityrat.requirementmanagement.domain.Extension;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.ExtensionRepository;
import org.securityrat.requirementmanagement.service.dto.ExtensionCriteria;


/**
 * Service for executing complex queries for Extension entities in the database.
 * The main input is a {@link ExtensionCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Extension} or a {@link Page} of {@link Extension} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExtensionQueryService extends QueryService<Extension> {

    private final Logger log = LoggerFactory.getLogger(ExtensionQueryService.class);


    private final ExtensionRepository extensionRepository;

    public ExtensionQueryService(ExtensionRepository extensionRepository) {
        this.extensionRepository = extensionRepository;
    }

    /**
     * Return a {@link List} of {@link Extension} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Extension> findByCriteria(ExtensionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Extension> specification = createSpecification(criteria);
        return extensionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Extension} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Extension> findByCriteria(ExtensionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Extension> specification = createSpecification(criteria);
        return extensionRepository.findAll(specification, page);
    }

    /**
     * Function to convert ExtensionCriteria to a {@link Specifications}
     */
    private Specifications<Extension> createSpecification(ExtensionCriteria criteria) {
        Specifications<Extension> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Extension_.id));
            }
            if (criteria.getShowOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShowOrder(), Extension_.showOrder));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Extension_.active));
            }
            if (criteria.getSkAtExId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSkAtExId(), Extension_.skAtExes, SkAtEx_.id));
            }
            if (criteria.getExtensionKeyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExtensionKeyId(), Extension_.extensionKey, ExtensionKey_.id));
            }
        }
        return specification;
    }

}
