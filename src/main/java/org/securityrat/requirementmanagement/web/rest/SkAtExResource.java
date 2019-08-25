package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.securityrat.requirementmanagement.service.SkAtExService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.service.dto.SkAtExCriteria;
import org.securityrat.requirementmanagement.service.SkAtExQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.securityrat.requirementmanagement.domain.SkAtEx}.
 */
@RestController
@RequestMapping("/api")
public class SkAtExResource {

    private final Logger log = LoggerFactory.getLogger(SkAtExResource.class);

    private static final String ENTITY_NAME = "requirementManagementSkAtEx";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SkAtExService skAtExService;

    private final SkAtExQueryService skAtExQueryService;

    public SkAtExResource(SkAtExService skAtExService, SkAtExQueryService skAtExQueryService) {
        this.skAtExService = skAtExService;
        this.skAtExQueryService = skAtExQueryService;
    }

    /**
     * {@code POST  /sk-at-exes} : Create a new skAtEx.
     *
     * @param skAtEx the skAtEx to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skAtEx, or with status {@code 400 (Bad Request)} if the skAtEx has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sk-at-exes")
    public ResponseEntity<SkAtEx> createSkAtEx(@RequestBody SkAtEx skAtEx) throws URISyntaxException {
        log.debug("REST request to save SkAtEx : {}", skAtEx);
        if (skAtEx.getId() != null) {
            throw new BadRequestAlertException("A new skAtEx cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SkAtEx result = skAtExService.save(skAtEx);
        return ResponseEntity.created(new URI("/api/sk-at-exes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sk-at-exes} : Updates an existing skAtEx.
     *
     * @param skAtEx the skAtEx to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skAtEx,
     * or with status {@code 400 (Bad Request)} if the skAtEx is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skAtEx couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sk-at-exes")
    public ResponseEntity<SkAtEx> updateSkAtEx(@RequestBody SkAtEx skAtEx) throws URISyntaxException {
        log.debug("REST request to update SkAtEx : {}", skAtEx);
        if (skAtEx.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SkAtEx result = skAtExService.save(skAtEx);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, skAtEx.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sk-at-exes} : get all the skAtExes.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of skAtExes in body.
     */
    @GetMapping("/sk-at-exes")
    public ResponseEntity<List<SkAtEx>> getAllSkAtExes(SkAtExCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SkAtExes by criteria: {}", criteria);
        Page<SkAtEx> page = skAtExQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /sk-at-exes/count} : count all the skAtExes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/sk-at-exes/count")
    public ResponseEntity<Long> countSkAtExes(SkAtExCriteria criteria) {
        log.debug("REST request to count SkAtExes by criteria: {}", criteria);
        return ResponseEntity.ok().body(skAtExQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sk-at-exes/:id} : get the "id" skAtEx.
     *
     * @param id the id of the skAtEx to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skAtEx, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sk-at-exes/{id}")
    public ResponseEntity<SkAtEx> getSkAtEx(@PathVariable Long id) {
        log.debug("REST request to get SkAtEx : {}", id);
        Optional<SkAtEx> skAtEx = skAtExService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skAtEx);
    }

    /**
     * {@code DELETE  /sk-at-exes/:id} : delete the "id" skAtEx.
     *
     * @param id the id of the skAtEx to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sk-at-exes/{id}")
    public ResponseEntity<Void> deleteSkAtEx(@PathVariable Long id) {
        log.debug("REST request to delete SkAtEx : {}", id);
        skAtExService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
