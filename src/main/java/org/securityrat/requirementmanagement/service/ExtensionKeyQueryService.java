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

import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.ExtensionKeyRepository;
import org.securityrat.requirementmanagement.service.dto.ExtensionKeyCriteria;

import org.securityrat.requirementmanagement.domain.enumeration.ExtensionSection;
import org.securityrat.requirementmanagement.domain.enumeration.ExtensionType;

/**
 * Service for executing complex queries for ExtensionKey entities in the database.
 * The main input is a {@link ExtensionKeyCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExtensionKey} or a {@link Page} of {@link ExtensionKey} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExtensionKeyQueryService extends QueryService<ExtensionKey> {

    private final Logger log = LoggerFactory.getLogger(ExtensionKeyQueryService.class);


    private final ExtensionKeyRepository extensionKeyRepository;

    public ExtensionKeyQueryService(ExtensionKeyRepository extensionKeyRepository) {
        this.extensionKeyRepository = extensionKeyRepository;
    }

    /**
     * Return a {@link List} of {@link ExtensionKey} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExtensionKey> findByCriteria(ExtensionKeyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ExtensionKey> specification = createSpecification(criteria);
        return extensionKeyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExtensionKey} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExtensionKey> findByCriteria(ExtensionKeyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ExtensionKey> specification = createSpecification(criteria);
        return extensionKeyRepository.findAll(specification, page);
    }

    /**
     * Function to convert ExtensionKeyCriteria to a {@link Specifications}
     */
    private Specifications<ExtensionKey> createSpecification(ExtensionKeyCriteria criteria) {
        Specifications<ExtensionKey> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ExtensionKey_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ExtensionKey_.name));
            }
            if (criteria.getSection() != null) {
                specification = specification.and(buildSpecification(criteria.getSection(), ExtensionKey_.section));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), ExtensionKey_.type));
            }
            if (criteria.getShowOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShowOrder(), ExtensionKey_.showOrder));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), ExtensionKey_.active));
            }
            if (criteria.getExtensionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExtensionId(), ExtensionKey_.extensions, Extension_.id));
            }
            if (criteria.getRequirementSetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRequirementSetId(), ExtensionKey_.requirementSet, RequirementSet_.id));
            }
        }
        return specification;
    }

}
