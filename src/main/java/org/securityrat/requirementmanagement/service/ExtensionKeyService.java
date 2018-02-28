package org.securityrat.requirementmanagement.service;

import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.securityrat.requirementmanagement.repository.ExtensionKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ExtensionKey.
 */
@Service
@Transactional
public class ExtensionKeyService {

    private final Logger log = LoggerFactory.getLogger(ExtensionKeyService.class);

    private final ExtensionKeyRepository extensionKeyRepository;

    public ExtensionKeyService(ExtensionKeyRepository extensionKeyRepository) {
        this.extensionKeyRepository = extensionKeyRepository;
    }

    /**
     * Save a extensionKey.
     *
     * @param extensionKey the entity to save
     * @return the persisted entity
     */
    public ExtensionKey save(ExtensionKey extensionKey) {
        log.debug("Request to save ExtensionKey : {}", extensionKey);
        return extensionKeyRepository.save(extensionKey);
    }

    /**
     * Get all the extensionKeys.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ExtensionKey> findAll(Pageable pageable) {
        log.debug("Request to get all ExtensionKeys");
        return extensionKeyRepository.findAll(pageable);
    }

    /**
     * Get one extensionKey by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ExtensionKey findOne(Long id) {
        log.debug("Request to get ExtensionKey : {}", id);
        return extensionKeyRepository.findOne(id);
    }

    /**
     * Delete the extensionKey by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExtensionKey : {}", id);
        extensionKeyRepository.delete(id);
    }
}
