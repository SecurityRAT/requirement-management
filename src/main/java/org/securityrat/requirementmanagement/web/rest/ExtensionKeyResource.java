package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.securityrat.requirementmanagement.service.ExtensionKeyService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.service.dto.ExtensionKeyCriteria;
import org.securityrat.requirementmanagement.service.ExtensionKeyQueryService;

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
 * REST controller for managing {@link org.securityrat.requirementmanagement.domain.ExtensionKey}.
 */
@RestController
@RequestMapping("/api")
public class ExtensionKeyResource {

    private final Logger log = LoggerFactory.getLogger(ExtensionKeyResource.class);

    private static final String ENTITY_NAME = "requirementManagementExtensionKey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtensionKeyService extensionKeyService;

    private final ExtensionKeyQueryService extensionKeyQueryService;

    public ExtensionKeyResource(ExtensionKeyService extensionKeyService, ExtensionKeyQueryService extensionKeyQueryService) {
        this.extensionKeyService = extensionKeyService;
        this.extensionKeyQueryService = extensionKeyQueryService;
    }

    /**
     * {@code POST  /extension-keys} : Create a new extensionKey.
     *
     * @param extensionKey the extensionKey to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extensionKey, or with status {@code 400 (Bad Request)} if the extensionKey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extension-keys")
    public ResponseEntity<ExtensionKey> createExtensionKey(@Valid @RequestBody ExtensionKey extensionKey) throws URISyntaxException {
        log.debug("REST request to save ExtensionKey : {}", extensionKey);
        if (extensionKey.getId() != null) {
            throw new BadRequestAlertException("A new extensionKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtensionKey result = extensionKeyService.save(extensionKey);
        return ResponseEntity.created(new URI("/api/extension-keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extension-keys} : Updates an existing extensionKey.
     *
     * @param extensionKey the extensionKey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extensionKey,
     * or with status {@code 400 (Bad Request)} if the extensionKey is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extensionKey couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extension-keys")
    public ResponseEntity<ExtensionKey> updateExtensionKey(@Valid @RequestBody ExtensionKey extensionKey) throws URISyntaxException {
        log.debug("REST request to update ExtensionKey : {}", extensionKey);
        if (extensionKey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExtensionKey result = extensionKeyService.save(extensionKey);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, extensionKey.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /extension-keys} : get all the extensionKeys.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extensionKeys in body.
     */
    @GetMapping("/extension-keys")
    public ResponseEntity<List<ExtensionKey>> getAllExtensionKeys(ExtensionKeyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExtensionKeys by criteria: {}", criteria);
        Page<ExtensionKey> page = extensionKeyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /extension-keys/count} : count all the extensionKeys.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/extension-keys/count")
    public ResponseEntity<Long> countExtensionKeys(ExtensionKeyCriteria criteria) {
        log.debug("REST request to count ExtensionKeys by criteria: {}", criteria);
        return ResponseEntity.ok().body(extensionKeyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /extension-keys/:id} : get the "id" extensionKey.
     *
     * @param id the id of the extensionKey to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extensionKey, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extension-keys/{id}")
    public ResponseEntity<ExtensionKey> getExtensionKey(@PathVariable Long id) {
        log.debug("REST request to get ExtensionKey : {}", id);
        Optional<ExtensionKey> extensionKey = extensionKeyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extensionKey);
    }

    /**
     * {@code DELETE  /extension-keys/:id} : delete the "id" extensionKey.
     *
     * @param id the id of the extensionKey to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extension-keys/{id}")
    public ResponseEntity<Void> deleteExtensionKey(@PathVariable Long id) {
        log.debug("REST request to delete ExtensionKey : {}", id);
        extensionKeyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
