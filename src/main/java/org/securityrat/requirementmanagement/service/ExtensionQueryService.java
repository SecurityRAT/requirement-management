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

import org.securityrat.requirementmanagement.domain.Extension;
import org.securityrat.requirementmanagement.domain.*; // for static metamodels
import org.securityrat.requirementmanagement.repository.ExtensionRepository;
import org.securityrat.requirementmanagement.service.dto.ExtensionCriteria;

/**
 * Service for executing complex queries for {@link Extension} entities in the database.
 * The main input is a {@link ExtensionCriteria} which gets converted to {@link Specification},
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
     * Return a {@link List} of {@link Extension} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Extension> findByCriteria(ExtensionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Extension> specification = createSpecification(criteria);
        return extensionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Extension} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Extension> findByCriteria(ExtensionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Extension> specification = createSpecification(criteria);
        return extensionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExtensionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Extension> specification = createSpecification(criteria);
        return extensionRepository.count(specification);
    }

    /**
     * Function to convert {@link ExtensionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Extension> createSpecification(ExtensionCriteria criteria) {
        Specification<Extension> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Extension_.id));
            }
            if (criteria.getShowOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShowOrder(), Extension_.showOrder));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Extension_.active));
            }
            if (criteria.getSkAtExId() != null) {
                specification = specification.and(buildSpecification(criteria.getSkAtExId(),
                    root -> root.join(Extension_.skAtExes, JoinType.LEFT).get(SkAtEx_.id)));
            }
            if (criteria.getExtensionKeyId() != null) {
                specification = specification.and(buildSpecification(criteria.getExtensionKeyId(),
                    root -> root.join(Extension_.extensionKey, JoinType.LEFT).get(ExtensionKey_.id)));
            }
        }
        return specification;
    }
}
