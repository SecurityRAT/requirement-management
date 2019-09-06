package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.securityrat.requirementmanagement.service.AttributeKeyService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.service.dto.AttributeKeyCriteria;
import org.securityrat.requirementmanagement.service.AttributeKeyQueryService;

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
 * REST controller for managing {@link org.securityrat.requirementmanagement.domain.AttributeKey}.
 */
@RestController
@RequestMapping("/api")
public class AttributeKeyResource {

    private final Logger log = LoggerFactory.getLogger(AttributeKeyResource.class);

    private static final String ENTITY_NAME = "requirementManagementAttributeKey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributeKeyService attributeKeyService;

    private final AttributeKeyQueryService attributeKeyQueryService;

    public AttributeKeyResource(AttributeKeyService attributeKeyService, AttributeKeyQueryService attributeKeyQueryService) {
        this.attributeKeyService = attributeKeyService;
        this.attributeKeyQueryService = attributeKeyQueryService;
    }

    /**
     * {@code POST  /attribute-keys} : Create a new attributeKey.
     *
     * @param attributeKey the attributeKey to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributeKey, or with status {@code 400 (Bad Request)} if the attributeKey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attribute-keys")
    public ResponseEntity<AttributeKey> createAttributeKey(@Valid @RequestBody AttributeKey attributeKey) throws URISyntaxException {
        log.debug("REST request to save AttributeKey : {}", attributeKey);
        if (attributeKey.getId() != null) {
            throw new BadRequestAlertException("A new attributeKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttributeKey result = attributeKeyService.save(attributeKey);
        return ResponseEntity.created(new URI("/api/attribute-keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attribute-keys} : Updates an existing attributeKey.
     *
     * @param attributeKey the attributeKey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeKey,
     * or with status {@code 400 (Bad Request)} if the attributeKey is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributeKey couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attribute-keys")
    public ResponseEntity<AttributeKey> updateAttributeKey(@Valid @RequestBody AttributeKey attributeKey) throws URISyntaxException {
        log.debug("REST request to update AttributeKey : {}", attributeKey);
        if (attributeKey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttributeKey result = attributeKeyService.save(attributeKey);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attributeKey.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attribute-keys} : get all the attributeKeys.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributeKeys in body.
     */
    @GetMapping("/attribute-keys")
    public ResponseEntity<List<AttributeKey>> getAllAttributeKeys(AttributeKeyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AttributeKeys by criteria: {}", criteria);
        Page<AttributeKey> page = attributeKeyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /attribute-keys/count} : count all the attributeKeys.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/attribute-keys/count")
    public ResponseEntity<Long> countAttributeKeys(AttributeKeyCriteria criteria) {
        log.debug("REST request to count AttributeKeys by criteria: {}", criteria);
        return ResponseEntity.ok().body(attributeKeyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /attribute-keys/:id} : get the "id" attributeKey.
     *
     * @param id the id of the attributeKey to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributeKey, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attribute-keys/{id}")
    public ResponseEntity<AttributeKey> getAttributeKey(@PathVariable Long id) {
        log.debug("REST request to get AttributeKey : {}", id);
        Optional<AttributeKey> attributeKey = attributeKeyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attributeKey);
    }

    /**
     * {@code DELETE  /attribute-keys/:id} : delete the "id" attributeKey.
     *
     * @param id the id of the attributeKey to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attribute-keys/{id}")
    public ResponseEntity<Void> deleteAttributeKey(@PathVariable Long id) {
        log.debug("REST request to delete AttributeKey : {}", id);
        attributeKeyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
