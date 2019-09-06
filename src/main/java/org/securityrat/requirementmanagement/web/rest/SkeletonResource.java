package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.domain.Skeleton;
import org.securityrat.requirementmanagement.service.SkeletonService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.service.dto.SkeletonCriteria;
import org.securityrat.requirementmanagement.service.SkeletonQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.securityrat.requirementmanagement.domain.Skeleton}.
 */
@RestController
@RequestMapping("/api")
public class SkeletonResource {

    private final Logger log = LoggerFactory.getLogger(SkeletonResource.class);

    private static final String ENTITY_NAME = "requirementManagementSkeleton";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SkeletonService skeletonService;

    private final SkeletonQueryService skeletonQueryService;

    public SkeletonResource(SkeletonService skeletonService, SkeletonQueryService skeletonQueryService) {
        this.skeletonService = skeletonService;
        this.skeletonQueryService = skeletonQueryService;
    }

    /**
     * {@code POST  /skeletons} : Create a new skeleton.
     *
     * @param skeleton the skeleton to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skeleton, or with status {@code 400 (Bad Request)} if the skeleton has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/skeletons")
    public ResponseEntity<Skeleton> createSkeleton(@Valid @RequestBody Skeleton skeleton) throws URISyntaxException {
        log.debug("REST request to save Skeleton : {}", skeleton);
        if (skeleton.getId() != null) {
            throw new BadRequestAlertException("A new skeleton cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Skeleton result = skeletonService.save(skeleton);
        return ResponseEntity.created(new URI("/api/skeletons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /skeletons} : Updates an existing skeleton.
     *
     * @param skeleton the skeleton to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skeleton,
     * or with status {@code 400 (Bad Request)} if the skeleton is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skeleton couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/skeletons")
    public ResponseEntity<Skeleton> updateSkeleton(@Valid @RequestBody Skeleton skeleton) throws URISyntaxException {
        log.debug("REST request to update Skeleton : {}", skeleton);
        if (skeleton.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Skeleton result = skeletonService.save(skeleton);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skeleton.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /skeletons} : get all the skeletons.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of skeletons in body.
     */
    @GetMapping("/skeletons")
    public ResponseEntity<List<Skeleton>> getAllSkeletons(SkeletonCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Skeletons by criteria: {}", criteria);
        Page<Skeleton> page = skeletonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /skeletons/count} : count all the skeletons.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/skeletons/count")
    public ResponseEntity<Long> countSkeletons(SkeletonCriteria criteria) {
        log.debug("REST request to count Skeletons by criteria: {}", criteria);
        return ResponseEntity.ok().body(skeletonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /skeletons/:id} : get the "id" skeleton.
     *
     * @param id the id of the skeleton to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skeleton, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/skeletons/{id}")
    public ResponseEntity<Skeleton> getSkeleton(@PathVariable Long id) {
        log.debug("REST request to get Skeleton : {}", id);
        Optional<Skeleton> skeleton = skeletonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skeleton);
    }

    /**
     * {@code DELETE  /skeletons/:id} : delete the "id" skeleton.
     *
     * @param id the id of the skeleton to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/skeletons/{id}")
    public ResponseEntity<Void> deleteSkeleton(@PathVariable Long id) {
        log.debug("REST request to delete Skeleton : {}", id);
        skeletonService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
