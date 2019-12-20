package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.RequirementManagementApp;
import org.securityrat.requirementmanagement.config.TestSecurityConfiguration;
import org.securityrat.requirementmanagement.domain.Skeleton;
import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.securityrat.requirementmanagement.repository.SkeletonRepository;
import org.securityrat.requirementmanagement.service.SkeletonService;
import org.securityrat.requirementmanagement.web.rest.errors.ExceptionTranslator;
import org.securityrat.requirementmanagement.service.dto.SkeletonCriteria;
import org.securityrat.requirementmanagement.service.SkeletonQueryService;

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
 * Integration tests for the {@link SkeletonResource} REST controller.
 */
@SpringBootTest(classes = {RequirementManagementApp.class, TestSecurityConfiguration.class})
public class SkeletonResourceIT {

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
    private SkeletonRepository skeletonRepository;

    @Autowired
    private SkeletonService skeletonService;

    @Autowired
    private SkeletonQueryService skeletonQueryService;

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

    private MockMvc restSkeletonMockMvc;

    private Skeleton skeleton;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SkeletonResource skeletonResource = new SkeletonResource(skeletonService, skeletonQueryService);
        this.restSkeletonMockMvc = MockMvcBuilders.standaloneSetup(skeletonResource)
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
    public static Skeleton createEntity(EntityManager em) {
        Skeleton skeleton = new Skeleton()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .showOrder(DEFAULT_SHOW_ORDER)
            .active(DEFAULT_ACTIVE);
        return skeleton;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skeleton createUpdatedEntity(EntityManager em) {
        Skeleton skeleton = new Skeleton()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);
        return skeleton;
    }

    @BeforeEach
    public void initTest() {
        skeleton = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkeleton() throws Exception {
        int databaseSizeBeforeCreate = skeletonRepository.findAll().size();

        // Create the Skeleton
        restSkeletonMockMvc.perform(post("/api/skeletons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skeleton)))
            .andExpect(status().isCreated());

        // Validate the Skeleton in the database
        List<Skeleton> skeletonList = skeletonRepository.findAll();
        assertThat(skeletonList).hasSize(databaseSizeBeforeCreate + 1);
        Skeleton testSkeleton = skeletonList.get(skeletonList.size() - 1);
        assertThat(testSkeleton.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSkeleton.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSkeleton.getShowOrder()).isEqualTo(DEFAULT_SHOW_ORDER);
        assertThat(testSkeleton.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createSkeletonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skeletonRepository.findAll().size();

        // Create the Skeleton with an existing ID
        skeleton.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkeletonMockMvc.perform(post("/api/skeletons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skeleton)))
            .andExpect(status().isBadRequest());

        // Validate the Skeleton in the database
        List<Skeleton> skeletonList = skeletonRepository.findAll();
        assertThat(skeletonList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = skeletonRepository.findAll().size();
        // set the field null
        skeleton.setName(null);

        // Create the Skeleton, which fails.

        restSkeletonMockMvc.perform(post("/api/skeletons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skeleton)))
            .andExpect(status().isBadRequest());

        List<Skeleton> skeletonList = skeletonRepository.findAll();
        assertThat(skeletonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = skeletonRepository.findAll().size();
        // set the field null
        skeleton.setActive(null);

        // Create the Skeleton, which fails.

        restSkeletonMockMvc.perform(post("/api/skeletons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skeleton)))
            .andExpect(status().isBadRequest());

        List<Skeleton> skeletonList = skeletonRepository.findAll();
        assertThat(skeletonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSkeletons() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList
        restSkeletonMockMvc.perform(get("/api/skeletons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skeleton.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSkeleton() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get the skeleton
        restSkeletonMockMvc.perform(get("/api/skeletons/{id}", skeleton.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(skeleton.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.showOrder").value(DEFAULT_SHOW_ORDER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getSkeletonsByIdFiltering() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        Long id = skeleton.getId();

        defaultSkeletonShouldBeFound("id.equals=" + id);
        defaultSkeletonShouldNotBeFound("id.notEquals=" + id);

        defaultSkeletonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSkeletonShouldNotBeFound("id.greaterThan=" + id);

        defaultSkeletonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSkeletonShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSkeletonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where name equals to DEFAULT_NAME
        defaultSkeletonShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the skeletonList where name equals to UPDATED_NAME
        defaultSkeletonShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where name not equals to DEFAULT_NAME
        defaultSkeletonShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the skeletonList where name not equals to UPDATED_NAME
        defaultSkeletonShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSkeletonShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the skeletonList where name equals to UPDATED_NAME
        defaultSkeletonShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where name is not null
        defaultSkeletonShouldBeFound("name.specified=true");

        // Get all the skeletonList where name is null
        defaultSkeletonShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllSkeletonsByNameContainsSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where name contains DEFAULT_NAME
        defaultSkeletonShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the skeletonList where name contains UPDATED_NAME
        defaultSkeletonShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where name does not contain DEFAULT_NAME
        defaultSkeletonShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the skeletonList where name does not contain UPDATED_NAME
        defaultSkeletonShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllSkeletonsByShowOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where showOrder equals to DEFAULT_SHOW_ORDER
        defaultSkeletonShouldBeFound("showOrder.equals=" + DEFAULT_SHOW_ORDER);

        // Get all the skeletonList where showOrder equals to UPDATED_SHOW_ORDER
        defaultSkeletonShouldNotBeFound("showOrder.equals=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByShowOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where showOrder not equals to DEFAULT_SHOW_ORDER
        defaultSkeletonShouldNotBeFound("showOrder.notEquals=" + DEFAULT_SHOW_ORDER);

        // Get all the skeletonList where showOrder not equals to UPDATED_SHOW_ORDER
        defaultSkeletonShouldBeFound("showOrder.notEquals=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByShowOrderIsInShouldWork() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where showOrder in DEFAULT_SHOW_ORDER or UPDATED_SHOW_ORDER
        defaultSkeletonShouldBeFound("showOrder.in=" + DEFAULT_SHOW_ORDER + "," + UPDATED_SHOW_ORDER);

        // Get all the skeletonList where showOrder equals to UPDATED_SHOW_ORDER
        defaultSkeletonShouldNotBeFound("showOrder.in=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByShowOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where showOrder is not null
        defaultSkeletonShouldBeFound("showOrder.specified=true");

        // Get all the skeletonList where showOrder is null
        defaultSkeletonShouldNotBeFound("showOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllSkeletonsByShowOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where showOrder is greater than or equal to DEFAULT_SHOW_ORDER
        defaultSkeletonShouldBeFound("showOrder.greaterThanOrEqual=" + DEFAULT_SHOW_ORDER);

        // Get all the skeletonList where showOrder is greater than or equal to UPDATED_SHOW_ORDER
        defaultSkeletonShouldNotBeFound("showOrder.greaterThanOrEqual=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByShowOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where showOrder is less than or equal to DEFAULT_SHOW_ORDER
        defaultSkeletonShouldBeFound("showOrder.lessThanOrEqual=" + DEFAULT_SHOW_ORDER);

        // Get all the skeletonList where showOrder is less than or equal to SMALLER_SHOW_ORDER
        defaultSkeletonShouldNotBeFound("showOrder.lessThanOrEqual=" + SMALLER_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByShowOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where showOrder is less than DEFAULT_SHOW_ORDER
        defaultSkeletonShouldNotBeFound("showOrder.lessThan=" + DEFAULT_SHOW_ORDER);

        // Get all the skeletonList where showOrder is less than UPDATED_SHOW_ORDER
        defaultSkeletonShouldBeFound("showOrder.lessThan=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByShowOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where showOrder is greater than DEFAULT_SHOW_ORDER
        defaultSkeletonShouldNotBeFound("showOrder.greaterThan=" + DEFAULT_SHOW_ORDER);

        // Get all the skeletonList where showOrder is greater than SMALLER_SHOW_ORDER
        defaultSkeletonShouldBeFound("showOrder.greaterThan=" + SMALLER_SHOW_ORDER);
    }


    @Test
    @Transactional
    public void getAllSkeletonsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where active equals to DEFAULT_ACTIVE
        defaultSkeletonShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the skeletonList where active equals to UPDATED_ACTIVE
        defaultSkeletonShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where active not equals to DEFAULT_ACTIVE
        defaultSkeletonShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the skeletonList where active not equals to UPDATED_ACTIVE
        defaultSkeletonShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultSkeletonShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the skeletonList where active equals to UPDATED_ACTIVE
        defaultSkeletonShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllSkeletonsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);

        // Get all the skeletonList where active is not null
        defaultSkeletonShouldBeFound("active.specified=true");

        // Get all the skeletonList where active is null
        defaultSkeletonShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllSkeletonsBySkAtExIsEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);
        SkAtEx skAtEx = SkAtExResourceIT.createEntity(em);
        em.persist(skAtEx);
        em.flush();
        skeleton.addSkAtEx(skAtEx);
        skeletonRepository.saveAndFlush(skeleton);
        Long skAtExId = skAtEx.getId();

        // Get all the skeletonList where skAtEx equals to skAtExId
        defaultSkeletonShouldBeFound("skAtExId.equals=" + skAtExId);

        // Get all the skeletonList where skAtEx equals to skAtExId + 1
        defaultSkeletonShouldNotBeFound("skAtExId.equals=" + (skAtExId + 1));
    }


    @Test
    @Transactional
    public void getAllSkeletonsByRequirementSetIsEqualToSomething() throws Exception {
        // Initialize the database
        skeletonRepository.saveAndFlush(skeleton);
        RequirementSet requirementSet = RequirementSetResourceIT.createEntity(em);
        em.persist(requirementSet);
        em.flush();
        skeleton.setRequirementSet(requirementSet);
        skeletonRepository.saveAndFlush(skeleton);
        Long requirementSetId = requirementSet.getId();

        // Get all the skeletonList where requirementSet equals to requirementSetId
        defaultSkeletonShouldBeFound("requirementSetId.equals=" + requirementSetId);

        // Get all the skeletonList where requirementSet equals to requirementSetId + 1
        defaultSkeletonShouldNotBeFound("requirementSetId.equals=" + (requirementSetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSkeletonShouldBeFound(String filter) throws Exception {
        restSkeletonMockMvc.perform(get("/api/skeletons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skeleton.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restSkeletonMockMvc.perform(get("/api/skeletons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSkeletonShouldNotBeFound(String filter) throws Exception {
        restSkeletonMockMvc.perform(get("/api/skeletons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSkeletonMockMvc.perform(get("/api/skeletons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSkeleton() throws Exception {
        // Get the skeleton
        restSkeletonMockMvc.perform(get("/api/skeletons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkeleton() throws Exception {
        // Initialize the database
        skeletonService.save(skeleton);

        int databaseSizeBeforeUpdate = skeletonRepository.findAll().size();

        // Update the skeleton
        Skeleton updatedSkeleton = skeletonRepository.findById(skeleton.getId()).get();
        // Disconnect from session so that the updates on updatedSkeleton are not directly saved in db
        em.detach(updatedSkeleton);
        updatedSkeleton
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);

        restSkeletonMockMvc.perform(put("/api/skeletons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSkeleton)))
            .andExpect(status().isOk());

        // Validate the Skeleton in the database
        List<Skeleton> skeletonList = skeletonRepository.findAll();
        assertThat(skeletonList).hasSize(databaseSizeBeforeUpdate);
        Skeleton testSkeleton = skeletonList.get(skeletonList.size() - 1);
        assertThat(testSkeleton.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSkeleton.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSkeleton.getShowOrder()).isEqualTo(UPDATED_SHOW_ORDER);
        assertThat(testSkeleton.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingSkeleton() throws Exception {
        int databaseSizeBeforeUpdate = skeletonRepository.findAll().size();

        // Create the Skeleton

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkeletonMockMvc.perform(put("/api/skeletons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skeleton)))
            .andExpect(status().isBadRequest());

        // Validate the Skeleton in the database
        List<Skeleton> skeletonList = skeletonRepository.findAll();
        assertThat(skeletonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSkeleton() throws Exception {
        // Initialize the database
        skeletonService.save(skeleton);

        int databaseSizeBeforeDelete = skeletonRepository.findAll().size();

        // Delete the skeleton
        restSkeletonMockMvc.perform(delete("/api/skeletons/{id}", skeleton.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Skeleton> skeletonList = skeletonRepository.findAll();
        assertThat(skeletonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
