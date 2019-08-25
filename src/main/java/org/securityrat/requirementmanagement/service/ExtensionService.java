package org.securityrat.requirementmanagement.service;

import org.securityrat.requirementmanagement.domain.Extension;
import org.securityrat.requirementmanagement.repository.ExtensionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Extension}.
 */
@Service
@Transactional
public class ExtensionService {

    private final Logger log = LoggerFactory.getLogger(ExtensionService.class);

    private final ExtensionRepository extensionRepository;

    public ExtensionService(ExtensionRepository extensionRepository) {
        this.extensionRepository = extensionRepository;
    }

    /**
     * Save a extension.
     *
     * @param extension the entity to save.
     * @return the persisted entity.
     */
    public Extension save(Extension extension) {
        log.debug("Request to save Extension : {}", extension);
        return extensionRepository.save(extension);
    }

    /**
     * Get all the extensions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Extension> findAll(Pageable pageable) {
        log.debug("Request to get all Extensions");
        return extensionRepository.findAll(pageable);
    }


    /**
     * Get one extension by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Extension> findOne(Long id) {
        log.debug("Request to get Extension : {}", id);
        return extensionRepository.findById(id);
    }

    /**
     * Delete the extension by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Extension : {}", id);
        extensionRepository.deleteById(id);
    }
}
