package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.domain.Extension;
import org.securityrat.requirementmanagement.service.ExtensionService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.service.dto.ExtensionCriteria;
import org.securityrat.requirementmanagement.service.ExtensionQueryService;

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
 * REST controller for managing {@link org.securityrat.requirementmanagement.domain.Extension}.
 */
@RestController
@RequestMapping("/api")
public class ExtensionResource {

    private final Logger log = LoggerFactory.getLogger(ExtensionResource.class);

    private static final String ENTITY_NAME = "requirementManagementExtension";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtensionService extensionService;

    private final ExtensionQueryService extensionQueryService;

    public ExtensionResource(ExtensionService extensionService, ExtensionQueryService extensionQueryService) {
        this.extensionService = extensionService;
        this.extensionQueryService = extensionQueryService;
    }

    /**
     * {@code POST  /extensions} : Create a new extension.
     *
     * @param extension the extension to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extension, or with status {@code 400 (Bad Request)} if the extension has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extensions")
    public ResponseEntity<Extension> createExtension(@Valid @RequestBody Extension extension) throws URISyntaxException {
        log.debug("REST request to save Extension : {}", extension);
        if (extension.getId() != null) {
            throw new BadRequestAlertException("A new extension cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Extension result = extensionService.save(extension);
        return ResponseEntity.created(new URI("/api/extensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extensions} : Updates an existing extension.
     *
     * @param extension the extension to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extension,
     * or with status {@code 400 (Bad Request)} if the extension is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extension couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extensions")
    public ResponseEntity<Extension> updateExtension(@Valid @RequestBody Extension extension) throws URISyntaxException {
        log.debug("REST request to update Extension : {}", extension);
        if (extension.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Extension result = extensionService.save(extension);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, extension.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /extensions} : get all the extensions.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extensions in body.
     */
    @GetMapping("/extensions")
    public ResponseEntity<List<Extension>> getAllExtensions(ExtensionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Extensions by criteria: {}", criteria);
        Page<Extension> page = extensionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /extensions/count} : count all the extensions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/extensions/count")
    public ResponseEntity<Long> countExtensions(ExtensionCriteria criteria) {
        log.debug("REST request to count Extensions by criteria: {}", criteria);
        return ResponseEntity.ok().body(extensionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /extensions/:id} : get the "id" extension.
     *
     * @param id the id of the extension to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extension, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extensions/{id}")
    public ResponseEntity<Extension> getExtension(@PathVariable Long id) {
        log.debug("REST request to get Extension : {}", id);
        Optional<Extension> extension = extensionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extension);
    }

    /**
     * {@code DELETE  /extensions/:id} : delete the "id" extension.
     *
     * @param id the id of the extension to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extensions/{id}")
    public ResponseEntity<Void> deleteExtension(@PathVariable Long id) {
        log.debug("REST request to delete Extension : {}", id);
        extensionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
