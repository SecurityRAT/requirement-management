package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.RequirementManagementApp;
import org.securityrat.requirementmanagement.config.TestSecurityConfiguration;
import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.securityrat.requirementmanagement.domain.Extension;
import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.securityrat.requirementmanagement.repository.ExtensionKeyRepository;
import org.securityrat.requirementmanagement.service.ExtensionKeyService;
import org.securityrat.requirementmanagement.web.rest.errors.ExceptionTranslator;
import org.securityrat.requirementmanagement.service.dto.ExtensionKeyCriteria;
import org.securityrat.requirementmanagement.service.ExtensionKeyQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.securityrat.requirementmanagement.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.securityrat.requirementmanagement.domain.enumeration.ExtensionSection;
import org.securityrat.requirementmanagement.domain.enumeration.ExtensionType;
/**
 * Integration tests for the {@link ExtensionKeyResource} REST controller.
 */
@SpringBootTest(classes = {RequirementManagementApp.class, TestSecurityConfiguration.class})
public class ExtensionKeyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ExtensionSection DEFAULT_SECTION = ExtensionSection.STATUS;
    private static final ExtensionSection UPDATED_SECTION = ExtensionSection.ENHANCEMENT;

    private static final ExtensionType DEFAULT_TYPE = ExtensionType.ENUM;
    private static final ExtensionType UPDATED_TYPE = ExtensionType.FREETEXT;

    private static final Integer DEFAULT_SHOW_ORDER = 1;
    private static final Integer UPDATED_SHOW_ORDER = 2;
    private static final Integer SMALLER_SHOW_ORDER = 1 - 1;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ExtensionKeyRepository extensionKeyRepository;

    @Autowired
    private ExtensionKeyService extensionKeyService;

    @Autowired
    private ExtensionKeyQueryService extensionKeyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restExtensionKeyMockMvc;

    private ExtensionKey extensionKey;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExtensionKeyResource extensionKeyResource = new ExtensionKeyResource(extensionKeyService, extensionKeyQueryService);
        this.restExtensionKeyMockMvc = MockMvcBuilders.standaloneSetup(extensionKeyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtensionKey createEntity(EntityManager em) {
        ExtensionKey extensionKey = new ExtensionKey()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .section(DEFAULT_SECTION)
            .type(DEFAULT_TYPE)
            .showOrder(DEFAULT_SHOW_ORDER)
            .active(DEFAULT_ACTIVE);
        return extensionKey;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtensionKey createUpdatedEntity(EntityManager em) {
        ExtensionKey extensionKey = new ExtensionKey()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .section(UPDATED_SECTION)
            .type(UPDATED_TYPE)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);
        return extensionKey;
    }

    @BeforeEach
    public void initTest() {
        extensionKey = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtensionKey() throws Exception {
        int databaseSizeBeforeCreate = extensionKeyRepository.findAll().size();

        // Create the ExtensionKey
        restExtensionKeyMockMvc.perform(post("/api/extension-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extensionKey)))
            .andExpect(status().isCreated());

        // Validate the ExtensionKey in the database
        List<ExtensionKey> extensionKeyList = extensionKeyRepository.findAll();
        assertThat(extensionKeyList).hasSize(databaseSizeBeforeCreate + 1);
        ExtensionKey testExtensionKey = extensionKeyList.get(extensionKeyList.size() - 1);
        assertThat(testExtensionKey.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExtensionKey.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExtensionKey.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testExtensionKey.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testExtensionKey.getShowOrder()).isEqualTo(DEFAULT_SHOW_ORDER);
        assertThat(testExtensionKey.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createExtensionKeyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extensionKeyRepository.findAll().size();

        // Create the ExtensionKey with an existing ID
        extensionKey.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtensionKeyMockMvc.perform(post("/api/extension-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extensionKey)))
            .andExpect(status().isBadRequest());

        // Validate the ExtensionKey in the database
        List<ExtensionKey> extensionKeyList = extensionKeyRepository.findAll();
        assertThat(extensionKeyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = extensionKeyRepository.findAll().size();
        // set the field null
        extensionKey.setName(null);

        // Create the ExtensionKey, which fails.

        restExtensionKeyMockMvc.perform(post("/api/extension-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extensionKey)))
            .andExpect(status().isBadRequest());

        List<ExtensionKey> extensionKeyList = extensionKeyRepository.findAll();
        assertThat(extensionKeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = extensionKeyRepository.findAll().size();
        // set the field null
        extensionKey.setSection(null);

        // Create the ExtensionKey, which fails.

        restExtensionKeyMockMvc.perform(post("/api/extension-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extensionKey)))
            .andExpect(status().isBadRequest());

        List<ExtensionKey> extensionKeyList = extensionKeyRepository.findAll();
        assertThat(extensionKeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = extensionKeyRepository.findAll().size();
        // set the field null
        extensionKey.setActive(null);

        // Create the ExtensionKey, which fails.

        restExtensionKeyMockMvc.perform(post("/api/extension-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extensionKey)))
            .andExpect(status().isBadRequest());

        List<ExtensionKey> extensionKeyList = extensionKeyRepository.findAll();
        assertThat(extensionKeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExtensionKeys() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList
        restExtensionKeyMockMvc.perform(get("/api/extension-keys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extensionKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getExtensionKey() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get the extensionKey
        restExtensionKeyMockMvc.perform(get("/api/extension-keys/{id}", extensionKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extensionKey.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.showOrder").value(DEFAULT_SHOW_ORDER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where name equals to DEFAULT_NAME
        defaultExtensionKeyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the extensionKeyList where name equals to UPDATED_NAME
        defaultExtensionKeyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByNameIsInShouldWork() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultExtensionKeyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the extensionKeyList where name equals to UPDATED_NAME
        defaultExtensionKeyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where name is not null
        defaultExtensionKeyShouldBeFound("name.specified=true");

        // Get all the extensionKeyList where name is null
        defaultExtensionKeyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtensionKeysBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where section equals to DEFAULT_SECTION
        defaultExtensionKeyShouldBeFound("section.equals=" + DEFAULT_SECTION);

        // Get all the extensionKeyList where section equals to UPDATED_SECTION
        defaultExtensionKeyShouldNotBeFound("section.equals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysBySectionIsInShouldWork() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where section in DEFAULT_SECTION or UPDATED_SECTION
        defaultExtensionKeyShouldBeFound("section.in=" + DEFAULT_SECTION + "," + UPDATED_SECTION);

        // Get all the extensionKeyList where section equals to UPDATED_SECTION
        defaultExtensionKeyShouldNotBeFound("section.in=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysBySectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where section is not null
        defaultExtensionKeyShouldBeFound("section.specified=true");

        // Get all the extensionKeyList where section is null
        defaultExtensionKeyShouldNotBeFound("section.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where type equals to DEFAULT_TYPE
        defaultExtensionKeyShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the extensionKeyList where type equals to UPDATED_TYPE
        defaultExtensionKeyShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultExtensionKeyShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the extensionKeyList where type equals to UPDATED_TYPE
        defaultExtensionKeyShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where type is not null
        defaultExtensionKeyShouldBeFound("type.specified=true");

        // Get all the extensionKeyList where type is null
        defaultExtensionKeyShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByShowOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where showOrder equals to DEFAULT_SHOW_ORDER
        defaultExtensionKeyShouldBeFound("showOrder.equals=" + DEFAULT_SHOW_ORDER);

        // Get all the extensionKeyList where showOrder equals to UPDATED_SHOW_ORDER
        defaultExtensionKeyShouldNotBeFound("showOrder.equals=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByShowOrderIsInShouldWork() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where showOrder in DEFAULT_SHOW_ORDER or UPDATED_SHOW_ORDER
        defaultExtensionKeyShouldBeFound("showOrder.in=" + DEFAULT_SHOW_ORDER + "," + UPDATED_SHOW_ORDER);

        // Get all the extensionKeyList where showOrder equals to UPDATED_SHOW_ORDER
        defaultExtensionKeyShouldNotBeFound("showOrder.in=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByShowOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where showOrder is not null
        defaultExtensionKeyShouldBeFound("showOrder.specified=true");

        // Get all the extensionKeyList where showOrder is null
        defaultExtensionKeyShouldNotBeFound("showOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByShowOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where showOrder is greater than or equal to DEFAULT_SHOW_ORDER
        defaultExtensionKeyShouldBeFound("showOrder.greaterThanOrEqual=" + DEFAULT_SHOW_ORDER);

        // Get all the extensionKeyList where showOrder is greater than or equal to UPDATED_SHOW_ORDER
        defaultExtensionKeyShouldNotBeFound("showOrder.greaterThanOrEqual=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByShowOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where showOrder is less than or equal to DEFAULT_SHOW_ORDER
        defaultExtensionKeyShouldBeFound("showOrder.lessThanOrEqual=" + DEFAULT_SHOW_ORDER);

        // Get all the extensionKeyList where showOrder is less than or equal to SMALLER_SHOW_ORDER
        defaultExtensionKeyShouldNotBeFound("showOrder.lessThanOrEqual=" + SMALLER_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByShowOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where showOrder is less than DEFAULT_SHOW_ORDER
        defaultExtensionKeyShouldNotBeFound("showOrder.lessThan=" + DEFAULT_SHOW_ORDER);

        // Get all the extensionKeyList where showOrder is less than UPDATED_SHOW_ORDER
        defaultExtensionKeyShouldBeFound("showOrder.lessThan=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByShowOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where showOrder is greater than DEFAULT_SHOW_ORDER
        defaultExtensionKeyShouldNotBeFound("showOrder.greaterThan=" + DEFAULT_SHOW_ORDER);

        // Get all the extensionKeyList where showOrder is greater than SMALLER_SHOW_ORDER
        defaultExtensionKeyShouldBeFound("showOrder.greaterThan=" + SMALLER_SHOW_ORDER);
    }


    @Test
    @Transactional
    public void getAllExtensionKeysByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where active equals to DEFAULT_ACTIVE
        defaultExtensionKeyShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the extensionKeyList where active equals to UPDATED_ACTIVE
        defaultExtensionKeyShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultExtensionKeyShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the extensionKeyList where active equals to UPDATED_ACTIVE
        defaultExtensionKeyShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);

        // Get all the extensionKeyList where active is not null
        defaultExtensionKeyShouldBeFound("active.specified=true");

        // Get all the extensionKeyList where active is null
        defaultExtensionKeyShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtensionKeysByExtensionIsEqualToSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);
        Extension extension = ExtensionResourceIT.createEntity(em);
        em.persist(extension);
        em.flush();
        extensionKey.addExtension(extension);
        extensionKeyRepository.saveAndFlush(extensionKey);
        Long extensionId = extension.getId();

        // Get all the extensionKeyList where extension equals to extensionId
        defaultExtensionKeyShouldBeFound("extensionId.equals=" + extensionId);

        // Get all the extensionKeyList where extension equals to extensionId + 1
        defaultExtensionKeyShouldNotBeFound("extensionId.equals=" + (extensionId + 1));
    }


    @Test
    @Transactional
    public void getAllExtensionKeysByRequirementSetIsEqualToSomething() throws Exception {
        // Initialize the database
        extensionKeyRepository.saveAndFlush(extensionKey);
        RequirementSet requirementSet = RequirementSetResourceIT.createEntity(em);
        em.persist(requirementSet);
        em.flush();
        extensionKey.setRequirementSet(requirementSet);
        extensionKeyRepository.saveAndFlush(extensionKey);
        Long requirementSetId = requirementSet.getId();

        // Get all the extensionKeyList where requirementSet equals to requirementSetId
        defaultExtensionKeyShouldBeFound("requirementSetId.equals=" + requirementSetId);

        // Get all the extensionKeyList where requirementSet equals to requirementSetId + 1
        defaultExtensionKeyShouldNotBeFound("requirementSetId.equals=" + (requirementSetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExtensionKeyShouldBeFound(String filter) throws Exception {
        restExtensionKeyMockMvc.perform(get("/api/extension-keys?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extensionKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restExtensionKeyMockMvc.perform(get("/api/extension-keys/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExtensionKeyShouldNotBeFound(String filter) throws Exception {
        restExtensionKeyMockMvc.perform(get("/api/extension-keys?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExtensionKeyMockMvc.perform(get("/api/extension-keys/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExtensionKey() throws Exception {
        // Get the extensionKey
        restExtensionKeyMockMvc.perform(get("/api/extension-keys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtensionKey() throws Exception {
        // Initialize the database
        extensionKeyService.save(extensionKey);

        int databaseSizeBeforeUpdate = extensionKeyRepository.findAll().size();

        // Update the extensionKey
        ExtensionKey updatedExtensionKey = extensionKeyRepository.findById(extensionKey.getId()).get();
        // Disconnect from session so that the updates on updatedExtensionKey are not directly saved in db
        em.detach(updatedExtensionKey);
        updatedExtensionKey
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .section(UPDATED_SECTION)
            .type(UPDATED_TYPE)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);

        restExtensionKeyMockMvc.perform(put("/api/extension-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtensionKey)))
            .andExpect(status().isOk());

        // Validate the ExtensionKey in the database
        List<ExtensionKey> extensionKeyList = extensionKeyRepository.findAll();
        assertThat(extensionKeyList).hasSize(databaseSizeBeforeUpdate);
        ExtensionKey testExtensionKey = extensionKeyList.get(extensionKeyList.size() - 1);
        assertThat(testExtensionKey.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExtensionKey.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExtensionKey.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testExtensionKey.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExtensionKey.getShowOrder()).isEqualTo(UPDATED_SHOW_ORDER);
        assertThat(testExtensionKey.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingExtensionKey() throws Exception {
        int databaseSizeBeforeUpdate = extensionKeyRepository.findAll().size();

        // Create the ExtensionKey

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtensionKeyMockMvc.perform(put("/api/extension-keys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extensionKey)))
            .andExpect(status().isBadRequest());

        // Validate the ExtensionKey in the database
        List<ExtensionKey> extensionKeyList = extensionKeyRepository.findAll();
        assertThat(extensionKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExtensionKey() throws Exception {
        // Initialize the database
        extensionKeyService.save(extensionKey);

        int databaseSizeBeforeDelete = extensionKeyRepository.findAll().size();

        // Delete the extensionKey
        restExtensionKeyMockMvc.perform(delete("/api/extension-keys/{id}", extensionKey.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExtensionKey> extensionKeyList = extensionKeyRepository.findAll();
        assertThat(extensionKeyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtensionKey.class);
        ExtensionKey extensionKey1 = new ExtensionKey();
        extensionKey1.setId(1L);
        ExtensionKey extensionKey2 = new ExtensionKey();
        extensionKey2.setId(extensionKey1.getId());
        assertThat(extensionKey1).isEqualTo(extensionKey2);
        extensionKey2.setId(2L);
        assertThat(extensionKey1).isNotEqualTo(extensionKey2);
        extensionKey1.setId(null);
        assertThat(extensionKey1).isNotEqualTo(extensionKey2);
    }
}
