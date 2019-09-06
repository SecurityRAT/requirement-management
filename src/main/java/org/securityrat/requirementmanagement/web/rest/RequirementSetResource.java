package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.securityrat.requirementmanagement.service.RequirementSetService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.service.dto.RequirementSetCriteria;
import org.securityrat.requirementmanagement.service.RequirementSetQueryService;

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
 * REST controller for managing {@link org.securityrat.requirementmanagement.domain.RequirementSet}.
 */
@RestController
@RequestMapping("/api")
public class RequirementSetResource {

    private final Logger log = LoggerFactory.getLogger(RequirementSetResource.class);

    private static final String ENTITY_NAME = "requirementManagementRequirementSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequirementSetService requirementSetService;

    private final RequirementSetQueryService requirementSetQueryService;

    public RequirementSetResource(RequirementSetService requirementSetService, RequirementSetQueryService requirementSetQueryService) {
        this.requirementSetService = requirementSetService;
        this.requirementSetQueryService = requirementSetQueryService;
    }

    /**
     * {@code POST  /requirement-sets} : Create a new requirementSet.
     *
     * @param requirementSet the requirementSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requirementSet, or with status {@code 400 (Bad Request)} if the requirementSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requirement-sets")
    public ResponseEntity<RequirementSet> createRequirementSet(@Valid @RequestBody RequirementSet requirementSet) throws URISyntaxException {
        log.debug("REST request to save RequirementSet : {}", requirementSet);
        if (requirementSet.getId() != null) {
            throw new BadRequestAlertException("A new requirementSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequirementSet result = requirementSetService.save(requirementSet);
        return ResponseEntity.created(new URI("/api/requirement-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /requirement-sets} : Updates an existing requirementSet.
     *
     * @param requirementSet the requirementSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requirementSet,
     * or with status {@code 400 (Bad Request)} if the requirementSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requirementSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requirement-sets")
    public ResponseEntity<RequirementSet> updateRequirementSet(@Valid @RequestBody RequirementSet requirementSet) throws URISyntaxException {
        log.debug("REST request to update RequirementSet : {}", requirementSet);
        if (requirementSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequirementSet result = requirementSetService.save(requirementSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requirementSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /requirement-sets} : get all the requirementSets.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requirementSets in body.
     */
    @GetMapping("/requirement-sets")
    public ResponseEntity<List<RequirementSet>> getAllRequirementSets(RequirementSetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RequirementSets by criteria: {}", criteria);
        Page<RequirementSet> page = requirementSetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /requirement-sets/count} : count all the requirementSets.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/requirement-sets/count")
    public ResponseEntity<Long> countRequirementSets(RequirementSetCriteria criteria) {
        log.debug("REST request to count RequirementSets by criteria: {}", criteria);
        return ResponseEntity.ok().body(requirementSetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /requirement-sets/:id} : get the "id" requirementSet.
     *
     * @param id the id of the requirementSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requirementSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requirement-sets/{id}")
    public ResponseEntity<RequirementSet> getRequirementSet(@PathVariable Long id) {
        log.debug("REST request to get RequirementSet : {}", id);
        Optional<RequirementSet> requirementSet = requirementSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requirementSet);
    }

    /**
     * {@code DELETE  /requirement-sets/:id} : delete the "id" requirementSet.
     *
     * @param id the id of the requirementSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requirement-sets/{id}")
    public ResponseEntity<Void> deleteRequirementSet(@PathVariable Long id) {
        log.debug("REST request to delete RequirementSet : {}", id);
        requirementSetService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
