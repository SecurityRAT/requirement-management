package org.securityrat.requirementmanagement.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.securityrat.requirementmanagement.service.RequirementSetService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.web.rest.util.HeaderUtil;
import org.securityrat.requirementmanagement.web.rest.util.PaginationUtil;
import org.securityrat.requirementmanagement.service.dto.RequirementSetCriteria;
import org.securityrat.requirementmanagement.service.RequirementSetQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RequirementSet.
 */
@RestController
@RequestMapping("/api")
public class RequirementSetResource {

    private final Logger log = LoggerFactory.getLogger(RequirementSetResource.class);

    private static final String ENTITY_NAME = "requirementSet";

    private final RequirementSetService requirementSetService;

    private final RequirementSetQueryService requirementSetQueryService;

    public RequirementSetResource(RequirementSetService requirementSetService, RequirementSetQueryService requirementSetQueryService) {
        this.requirementSetService = requirementSetService;
        this.requirementSetQueryService = requirementSetQueryService;
    }

    /**
     * POST  /requirement-sets : Create a new requirementSet.
     *
     * @param requirementSet the requirementSet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new requirementSet, or with status 400 (Bad Request) if the requirementSet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/requirement-sets")
    @Timed
    public ResponseEntity<RequirementSet> createRequirementSet(@Valid @RequestBody RequirementSet requirementSet) throws URISyntaxException {
        log.debug("REST request to save RequirementSet : {}", requirementSet);
        if (requirementSet.getId() != null) {
            throw new BadRequestAlertException("A new requirementSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequirementSet result = requirementSetService.save(requirementSet);
        return ResponseEntity.created(new URI("/api/requirement-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /requirement-sets : Updates an existing requirementSet.
     *
     * @param requirementSet the requirementSet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated requirementSet,
     * or with status 400 (Bad Request) if the requirementSet is not valid,
     * or with status 500 (Internal Server Error) if the requirementSet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/requirement-sets")
    @Timed
    public ResponseEntity<RequirementSet> updateRequirementSet(@Valid @RequestBody RequirementSet requirementSet) throws URISyntaxException {
        log.debug("REST request to update RequirementSet : {}", requirementSet);
        if (requirementSet.getId() == null) {
            return createRequirementSet(requirementSet);
        }
        RequirementSet result = requirementSetService.save(requirementSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, requirementSet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /requirement-sets : get all the requirementSets.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of requirementSets in body
     */
    @GetMapping("/requirement-sets")
    @Timed
    public ResponseEntity<List<RequirementSet>> getAllRequirementSets(RequirementSetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RequirementSets by criteria: {}", criteria);
        Page<RequirementSet> page = requirementSetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/requirement-sets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /requirement-sets/:id : get the "id" requirementSet.
     *
     * @param id the id of the requirementSet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the requirementSet, or with status 404 (Not Found)
     */
    @GetMapping("/requirement-sets/{id}")
    @Timed
    public ResponseEntity<RequirementSet> getRequirementSet(@PathVariable Long id) {
        log.debug("REST request to get RequirementSet : {}", id);
        RequirementSet requirementSet = requirementSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(requirementSet));
    }

    /**
     * DELETE  /requirement-sets/:id : delete the "id" requirementSet.
     *
     * @param id the id of the requirementSet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/requirement-sets/{id}")
    @Timed
    public ResponseEntity<Void> deleteRequirementSet(@PathVariable Long id) {
        log.debug("REST request to delete RequirementSet : {}", id);
        requirementSetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
