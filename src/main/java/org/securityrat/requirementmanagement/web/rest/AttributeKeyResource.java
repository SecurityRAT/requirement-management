package org.securityrat.requirementmanagement.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.securityrat.requirementmanagement.service.AttributeKeyService;
import org.securityrat.requirementmanagement.web.rest.errors.BadRequestAlertException;
import org.securityrat.requirementmanagement.web.rest.util.HeaderUtil;
import org.securityrat.requirementmanagement.web.rest.util.PaginationUtil;
import org.securityrat.requirementmanagement.service.dto.AttributeKeyCriteria;
import org.securityrat.requirementmanagement.service.AttributeKeyQueryService;
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
 * REST controller for managing AttributeKey.
 */
@RestController
@RequestMapping("/api")
public class AttributeKeyResource {

    private final Logger log = LoggerFactory.getLogger(AttributeKeyResource.class);

    private static final String ENTITY_NAME = "attributeKey";

    private final AttributeKeyService attributeKeyService;

    private final AttributeKeyQueryService attributeKeyQueryService;

    public AttributeKeyResource(AttributeKeyService attributeKeyService, AttributeKeyQueryService attributeKeyQueryService) {
        this.attributeKeyService = attributeKeyService;
        this.attributeKeyQueryService = attributeKeyQueryService;
    }

    /**
     * POST  /attribute-keys : Create a new attributeKey.
     *
     * @param attributeKey the attributeKey to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attributeKey, or with status 400 (Bad Request) if the attributeKey has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attribute-keys")
    @Timed
    public ResponseEntity<AttributeKey> createAttributeKey(@Valid @RequestBody AttributeKey attributeKey) throws URISyntaxException {
        log.debug("REST request to save AttributeKey : {}", attributeKey);
        if (attributeKey.getId() != null) {
            throw new BadRequestAlertException("A new attributeKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttributeKey result = attributeKeyService.save(attributeKey);
        return ResponseEntity.created(new URI("/api/attribute-keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attribute-keys : Updates an existing attributeKey.
     *
     * @param attributeKey the attributeKey to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attributeKey,
     * or with status 400 (Bad Request) if the attributeKey is not valid,
     * or with status 500 (Internal Server Error) if the attributeKey couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attribute-keys")
    @Timed
    public ResponseEntity<AttributeKey> updateAttributeKey(@Valid @RequestBody AttributeKey attributeKey) throws URISyntaxException {
        log.debug("REST request to update AttributeKey : {}", attributeKey);
        if (attributeKey.getId() == null) {
            return createAttributeKey(attributeKey);
        }
        AttributeKey result = attributeKeyService.save(attributeKey);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attributeKey.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attribute-keys : get all the attributeKeys.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of attributeKeys in body
     */
    @GetMapping("/attribute-keys")
    @Timed
    public ResponseEntity<List<AttributeKey>> getAllAttributeKeys(AttributeKeyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AttributeKeys by criteria: {}", criteria);
        Page<AttributeKey> page = attributeKeyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attribute-keys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attribute-keys/:id : get the "id" attributeKey.
     *
     * @param id the id of the attributeKey to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attributeKey, or with status 404 (Not Found)
     */
    @GetMapping("/attribute-keys/{id}")
    @Timed
    public ResponseEntity<AttributeKey> getAttributeKey(@PathVariable Long id) {
        log.debug("REST request to get AttributeKey : {}", id);
        AttributeKey attributeKey = attributeKeyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attributeKey));
    }

    /**
     * DELETE  /attribute-keys/:id : delete the "id" attributeKey.
     *
     * @param id the id of the attributeKey to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attribute-keys/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttributeKey(@PathVariable Long id) {
        log.debug("REST request to delete AttributeKey : {}", id);
        attributeKeyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
