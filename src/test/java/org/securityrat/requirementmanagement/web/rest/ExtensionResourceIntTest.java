package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.RequirementManagementApp;

import org.securityrat.requirementmanagement.domain.Extension;
import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.securityrat.requirementmanagement.repository.ExtensionRepository;
import org.securityrat.requirementmanagement.service.ExtensionService;
import org.securityrat.requirementmanagement.web.rest.errors.ExceptionTranslator;
import org.securityrat.requirementmanagement.service.dto.ExtensionCriteria;
import org.securityrat.requirementmanagement.service.ExtensionQueryService;

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

/**
 * Test class for the ExtensionResource REST controller.
 *
 * @see ExtensionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RequirementManagementApp.class)
public class ExtensionResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SHOW_ORDER = 1;
    private static final Integer UPDATED_SHOW_ORDER = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ExtensionRepository extensionRepository;

    @Autowired
    private ExtensionService extensionService;

    @Autowired
    private ExtensionQueryService extensionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExtensionMockMvc;

    private Extension extension;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExtensionResource extensionResource = new ExtensionResource(extensionService, extensionQueryService);
        this.restExtensionMockMvc = MockMvcBuilders.standaloneSetup(extensionResource)
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
    public static Extension createEntity(EntityManager em) {
        Extension extension = new Extension()
            .content(DEFAULT_CONTENT)
            .description(DEFAULT_DESCRIPTION)
            .showOrder(DEFAULT_SHOW_ORDER)
            .active(DEFAULT_ACTIVE);
        return extension;
    }

    @Before
    public void initTest() {
        extension = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtension() throws Exception {
        int databaseSizeBeforeCreate = extensionRepository.findAll().size();

        // Create the Extension
        restExtensionMockMvc.perform(post("/api/extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extension)))
            .andExpect(status().isCreated());

        // Validate the Extension in the database
        List<Extension> extensionList = extensionRepository.findAll();
        assertThat(extensionList).hasSize(databaseSizeBeforeCreate + 1);
        Extension testExtension = extensionList.get(extensionList.size() - 1);
        assertThat(testExtension.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testExtension.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExtension.getShowOrder()).isEqualTo(DEFAULT_SHOW_ORDER);
        assertThat(testExtension.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createExtensionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extensionRepository.findAll().size();

        // Create the Extension with an existing ID
        extension.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtensionMockMvc.perform(post("/api/extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extension)))
            .andExpect(status().isBadRequest());

        // Validate the Extension in the database
        List<Extension> extensionList = extensionRepository.findAll();
        assertThat(extensionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = extensionRepository.findAll().size();
        // set the field null
        extension.setContent(null);

        // Create the Extension, which fails.

        restExtensionMockMvc.perform(post("/api/extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extension)))
            .andExpect(status().isBadRequest());

        List<Extension> extensionList = extensionRepository.findAll();
        assertThat(extensionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = extensionRepository.findAll().size();
        // set the field null
        extension.setActive(null);

        // Create the Extension, which fails.

        restExtensionMockMvc.perform(post("/api/extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extension)))
            .andExpect(status().isBadRequest());

        List<Extension> extensionList = extensionRepository.findAll();
        assertThat(extensionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExtensions() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get all the extensionList
        restExtensionMockMvc.perform(get("/api/extensions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extension.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getExtension() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get the extension
        restExtensionMockMvc.perform(get("/api/extensions/{id}", extension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extension.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.showOrder").value(DEFAULT_SHOW_ORDER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllExtensionsByShowOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get all the extensionList where showOrder equals to DEFAULT_SHOW_ORDER
        defaultExtensionShouldBeFound("showOrder.equals=" + DEFAULT_SHOW_ORDER);

        // Get all the extensionList where showOrder equals to UPDATED_SHOW_ORDER
        defaultExtensionShouldNotBeFound("showOrder.equals=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllExtensionsByShowOrderIsInShouldWork() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get all the extensionList where showOrder in DEFAULT_SHOW_ORDER or UPDATED_SHOW_ORDER
        defaultExtensionShouldBeFound("showOrder.in=" + DEFAULT_SHOW_ORDER + "," + UPDATED_SHOW_ORDER);

        // Get all the extensionList where showOrder equals to UPDATED_SHOW_ORDER
        defaultExtensionShouldNotBeFound("showOrder.in=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllExtensionsByShowOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get all the extensionList where showOrder is not null
        defaultExtensionShouldBeFound("showOrder.specified=true");

        // Get all the extensionList where showOrder is null
        defaultExtensionShouldNotBeFound("showOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtensionsByShowOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get all the extensionList where showOrder greater than or equals to DEFAULT_SHOW_ORDER
        defaultExtensionShouldBeFound("showOrder.greaterOrEqualThan=" + DEFAULT_SHOW_ORDER);

        // Get all the extensionList where showOrder greater than or equals to UPDATED_SHOW_ORDER
        defaultExtensionShouldNotBeFound("showOrder.greaterOrEqualThan=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllExtensionsByShowOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get all the extensionList where showOrder less than or equals to DEFAULT_SHOW_ORDER
        defaultExtensionShouldNotBeFound("showOrder.lessThan=" + DEFAULT_SHOW_ORDER);

        // Get all the extensionList where showOrder less than or equals to UPDATED_SHOW_ORDER
        defaultExtensionShouldBeFound("showOrder.lessThan=" + UPDATED_SHOW_ORDER);
    }


    @Test
    @Transactional
    public void getAllExtensionsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get all the extensionList where active equals to DEFAULT_ACTIVE
        defaultExtensionShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the extensionList where active equals to UPDATED_ACTIVE
        defaultExtensionShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExtensionsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get all the extensionList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultExtensionShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the extensionList where active equals to UPDATED_ACTIVE
        defaultExtensionShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllExtensionsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        extensionRepository.saveAndFlush(extension);

        // Get all the extensionList where active is not null
        defaultExtensionShouldBeFound("active.specified=true");

        // Get all the extensionList where active is null
        defaultExtensionShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtensionsBySkAtExIsEqualToSomething() throws Exception {
        // Initialize the database
        SkAtEx skAtEx = SkAtExResourceIntTest.createEntity(em);
        em.persist(skAtEx);
        em.flush();
        extension.addSkAtEx(skAtEx);
        extensionRepository.saveAndFlush(extension);
        Long skAtExId = skAtEx.getId();

        // Get all the extensionList where skAtEx equals to skAtExId
        defaultExtensionShouldBeFound("skAtExId.equals=" + skAtExId);

        // Get all the extensionList where skAtEx equals to skAtExId + 1
        defaultExtensionShouldNotBeFound("skAtExId.equals=" + (skAtExId + 1));
    }


    @Test
    @Transactional
    public void getAllExtensionsByExtensionKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        ExtensionKey extensionKey = ExtensionKeyResourceIntTest.createEntity(em);
        em.persist(extensionKey);
        em.flush();
        extension.setExtensionKey(extensionKey);
        extensionRepository.saveAndFlush(extension);
        Long extensionKeyId = extensionKey.getId();

        // Get all the extensionList where extensionKey equals to extensionKeyId
        defaultExtensionShouldBeFound("extensionKeyId.equals=" + extensionKeyId);

        // Get all the extensionList where extensionKey equals to extensionKeyId + 1
        defaultExtensionShouldNotBeFound("extensionKeyId.equals=" + (extensionKeyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultExtensionShouldBeFound(String filter) throws Exception {
        restExtensionMockMvc.perform(get("/api/extensions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extension.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultExtensionShouldNotBeFound(String filter) throws Exception {
        restExtensionMockMvc.perform(get("/api/extensions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingExtension() throws Exception {
        // Get the extension
        restExtensionMockMvc.perform(get("/api/extensions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtension() throws Exception {
        // Initialize the database
        extensionService.save(extension);

        int databaseSizeBeforeUpdate = extensionRepository.findAll().size();

        // Update the extension
        Extension updatedExtension = extensionRepository.findOne(extension.getId());
        // Disconnect from session so that the updates on updatedExtension are not directly saved in db
        em.detach(updatedExtension);
        updatedExtension
            .content(UPDATED_CONTENT)
            .description(UPDATED_DESCRIPTION)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);

        restExtensionMockMvc.perform(put("/api/extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtension)))
            .andExpect(status().isOk());

        // Validate the Extension in the database
        List<Extension> extensionList = extensionRepository.findAll();
        assertThat(extensionList).hasSize(databaseSizeBeforeUpdate);
        Extension testExtension = extensionList.get(extensionList.size() - 1);
        assertThat(testExtension.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testExtension.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExtension.getShowOrder()).isEqualTo(UPDATED_SHOW_ORDER);
        assertThat(testExtension.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingExtension() throws Exception {
        int databaseSizeBeforeUpdate = extensionRepository.findAll().size();

        // Create the Extension

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExtensionMockMvc.perform(put("/api/extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extension)))
            .andExpect(status().isCreated());

        // Validate the Extension in the database
        List<Extension> extensionList = extensionRepository.findAll();
        assertThat(extensionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExtension() throws Exception {
        // Initialize the database
        extensionService.save(extension);

        int databaseSizeBeforeDelete = extensionRepository.findAll().size();

        // Get the extension
        restExtensionMockMvc.perform(delete("/api/extensions/{id}", extension.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Extension> extensionList = extensionRepository.findAll();
        assertThat(extensionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Extension.class);
        Extension extension1 = new Extension();
        extension1.setId(1L);
        Extension extension2 = new Extension();
        extension2.setId(extension1.getId());
        assertThat(extension1).isEqualTo(extension2);
        extension2.setId(2L);
        assertThat(extension1).isNotEqualTo(extension2);
        extension1.setId(null);
        assertThat(extension1).isNotEqualTo(extension2);
    }
}
