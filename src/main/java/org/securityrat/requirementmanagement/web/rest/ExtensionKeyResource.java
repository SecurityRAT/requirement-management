package org.securityrat.requirementmanagement.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.securityrat.requirementmanagement.service.ExtensionKeyService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.web.rest.util.HeaderUtil;
import org.securityrat.requirementmanagement.web.rest.util.PaginationUtil;
import org.securityrat.requirementmanagement.service.dto.ExtensionKeyCriteria;
import org.securityrat.requirementmanagement.service.ExtensionKeyQueryService;
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
 * REST controller for managing ExtensionKey.
 */
@RestController
@RequestMapping("/api")
public class ExtensionKeyResource {

    private final Logger log = LoggerFactory.getLogger(ExtensionKeyResource.class);

    private static final String ENTITY_NAME = "extensionKey";

    private final ExtensionKeyService extensionKeyService;

    private final ExtensionKeyQueryService extensionKeyQueryService;

    public ExtensionKeyResource(ExtensionKeyService extensionKeyService, ExtensionKeyQueryService extensionKeyQueryService) {
        this.extensionKeyService = extensionKeyService;
        this.extensionKeyQueryService = extensionKeyQueryService;
    }

    /**
     * POST  /extension-keys : Create a new extensionKey.
     *
     * @param extensionKey the extensionKey to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extensionKey, or with status 400 (Bad Request) if the extensionKey has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/extension-keys")
    @Timed
    public ResponseEntity<ExtensionKey> createExtensionKey(@Valid @RequestBody ExtensionKey extensionKey) throws URISyntaxException {
        log.debug("REST request to save ExtensionKey : {}", extensionKey);
        if (extensionKey.getId() != null) {
            throw new BadRequestAlertException("A new extensionKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtensionKey result = extensionKeyService.save(extensionKey);
        return ResponseEntity.created(new URI("/api/extension-keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /extension-keys : Updates an existing extensionKey.
     *
     * @param extensionKey the extensionKey to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extensionKey,
     * or with status 400 (Bad Request) if the extensionKey is not valid,
     * or with status 500 (Internal Server Error) if the extensionKey couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/extension-keys")
    @Timed
    public ResponseEntity<ExtensionKey> updateExtensionKey(@Valid @RequestBody ExtensionKey extensionKey) throws URISyntaxException {
        log.debug("REST request to update ExtensionKey : {}", extensionKey);
        if (extensionKey.getId() == null) {
            return createExtensionKey(extensionKey);
        }
        ExtensionKey result = extensionKeyService.save(extensionKey);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, extensionKey.getId().toString()))
            .body(result);
    }

    /**
     * GET  /extension-keys : get all the extensionKeys.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of extensionKeys in body
     */
    @GetMapping("/extension-keys")
    @Timed
    public ResponseEntity<List<ExtensionKey>> getAllExtensionKeys(ExtensionKeyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExtensionKeys by criteria: {}", criteria);
        Page<ExtensionKey> page = extensionKeyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/extension-keys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /extension-keys/:id : get the "id" extensionKey.
     *
     * @param id the id of the extensionKey to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extensionKey, or with status 404 (Not Found)
     */
    @GetMapping("/extension-keys/{id}")
    @Timed
    public ResponseEntity<ExtensionKey> getExtensionKey(@PathVariable Long id) {
        log.debug("REST request to get ExtensionKey : {}", id);
        ExtensionKey extensionKey = extensionKeyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(extensionKey));
    }

    /**
     * DELETE  /extension-keys/:id : delete the "id" extensionKey.
     *
     * @param id the id of the extensionKey to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/extension-keys/{id}")
    @Timed
    public ResponseEntity<Void> deleteExtensionKey(@PathVariable Long id) {
        log.debug("REST request to delete ExtensionKey : {}", id);
        extensionKeyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
