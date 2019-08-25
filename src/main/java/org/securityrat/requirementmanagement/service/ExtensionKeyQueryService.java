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

import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.ExtensionKeyRepository;
import org.securityrat.requirementmanagement.service.dto.ExtensionKeyCriteria;

/**
 * Service for executing complex queries for {@link ExtensionKey} entities in the database.
 * The main input is a {@link ExtensionKeyCriteria} which gets converted to {@link Specification},
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
     * Return a {@link List} of {@link ExtensionKey} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExtensionKey> findByCriteria(ExtensionKeyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExtensionKey> specification = createSpecification(criteria);
        return extensionKeyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExtensionKey} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExtensionKey> findByCriteria(ExtensionKeyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExtensionKey> specification = createSpecification(criteria);
        return extensionKeyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExtensionKeyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExtensionKey> specification = createSpecification(criteria);
        return extensionKeyRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<ExtensionKey> createSpecification(ExtensionKeyCriteria criteria) {
        Specification<ExtensionKey> specification = Specification.where(null);
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
                specification = specification.and(buildSpecification(criteria.getExtensionId(),
                    root -> root.join(ExtensionKey_.extensions, JoinType.LEFT).get(Extension_.id)));
            }
            if (criteria.getRequirementSetId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequirementSetId(),
                    root -> root.join(ExtensionKey_.requirementSet, JoinType.LEFT).get(RequirementSet_.id)));
            }
        }
        return specification;
    }
}
