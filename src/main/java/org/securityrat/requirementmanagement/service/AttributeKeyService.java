package org.securityrat.requirementmanagement.service;

import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.securityrat.requirementmanagement.repository.AttributeKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing AttributeKey.
 */
@Service
@Transactional
public class AttributeKeyService {

    private final Logger log = LoggerFactory.getLogger(AttributeKeyService.class);

    private final AttributeKeyRepository attributeKeyRepository;

    public AttributeKeyService(AttributeKeyRepository attributeKeyRepository) {
        this.attributeKeyRepository = attributeKeyRepository;
    }

    /**
     * Save a attributeKey.
     *
     * @param attributeKey the entity to save
     * @return the persisted entity
     */
    public AttributeKey save(AttributeKey attributeKey) {
        log.debug("Request to save AttributeKey : {}", attributeKey);
        return attributeKeyRepository.save(attributeKey);
    }

    /**
     * Get all the attributeKeys.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AttributeKey> findAll(Pageable pageable) {
        log.debug("Request to get all AttributeKeys");
        return attributeKeyRepository.findAll(pageable);
    }

    /**
     * Get one attributeKey by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AttributeKey findOne(Long id) {
        log.debug("Request to get AttributeKey : {}", id);
        return attributeKeyRepository.findOne(id);
    }

    /**
     * Delete the attributeKey by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AttributeKey : {}", id);
        attributeKeyRepository.delete(id);
    }
}
