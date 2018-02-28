package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.RequirementManagementApp;

import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.securityrat.requirementmanagement.domain.Attribute;
import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.securityrat.requirementmanagement.repository.AttributeKeyRepository;
import org.securityrat.requirementmanagement.service.AttributeKeyService;
import org.securityrat.requirementmanagement.web.rest.errors.ExceptionTranslator;
import org.securityrat.requirementmanagement.service.dto.AttributeKeyCriteria;
import org.securityrat.requirementmanagement.service.AttributeKeyQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.securityrat.requirementmanagement.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.securityrat.requirementmanagement.domain.enumeration.AttributeType;
/**
 * Test class for the AttributeKeyResource REST controller.
 *
 * @see AttributeKeyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RequirementManagementApp.class)
public class AttributeKeyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final AttributeType DEFAULT_TYPE = AttributeType.FE_TAG;
    private static final AttributeType UPDATED_TYPE = AttributeType.PARAMETER;

    private static final Integer DEFAULT_SHOW_ORDER = 1;
    private static final Integer UPDATED_SHOW_ORDER = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private AttributeKeyRepository attributeKeyRepository;

    @Autowired
    private AttributeKeyService attributeKeyService;

    @Autowired
    private AttributeKeyQueryService attributeKeyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttributeKeyMockMvc;

    private AttributeKey attributeKey;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttributeKeyResource attributeKeyResource = new AttributeKeyResource(attributeKeyService, attributeKeyQueryService);
        this.restAttributeKeyMockMvc = MockMvcBuilders.standaloneSetup(attributeKeyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeKey createEntity(EntityManager em) {
        AttributeKey attributeKey = new AttributeKey()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .showOrder(DEFAULT_SHOW_ORDER)
            .active(DEFAULT_ACTIVE);
        return attributeKey;
    }

    @Before
    public void initTest() {
        attributeKey = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttributeKey() throws Exception {
        int databaseSizeBeforeCreate = attributeKeyRepository.findAll().size();

        // Create the AttributeKey
        restAttributeKeyMockMvc.perform(post("/api/attribute-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeKey)))
            .andExpect(status().isCreated());

        // Validate the AttributeKey in the database
        List<AttributeKey> attributeKeyList = attributeKeyRepository.findAll();
        assertThat(attributeKeyList).hasSize(databaseSizeBeforeCreate + 1);
        AttributeKey testAttributeKey = attributeKeyList.get(attributeKeyList.size() - 1);
        assertThat(testAttributeKey.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttributeKey.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAttributeKey.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAttributeKey.getShowOrder()).isEqualTo(DEFAULT_SHOW_ORDER);
        assertThat(testAttributeKey.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createAttributeKeyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributeKeyRepository.findAll().size();

        // Create the AttributeKey with an existing ID
        attributeKey.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeKeyMockMvc.perform(post("/api/attribute-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeKey)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeKey in the database
        List<AttributeKey> attributeKeyList = attributeKeyRepository.findAll();
        assertThat(attributeKeyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeKeyRepository.findAll().size();
        // set the field null
        attributeKey.setName(null);

        // Create the AttributeKey, which fails.

        restAttributeKeyMockMvc.perform(post("/api/attribute-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeKey)))
            .andExpect(status().isBadRequest());

        List<AttributeKey> attributeKeyList = attributeKeyRepository.findAll();
        assertThat(attributeKeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeKeyRepository.findAll().size();
        // set the field null
        attributeKey.setType(null);

        // Create the AttributeKey, which fails.

        restAttributeKeyMockMvc.perform(post("/api/attribute-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeKey)))
            .andExpect(status().isBadRequest());

        List<AttributeKey> attributeKeyList = attributeKeyRepository.findAll();
        assertThat(attributeKeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeKeyRepository.findAll().size();
        // set the field null
        attributeKey.setActive(null);

        // Create the AttributeKey, which fails.

        restAttributeKeyMockMvc.perform(post("/api/attribute-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeKey)))
            .andExpect(status().isBadRequest());

        List<AttributeKey> attributeKeyList = attributeKeyRepository.findAll();
        assertThat(attributeKeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttributeKeys() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList
        restAttributeKeyMockMvc.perform(get("/api/attribute-keys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getAttributeKey() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get the attributeKey
        restAttributeKeyMockMvc.perform(get("/api/attribute-keys/{id}", attributeKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attributeKey.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.showOrder").value(DEFAULT_SHOW_ORDER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where name equals to DEFAULT_NAME
        defaultAttributeKeyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the attributeKeyList where name equals to UPDATED_NAME
        defaultAttributeKeyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByNameIsInShouldWork() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAttributeKeyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the attributeKeyList where name equals to UPDATED_NAME
        defaultAttributeKeyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where name is not null
        defaultAttributeKeyShouldBeFound("name.specified=true");

        // Get all the attributeKeyList where name is null
        defaultAttributeKeyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where type equals to DEFAULT_TYPE
        defaultAttributeKeyShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the attributeKeyList where type equals to UPDATED_TYPE
        defaultAttributeKeyShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAttributeKeyShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the attributeKeyList where type equals to UPDATED_TYPE
        defaultAttributeKeyShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where type is not null
        defaultAttributeKeyShouldBeFound("type.specified=true");

        // Get all the attributeKeyList where type is null
        defaultAttributeKeyShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByShowOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where showOrder equals to DEFAULT_SHOW_ORDER
        defaultAttributeKeyShouldBeFound("showOrder.equals=" + DEFAULT_SHOW_ORDER);

        // Get all the attributeKeyList where showOrder equals to UPDATED_SHOW_ORDER
        defaultAttributeKeyShouldNotBeFound("showOrder.equals=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByShowOrderIsInShouldWork() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where showOrder in DEFAULT_SHOW_ORDER or UPDATED_SHOW_ORDER
        defaultAttributeKeyShouldBeFound("showOrder.in=" + DEFAULT_SHOW_ORDER + "," + UPDATED_SHOW_ORDER);

        // Get all the attributeKeyList where showOrder equals to UPDATED_SHOW_ORDER
        defaultAttributeKeyShouldNotBeFound("showOrder.in=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByShowOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where showOrder is not null
        defaultAttributeKeyShouldBeFound("showOrder.specified=true");

        // Get all the attributeKeyList where showOrder is null
        defaultAttributeKeyShouldNotBeFound("showOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByShowOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where showOrder greater than or equals to DEFAULT_SHOW_ORDER
        defaultAttributeKeyShouldBeFound("showOrder.greaterOrEqualThan=" + DEFAULT_SHOW_ORDER);

        // Get all the attributeKeyList where showOrder greater than or equals to UPDATED_SHOW_ORDER
        defaultAttributeKeyShouldNotBeFound("showOrder.greaterOrEqualThan=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByShowOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where showOrder less than or equals to DEFAULT_SHOW_ORDER
        defaultAttributeKeyShouldNotBeFound("showOrder.lessThan=" + DEFAULT_SHOW_ORDER);

        // Get all the attributeKeyList where showOrder less than or equals to UPDATED_SHOW_ORDER
        defaultAttributeKeyShouldBeFound("showOrder.lessThan=" + UPDATED_SHOW_ORDER);
    }


    @Test
    @Transactional
    public void getAllAttributeKeysByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where active equals to DEFAULT_ACTIVE
        defaultAttributeKeyShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the attributeKeyList where active equals to UPDATED_ACTIVE
        defaultAttributeKeyShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultAttributeKeyShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the attributeKeyList where active equals to UPDATED_ACTIVE
        defaultAttributeKeyShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeKeyRepository.saveAndFlush(attributeKey);

        // Get all the attributeKeyList where active is not null
        defaultAttributeKeyShouldBeFound("active.specified=true");

        // Get all the attributeKeyList where active is null
        defaultAttributeKeyShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributeKeysByAttributeIsEqualToSomething() throws Exception {
        // Initialize the database
        Attribute attribute = AttributeResourceIntTest.createEntity(em);
        em.persist(attribute);
        em.flush();
        attributeKey.addAttribute(attribute);
        attributeKeyRepository.saveAndFlush(attributeKey);
        Long attributeId = attribute.getId();

        // Get all the attributeKeyList where attribute equals to attributeId
        defaultAttributeKeyShouldBeFound("attributeId.equals=" + attributeId);

        // Get all the attributeKeyList where attribute equals to attributeId + 1
        defaultAttributeKeyShouldNotBeFound("attributeId.equals=" + (attributeId + 1));
    }


    @Test
    @Transactional
    public void getAllAttributeKeysByRequirementSetIsEqualToSomething() throws Exception {
        // Initialize the database
        RequirementSet requirementSet = RequirementSetResourceIntTest.createEntity(em);
        em.persist(requirementSet);
        em.flush();
        attributeKey.setRequirementSet(requirementSet);
        attributeKeyRepository.saveAndFlush(attributeKey);
        Long requirementSetId = requirementSet.getId();

        // Get all the attributeKeyList where requirementSet equals to requirementSetId
        defaultAttributeKeyShouldBeFound("requirementSetId.equals=" + requirementSetId);

        // Get all the attributeKeyList where requirementSet equals to requirementSetId + 1
        defaultAttributeKeyShouldNotBeFound("requirementSetId.equals=" + (requirementSetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAttributeKeyShouldBeFound(String filter) throws Exception {
        restAttributeKeyMockMvc.perform(get("/api/attribute-keys?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAttributeKeyShouldNotBeFound(String filter) throws Exception {
        restAttributeKeyMockMvc.perform(get("/api/attribute-keys?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAttributeKey() throws Exception {
        // Get the attributeKey
        restAttributeKeyMockMvc.perform(get("/api/attribute-keys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttributeKey() throws Exception {
        // Initialize the database
        attributeKeyService.save(attributeKey);

        int databaseSizeBeforeUpdate = attributeKeyRepository.findAll().size();

        // Update the attributeKey
        AttributeKey updatedAttributeKey = attributeKeyRepository.findOne(attributeKey.getId());
        // Disconnect from session so that the updates on updatedAttributeKey are not directly saved in db
        em.detach(updatedAttributeKey);
        updatedAttributeKey
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);

        restAttributeKeyMockMvc.perform(put("/api/attribute-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttributeKey)))
            .andExpect(status().isOk());

        // Validate the AttributeKey in the database
        List<AttributeKey> attributeKeyList = attributeKeyRepository.findAll();
        assertThat(attributeKeyList).hasSize(databaseSizeBeforeUpdate);
        AttributeKey testAttributeKey = attributeKeyList.get(attributeKeyList.size() - 1);
        assertThat(testAttributeKey.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttributeKey.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAttributeKey.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAttributeKey.getShowOrder()).isEqualTo(UPDATED_SHOW_ORDER);
        assertThat(testAttributeKey.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingAttributeKey() throws Exception {
        int databaseSizeBeforeUpdate = attributeKeyRepository.findAll().size();

        // Create the AttributeKey

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAttributeKeyMockMvc.perform(put("/api/attribute-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attributeKey)))
            .andExpect(status().isCreated());

        // Validate the AttributeKey in the database
        List<AttributeKey> attributeKeyList = attributeKeyRepository.findAll();
        assertThat(attributeKeyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAttributeKey() throws Exception {
        // Initialize the database
        attributeKeyService.save(attributeKey);

        int databaseSizeBeforeDelete = attributeKeyRepository.findAll().size();

        // Get the attributeKey
        restAttributeKeyMockMvc.perform(delete("/api/attribute-keys/{id}", attributeKey.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AttributeKey> attributeKeyList = attributeKeyRepository.findAll();
        assertThat(attributeKeyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeKey.class);
        AttributeKey attributeKey1 = new AttributeKey();
        attributeKey1.setId(1L);
        AttributeKey attributeKey2 = new AttributeKey();
        attributeKey2.setId(attributeKey1.getId());
        assertThat(attributeKey1).isEqualTo(attributeKey2);
        attributeKey2.setId(2L);
        assertThat(attributeKey1).isNotEqualTo(attributeKey2);
        attributeKey1.setId(null);
        assertThat(attributeKey1).isNotEqualTo(attributeKey2);
    }
}
