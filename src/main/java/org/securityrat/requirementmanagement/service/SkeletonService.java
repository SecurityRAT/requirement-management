package org.securityrat.requirementmanagement.service;

import org.securityrat.requirementmanagement.domain.Skeleton;
import org.securityrat.requirementmanagement.repository.SkeletonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Skeleton.
 */
@Service
@Transactional
public class SkeletonService {

    private final Logger log = LoggerFactory.getLogger(SkeletonService.class);

    private final SkeletonRepository skeletonRepository;

    public SkeletonService(SkeletonRepository skeletonRepository) {
        this.skeletonRepository = skeletonRepository;
    }

    /**
     * Save a skeleton.
     *
     * @param skeleton the entity to save
     * @return the persisted entity
     */
    public Skeleton save(Skeleton skeleton) {
        log.debug("Request to save Skeleton : {}", skeleton);
        return skeletonRepository.save(skeleton);
    }

    /**
     * Get all the skeletons.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Skeleton> findAll(Pageable pageable) {
        log.debug("Request to get all Skeletons");
        return skeletonRepository.findAll(pageable);
    }

    /**
     * Get one skeleton by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Skeleton findOne(Long id) {
        log.debug("Request to get Skeleton : {}", id);
        return skeletonRepository.findOne(id);
    }

    /**
     * Delete the skeleton by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Skeleton : {}", id);
        skeletonRepository.delete(id);
    }
}
