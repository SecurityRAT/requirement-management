package org.securityrat.requirementmanagement.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.securityrat.requirementmanagement.service.SkAtExService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.web.rest.util.HeaderUtil;
import org.securityrat.requirementmanagement.web.rest.util.PaginationUtil;
import org.securityrat.requirementmanagement.service.dto.SkAtExCriteria;
import org.securityrat.requirementmanagement.service.SkAtExQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SkAtEx.
 */
@RestController
@RequestMapping("/api")
public class SkAtExResource {

    private final Logger log = LoggerFactory.getLogger(SkAtExResource.class);

    private static final String ENTITY_NAME = "skAtEx";

    private final SkAtExService skAtExService;

    private final SkAtExQueryService skAtExQueryService;

    public SkAtExResource(SkAtExService skAtExService, SkAtExQueryService skAtExQueryService) {
        this.skAtExService = skAtExService;
        this.skAtExQueryService = skAtExQueryService;
    }

    /**
     * POST  /sk-at-exes : Create a new skAtEx.
     *
     * @param skAtEx the skAtEx to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skAtEx, or with status 400 (Bad Request) if the skAtEx has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sk-at-exes")
    @Timed
    public ResponseEntity<SkAtEx> createSkAtEx(@RequestBody SkAtEx skAtEx) throws URISyntaxException {
        log.debug("REST request to save SkAtEx : {}", skAtEx);
        if (skAtEx.getId() != null) {
            throw new BadRequestAlertException("A new skAtEx cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SkAtEx result = skAtExService.save(skAtEx);
        return ResponseEntity.created(new URI("/api/sk-at-exes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sk-at-exes : Updates an existing skAtEx.
     *
     * @param skAtEx the skAtEx to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skAtEx,
     * or with status 400 (Bad Request) if the skAtEx is not valid,
     * or with status 500 (Internal Server Error) if the skAtEx couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sk-at-exes")
    @Timed
    public ResponseEntity<SkAtEx> updateSkAtEx(@RequestBody SkAtEx skAtEx) throws URISyntaxException {
        log.debug("REST request to update SkAtEx : {}", skAtEx);
        if (skAtEx.getId() == null) {
            return createSkAtEx(skAtEx);
        }
        SkAtEx result = skAtExService.save(skAtEx);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, skAtEx.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sk-at-exes : get all the skAtExes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of skAtExes in body
     */
    @GetMapping("/sk-at-exes")
    @Timed
    public ResponseEntity<List<SkAtEx>> getAllSkAtExes(SkAtExCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SkAtExes by criteria: {}", criteria);
        Page<SkAtEx> page = skAtExQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sk-at-exes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sk-at-exes/:id : get the "id" skAtEx.
     *
     * @param id the id of the skAtEx to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skAtEx, or with status 404 (Not Found)
     */
    @GetMapping("/sk-at-exes/{id}")
    @Timed
    public ResponseEntity<SkAtEx> getSkAtEx(@PathVariable Long id) {
        log.debug("REST request to get SkAtEx : {}", id);
        SkAtEx skAtEx = skAtExService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(skAtEx));
    }

    /**
     * DELETE  /sk-at-exes/:id : delete the "id" skAtEx.
     *
     * @param id the id of the skAtEx to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sk-at-exes/{id}")
    @Timed
    public ResponseEntity<Void> deleteSkAtEx(@PathVariable Long id) {
        log.debug("REST request to delete SkAtEx : {}", id);
        skAtExService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
