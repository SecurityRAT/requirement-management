package org.securityrat.requirementmanagement.service;

import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.securityrat.requirementmanagement.repository.RequirementSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing RequirementSet.
 */
@Service
@Transactional
public class RequirementSetService {

    private final Logger log = LoggerFactory.getLogger(RequirementSetService.class);

    private final RequirementSetRepository requirementSetRepository;

    public RequirementSetService(RequirementSetRepository requirementSetRepository) {
        this.requirementSetRepository = requirementSetRepository;
    }

    /**
     * Save a requirementSet.
     *
     * @param requirementSet the entity to save
     * @return the persisted entity
     */
    public RequirementSet save(RequirementSet requirementSet) {
        log.debug("Request to save RequirementSet : {}", requirementSet);
        return requirementSetRepository.save(requirementSet);
    }

    /**
     * Get all the requirementSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RequirementSet> findAll(Pageable pageable) {
        log.debug("Request to get all RequirementSets");
        return requirementSetRepository.findAll(pageable);
    }

    /**
     * Get one requirementSet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RequirementSet findOne(Long id) {
        log.debug("Request to get RequirementSet : {}", id);
        return requirementSetRepository.findOne(id);
    }

    /**
     * Delete the requirementSet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RequirementSet : {}", id);
        requirementSetRepository.delete(id);
    }
}
