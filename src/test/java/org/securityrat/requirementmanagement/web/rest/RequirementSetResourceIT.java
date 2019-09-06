package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.RequirementManagementApp;
import org.securityrat.requirementmanagement.config.TestSecurityConfiguration;
import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.securityrat.requirementmanagement.domain.Skeleton;
import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.securityrat.requirementmanagement.repository.RequirementSetRepository;
import org.securityrat.requirementmanagement.service.RequirementSetService;
import org.securityrat.requirementmanagement.web.rest.errors.ExceptionTranslator;
import org.securityrat.requirementmanagement.service.dto.RequirementSetCriteria;
import org.securityrat.requirementmanagement.service.RequirementSetQueryService;

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

/**
 * Integration tests for the {@link RequirementSetResource} REST controller.
 */
@SpringBootTest(classes = {RequirementManagementApp.class, TestSecurityConfiguration.class})
public class RequirementSetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SHOW_ORDER = 1;
    private static final Integer UPDATED_SHOW_ORDER = 2;
    private static final Integer SMALLER_SHOW_ORDER = 1 - 1;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private RequirementSetRepository requirementSetRepository;

    @Autowired
    private RequirementSetService requirementSetService;

    @Autowired
    private RequirementSetQueryService requirementSetQueryService;

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

    private MockMvc restRequirementSetMockMvc;

    private RequirementSet requirementSet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequirementSetResource requirementSetResource = new RequirementSetResource(requirementSetService, requirementSetQueryService);
        this.restRequirementSetMockMvc = MockMvcBuilders.standaloneSetup(requirementSetResource)
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
    public static RequirementSet createEntity(EntityManager em) {
        RequirementSet requirementSet = new RequirementSet()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .showOrder(DEFAULT_SHOW_ORDER)
            .active(DEFAULT_ACTIVE);
        return requirementSet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequirementSet createUpdatedEntity(EntityManager em) {
        RequirementSet requirementSet = new RequirementSet()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);
        return requirementSet;
    }

    @BeforeEach
    public void initTest() {
        requirementSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequirementSet() throws Exception {
        int databaseSizeBeforeCreate = requirementSetRepository.findAll().size();

        // Create the RequirementSet
        restRequirementSetMockMvc.perform(post("/api/requirement-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirementSet)))
            .andExpect(status().isCreated());

        // Validate the RequirementSet in the database
        List<RequirementSet> requirementSetList = requirementSetRepository.findAll();
        assertThat(requirementSetList).hasSize(databaseSizeBeforeCreate + 1);
        RequirementSet testRequirementSet = requirementSetList.get(requirementSetList.size() - 1);
        assertThat(testRequirementSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRequirementSet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequirementSet.getShowOrder()).isEqualTo(DEFAULT_SHOW_ORDER);
        assertThat(testRequirementSet.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createRequirementSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requirementSetRepository.findAll().size();

        // Create the RequirementSet with an existing ID
        requirementSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequirementSetMockMvc.perform(post("/api/requirement-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirementSet)))
            .andExpect(status().isBadRequest());

        // Validate the RequirementSet in the database
        List<RequirementSet> requirementSetList = requirementSetRepository.findAll();
        assertThat(requirementSetList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = requirementSetRepository.findAll().size();
        // set the field null
        requirementSet.setName(null);

        // Create the RequirementSet, which fails.

        restRequirementSetMockMvc.perform(post("/api/requirement-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirementSet)))
            .andExpect(status().isBadRequest());

        List<RequirementSet> requirementSetList = requirementSetRepository.findAll();
        assertThat(requirementSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = requirementSetRepository.findAll().size();
        // set the field null
        requirementSet.setActive(null);

        // Create the RequirementSet, which fails.

        restRequirementSetMockMvc.perform(post("/api/requirement-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirementSet)))
            .andExpect(status().isBadRequest());

        List<RequirementSet> requirementSetList = requirementSetRepository.findAll();
        assertThat(requirementSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequirementSets() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList
        restRequirementSetMockMvc.perform(get("/api/requirement-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requirementSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getRequirementSet() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get the requirementSet
        restRequirementSetMockMvc.perform(get("/api/requirement-sets/{id}", requirementSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requirementSet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.showOrder").value(DEFAULT_SHOW_ORDER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where name equals to DEFAULT_NAME
        defaultRequirementSetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the requirementSetList where name equals to UPDATED_NAME
        defaultRequirementSetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRequirementSetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the requirementSetList where name equals to UPDATED_NAME
        defaultRequirementSetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where name is not null
        defaultRequirementSetShouldBeFound("name.specified=true");

        // Get all the requirementSetList where name is null
        defaultRequirementSetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByShowOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where showOrder equals to DEFAULT_SHOW_ORDER
        defaultRequirementSetShouldBeFound("showOrder.equals=" + DEFAULT_SHOW_ORDER);

        // Get all the requirementSetList where showOrder equals to UPDATED_SHOW_ORDER
        defaultRequirementSetShouldNotBeFound("showOrder.equals=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByShowOrderIsInShouldWork() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where showOrder in DEFAULT_SHOW_ORDER or UPDATED_SHOW_ORDER
        defaultRequirementSetShouldBeFound("showOrder.in=" + DEFAULT_SHOW_ORDER + "," + UPDATED_SHOW_ORDER);

        // Get all the requirementSetList where showOrder equals to UPDATED_SHOW_ORDER
        defaultRequirementSetShouldNotBeFound("showOrder.in=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByShowOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where showOrder is not null
        defaultRequirementSetShouldBeFound("showOrder.specified=true");

        // Get all the requirementSetList where showOrder is null
        defaultRequirementSetShouldNotBeFound("showOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByShowOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where showOrder is greater than or equal to DEFAULT_SHOW_ORDER
        defaultRequirementSetShouldBeFound("showOrder.greaterThanOrEqual=" + DEFAULT_SHOW_ORDER);

        // Get all the requirementSetList where showOrder is greater than or equal to UPDATED_SHOW_ORDER
        defaultRequirementSetShouldNotBeFound("showOrder.greaterThanOrEqual=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByShowOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where showOrder is less than or equal to DEFAULT_SHOW_ORDER
        defaultRequirementSetShouldBeFound("showOrder.lessThanOrEqual=" + DEFAULT_SHOW_ORDER);

        // Get all the requirementSetList where showOrder is less than or equal to SMALLER_SHOW_ORDER
        defaultRequirementSetShouldNotBeFound("showOrder.lessThanOrEqual=" + SMALLER_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByShowOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where showOrder is less than DEFAULT_SHOW_ORDER
        defaultRequirementSetShouldNotBeFound("showOrder.lessThan=" + DEFAULT_SHOW_ORDER);

        // Get all the requirementSetList where showOrder is less than UPDATED_SHOW_ORDER
        defaultRequirementSetShouldBeFound("showOrder.lessThan=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByShowOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where showOrder is greater than DEFAULT_SHOW_ORDER
        defaultRequirementSetShouldNotBeFound("showOrder.greaterThan=" + DEFAULT_SHOW_ORDER);

        // Get all the requirementSetList where showOrder is greater than SMALLER_SHOW_ORDER
        defaultRequirementSetShouldBeFound("showOrder.greaterThan=" + SMALLER_SHOW_ORDER);
    }


    @Test
    @Transactional
    public void getAllRequirementSetsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where active equals to DEFAULT_ACTIVE
        defaultRequirementSetShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the requirementSetList where active equals to UPDATED_ACTIVE
        defaultRequirementSetShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultRequirementSetShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the requirementSetList where active equals to UPDATED_ACTIVE
        defaultRequirementSetShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);

        // Get all the requirementSetList where active is not null
        defaultRequirementSetShouldBeFound("active.specified=true");

        // Get all the requirementSetList where active is null
        defaultRequirementSetShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllRequirementSetsByAttributeKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);
        AttributeKey attributeKey = AttributeKeyResourceIT.createEntity(em);
        em.persist(attributeKey);
        em.flush();
        requirementSet.addAttributeKey(attributeKey);
        requirementSetRepository.saveAndFlush(requirementSet);
        Long attributeKeyId = attributeKey.getId();

        // Get all the requirementSetList where attributeKey equals to attributeKeyId
        defaultRequirementSetShouldBeFound("attributeKeyId.equals=" + attributeKeyId);

        // Get all the requirementSetList where attributeKey equals to attributeKeyId + 1
        defaultRequirementSetShouldNotBeFound("attributeKeyId.equals=" + (attributeKeyId + 1));
    }


    @Test
    @Transactional
    public void getAllRequirementSetsBySkeletonIsEqualToSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);
        Skeleton skeleton = SkeletonResourceIT.createEntity(em);
        em.persist(skeleton);
        em.flush();
        requirementSet.addSkeleton(skeleton);
        requirementSetRepository.saveAndFlush(requirementSet);
        Long skeletonId = skeleton.getId();

        // Get all the requirementSetList where skeleton equals to skeletonId
        defaultRequirementSetShouldBeFound("skeletonId.equals=" + skeletonId);

        // Get all the requirementSetList where skeleton equals to skeletonId + 1
        defaultRequirementSetShouldNotBeFound("skeletonId.equals=" + (skeletonId + 1));
    }


    @Test
    @Transactional
    public void getAllRequirementSetsByExtensionKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        requirementSetRepository.saveAndFlush(requirementSet);
        ExtensionKey extensionKey = ExtensionKeyResourceIT.createEntity(em);
        em.persist(extensionKey);
        em.flush();
        requirementSet.addExtensionKey(extensionKey);
        requirementSetRepository.saveAndFlush(requirementSet);
        Long extensionKeyId = extensionKey.getId();

        // Get all the requirementSetList where extensionKey equals to extensionKeyId
        defaultRequirementSetShouldBeFound("extensionKeyId.equals=" + extensionKeyId);

        // Get all the requirementSetList where extensionKey equals to extensionKeyId + 1
        defaultRequirementSetShouldNotBeFound("extensionKeyId.equals=" + (extensionKeyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRequirementSetShouldBeFound(String filter) throws Exception {
        restRequirementSetMockMvc.perform(get("/api/requirement-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requirementSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restRequirementSetMockMvc.perform(get("/api/requirement-sets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRequirementSetShouldNotBeFound(String filter) throws Exception {
        restRequirementSetMockMvc.perform(get("/api/requirement-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRequirementSetMockMvc.perform(get("/api/requirement-sets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRequirementSet() throws Exception {
        // Get the requirementSet
        restRequirementSetMockMvc.perform(get("/api/requirement-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequirementSet() throws Exception {
        // Initialize the database
        requirementSetService.save(requirementSet);

        int databaseSizeBeforeUpdate = requirementSetRepository.findAll().size();

        // Update the requirementSet
        RequirementSet updatedRequirementSet = requirementSetRepository.findById(requirementSet.getId()).get();
        // Disconnect from session so that the updates on updatedRequirementSet are not directly saved in db
        em.detach(updatedRequirementSet);
        updatedRequirementSet
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);

        restRequirementSetMockMvc.perform(put("/api/requirement-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequirementSet)))
            .andExpect(status().isOk());

        // Validate the RequirementSet in the database
        List<RequirementSet> requirementSetList = requirementSetRepository.findAll();
        assertThat(requirementSetList).hasSize(databaseSizeBeforeUpdate);
        RequirementSet testRequirementSet = requirementSetList.get(requirementSetList.size() - 1);
        assertThat(testRequirementSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRequirementSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequirementSet.getShowOrder()).isEqualTo(UPDATED_SHOW_ORDER);
        assertThat(testRequirementSet.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingRequirementSet() throws Exception {
        int databaseSizeBeforeUpdate = requirementSetRepository.findAll().size();

        // Create the RequirementSet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequirementSetMockMvc.perform(put("/api/requirement-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirementSet)))
            .andExpect(status().isBadRequest());

        // Validate the RequirementSet in the database
        List<RequirementSet> requirementSetList = requirementSetRepository.findAll();
        assertThat(requirementSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequirementSet() throws Exception {
        // Initialize the database
        requirementSetService.save(requirementSet);

        int databaseSizeBeforeDelete = requirementSetRepository.findAll().size();

        // Delete the requirementSet
        restRequirementSetMockMvc.perform(delete("/api/requirement-sets/{id}", requirementSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequirementSet> requirementSetList = requirementSetRepository.findAll();
        assertThat(requirementSetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequirementSet.class);
        RequirementSet requirementSet1 = new RequirementSet();
        requirementSet1.setId(1L);
        RequirementSet requirementSet2 = new RequirementSet();
        requirementSet2.setId(requirementSet1.getId());
        assertThat(requirementSet1).isEqualTo(requirementSet2);
        requirementSet2.setId(2L);
        assertThat(requirementSet1).isNotEqualTo(requirementSet2);
        requirementSet1.setId(null);
        assertThat(requirementSet1).isNotEqualTo(requirementSet2);
    }
}
