package org.securityrat.requirementmanagement.service;

import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.securityrat.requirementmanagement.repository.SkAtExRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing SkAtEx.
 */
@Service
@Transactional
public class SkAtExService {

    private final Logger log = LoggerFactory.getLogger(SkAtExService.class);

    private final SkAtExRepository skAtExRepository;

    public SkAtExService(SkAtExRepository skAtExRepository) {
        this.skAtExRepository = skAtExRepository;
    }

    /**
     * Save a skAtEx.
     *
     * @param skAtEx the entity to save
     * @return the persisted entity
     */
    public SkAtEx save(SkAtEx skAtEx) {
        log.debug("Request to save SkAtEx : {}", skAtEx);
        return skAtExRepository.save(skAtEx);
    }

    /**
     * Get all the skAtExes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SkAtEx> findAll(Pageable pageable) {
        log.debug("Request to get all SkAtExes");
        return skAtExRepository.findAll(pageable);
    }

    /**
     * Get one skAtEx by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SkAtEx findOne(Long id) {
        log.debug("Request to get SkAtEx : {}", id);
        return skAtExRepository.findOne(id);
    }

    /**
     * Delete the skAtEx by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SkAtEx : {}", id);
        skAtExRepository.delete(id);
    }
}
