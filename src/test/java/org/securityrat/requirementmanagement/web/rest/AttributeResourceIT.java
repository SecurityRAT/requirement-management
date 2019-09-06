package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.RequirementManagementApp;
import org.securityrat.requirementmanagement.config.TestSecurityConfiguration;
import org.securityrat.requirementmanagement.domain.Attribute;
import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.securityrat.requirementmanagement.domain.Attribute;
import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.securityrat.requirementmanagement.repository.AttributeRepository;
import org.securityrat.requirementmanagement.service.AttributeService;
import org.securityrat.requirementmanagement.web.rest.errors.ExceptionTranslator;
import org.securityrat.requirementmanagement.service.dto.AttributeCriteria;
import org.securityrat.requirementmanagement.service.AttributeQueryService;

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
 * Integration tests for the {@link AttributeResource} REST controller.
 */
@SpringBootTest(classes = {RequirementManagementApp.class, TestSecurityConfiguration.class})
public class AttributeResourceIT {

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
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private AttributeQueryService attributeQueryService;

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

    private MockMvc restAttributeMockMvc;

    private Attribute attribute;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttributeResource attributeResource = new AttributeResource(attributeService, attributeQueryService);
        this.restAttributeMockMvc = MockMvcBuilders.standaloneSetup(attributeResource)
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
    public static Attribute createEntity(EntityManager em) {
        Attribute attribute = new Attribute()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .showOrder(DEFAULT_SHOW_ORDER)
            .active(DEFAULT_ACTIVE);
        return attribute;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attribute createUpdatedEntity(EntityManager em) {
        Attribute attribute = new Attribute()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);
        return attribute;
    }

    @BeforeEach
    public void initTest() {
        attribute = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttribute() throws Exception {
        int databaseSizeBeforeCreate = attributeRepository.findAll().size();

        // Create the Attribute
        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isCreated());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeCreate + 1);
        Attribute testAttribute = attributeList.get(attributeList.size() - 1);
        assertThat(testAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttribute.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAttribute.getShowOrder()).isEqualTo(DEFAULT_SHOW_ORDER);
        assertThat(testAttribute.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createAttributeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributeRepository.findAll().size();

        // Create the Attribute with an existing ID
        attribute.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setName(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setActive(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttributes() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getAttribute() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", attribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attribute.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.showOrder").value(DEFAULT_SHOW_ORDER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllAttributesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name equals to DEFAULT_NAME
        defaultAttributeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the attributeList where name equals to UPDATED_NAME
        defaultAttributeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttributesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAttributeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the attributeList where name equals to UPDATED_NAME
        defaultAttributeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttributesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name is not null
        defaultAttributeShouldBeFound("name.specified=true");

        // Get all the attributeList where name is null
        defaultAttributeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByShowOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where showOrder equals to DEFAULT_SHOW_ORDER
        defaultAttributeShouldBeFound("showOrder.equals=" + DEFAULT_SHOW_ORDER);

        // Get all the attributeList where showOrder equals to UPDATED_SHOW_ORDER
        defaultAttributeShouldNotBeFound("showOrder.equals=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesByShowOrderIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where showOrder in DEFAULT_SHOW_ORDER or UPDATED_SHOW_ORDER
        defaultAttributeShouldBeFound("showOrder.in=" + DEFAULT_SHOW_ORDER + "," + UPDATED_SHOW_ORDER);

        // Get all the attributeList where showOrder equals to UPDATED_SHOW_ORDER
        defaultAttributeShouldNotBeFound("showOrder.in=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesByShowOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where showOrder is not null
        defaultAttributeShouldBeFound("showOrder.specified=true");

        // Get all the attributeList where showOrder is null
        defaultAttributeShouldNotBeFound("showOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByShowOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where showOrder is greater than or equal to DEFAULT_SHOW_ORDER
        defaultAttributeShouldBeFound("showOrder.greaterThanOrEqual=" + DEFAULT_SHOW_ORDER);

        // Get all the attributeList where showOrder is greater than or equal to UPDATED_SHOW_ORDER
        defaultAttributeShouldNotBeFound("showOrder.greaterThanOrEqual=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesByShowOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where showOrder is less than or equal to DEFAULT_SHOW_ORDER
        defaultAttributeShouldBeFound("showOrder.lessThanOrEqual=" + DEFAULT_SHOW_ORDER);

        // Get all the attributeList where showOrder is less than or equal to SMALLER_SHOW_ORDER
        defaultAttributeShouldNotBeFound("showOrder.lessThanOrEqual=" + SMALLER_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesByShowOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where showOrder is less than DEFAULT_SHOW_ORDER
        defaultAttributeShouldNotBeFound("showOrder.lessThan=" + DEFAULT_SHOW_ORDER);

        // Get all the attributeList where showOrder is less than UPDATED_SHOW_ORDER
        defaultAttributeShouldBeFound("showOrder.lessThan=" + UPDATED_SHOW_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesByShowOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where showOrder is greater than DEFAULT_SHOW_ORDER
        defaultAttributeShouldNotBeFound("showOrder.greaterThan=" + DEFAULT_SHOW_ORDER);

        // Get all the attributeList where showOrder is greater than SMALLER_SHOW_ORDER
        defaultAttributeShouldBeFound("showOrder.greaterThan=" + SMALLER_SHOW_ORDER);
    }


    @Test
    @Transactional
    public void getAllAttributesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where active equals to DEFAULT_ACTIVE
        defaultAttributeShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the attributeList where active equals to UPDATED_ACTIVE
        defaultAttributeShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllAttributesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultAttributeShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the attributeList where active equals to UPDATED_ACTIVE
        defaultAttributeShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllAttributesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where active is not null
        defaultAttributeShouldBeFound("active.specified=true");

        // Get all the attributeList where active is null
        defaultAttributeShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesBySkAtExIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);
        SkAtEx skAtEx = SkAtExResourceIT.createEntity(em);
        em.persist(skAtEx);
        em.flush();
        attribute.addSkAtEx(skAtEx);
        attributeRepository.saveAndFlush(attribute);
        Long skAtExId = skAtEx.getId();

        // Get all the attributeList where skAtEx equals to skAtExId
        defaultAttributeShouldBeFound("skAtExId.equals=" + skAtExId);

        // Get all the attributeList where skAtEx equals to skAtExId + 1
        defaultAttributeShouldNotBeFound("skAtExId.equals=" + (skAtExId + 1));
    }


    @Test
    @Transactional
    public void getAllAttributesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);
        Attribute parent = AttributeResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        attribute.setParent(parent);
        attributeRepository.saveAndFlush(attribute);
        Long parentId = parent.getId();

        // Get all the attributeList where parent equals to parentId
        defaultAttributeShouldBeFound("parentId.equals=" + parentId);

        // Get all the attributeList where parent equals to parentId + 1
        defaultAttributeShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllAttributesByAttributeKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);
        AttributeKey attributeKey = AttributeKeyResourceIT.createEntity(em);
        em.persist(attributeKey);
        em.flush();
        attribute.setAttributeKey(attributeKey);
        attributeRepository.saveAndFlush(attribute);
        Long attributeKeyId = attributeKey.getId();

        // Get all the attributeList where attributeKey equals to attributeKeyId
        defaultAttributeShouldBeFound("attributeKeyId.equals=" + attributeKeyId);

        // Get all the attributeList where attributeKey equals to attributeKeyId + 1
        defaultAttributeShouldNotBeFound("attributeKeyId.equals=" + (attributeKeyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttributeShouldBeFound(String filter) throws Exception {
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].showOrder").value(hasItem(DEFAULT_SHOW_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restAttributeMockMvc.perform(get("/api/attributes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttributeShouldNotBeFound(String filter) throws Exception {
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttributeMockMvc.perform(get("/api/attributes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAttribute() throws Exception {
        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);

        int databaseSizeBeforeUpdate = attributeRepository.findAll().size();

        // Update the attribute
        Attribute updatedAttribute = attributeRepository.findById(attribute.getId()).get();
        // Disconnect from session so that the updates on updatedAttribute are not directly saved in db
        em.detach(updatedAttribute);
        updatedAttribute
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .showOrder(UPDATED_SHOW_ORDER)
            .active(UPDATED_ACTIVE);

        restAttributeMockMvc.perform(put("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttribute)))
            .andExpect(status().isOk());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeUpdate);
        Attribute testAttribute = attributeList.get(attributeList.size() - 1);
        assertThat(testAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttribute.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAttribute.getShowOrder()).isEqualTo(UPDATED_SHOW_ORDER);
        assertThat(testAttribute.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingAttribute() throws Exception {
        int databaseSizeBeforeUpdate = attributeRepository.findAll().size();

        // Create the Attribute

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeMockMvc.perform(put("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);

        int databaseSizeBeforeDelete = attributeRepository.findAll().size();

        // Delete the attribute
        restAttributeMockMvc.perform(delete("/api/attributes/{id}", attribute.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attribute.class);
        Attribute attribute1 = new Attribute();
        attribute1.setId(1L);
        Attribute attribute2 = new Attribute();
        attribute2.setId(attribute1.getId());
        assertThat(attribute1).isEqualTo(attribute2);
        attribute2.setId(2L);
        assertThat(attribute1).isNotEqualTo(attribute2);
        attribute1.setId(null);
        assertThat(attribute1).isNotEqualTo(attribute2);
    }
}
