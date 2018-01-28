package org.securityrat.requirementmanagement.web.rest;

import org.securityrat.requirementmanagement.RequirementManagementApp;

import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.securityrat.requirementmanagement.domain.Skeleton;
import org.securityrat.requirementmanagement.domain.Attribute;
import org.securityrat.requirementmanagement.domain.Extension;
import org.securityrat.requirementmanagement.repository.SkAtExRepository;
import org.securityrat.requirementmanagement.service.SkAtExService;
import org.securityrat.requirementmanagement.web.rest.errors.ExceptionTranslator;
import org.securityrat.requirementmanagement.service.dto.SkAtExCriteria;
import org.securityrat.requirementmanagement.service.SkAtExQueryService;

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

import javax.persistence.EntityManager;
import java.util.List;

import static org.securityrat.requirementmanagement.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SkAtExResource REST controller.
 *
 * @see SkAtExResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RequirementManagementApp.class)
public class SkAtExResourceIntTest {

    @Autowired
    private SkAtExRepository skAtExRepository;

    @Autowired
    private SkAtExService skAtExService;

    @Autowired
    private SkAtExQueryService skAtExQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSkAtExMockMvc;

    private SkAtEx skAtEx;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SkAtExResource skAtExResource = new SkAtExResource(skAtExService, skAtExQueryService);
        this.restSkAtExMockMvc = MockMvcBuilders.standaloneSetup(skAtExResource)
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
    public static SkAtEx createEntity(EntityManager em) {
        SkAtEx skAtEx = new SkAtEx();
        return skAtEx;
    }

    @Before
    public void initTest() {
        skAtEx = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkAtEx() throws Exception {
        int databaseSizeBeforeCreate = skAtExRepository.findAll().size();

        // Create the SkAtEx
        restSkAtExMockMvc.perform(post("/api/sk-at-exes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skAtEx)))
            .andExpect(status().isCreated());

        // Validate the SkAtEx in the database
        List<SkAtEx> skAtExList = skAtExRepository.findAll();
        assertThat(skAtExList).hasSize(databaseSizeBeforeCreate + 1);
        SkAtEx testSkAtEx = skAtExList.get(skAtExList.size() - 1);
    }

    @Test
    @Transactional
    public void createSkAtExWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skAtExRepository.findAll().size();

        // Create the SkAtEx with an existing ID
        skAtEx.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkAtExMockMvc.perform(post("/api/sk-at-exes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skAtEx)))
            .andExpect(status().isBadRequest());

        // Validate the SkAtEx in the database
        List<SkAtEx> skAtExList = skAtExRepository.findAll();
        assertThat(skAtExList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSkAtExes() throws Exception {
        // Initialize the database
        skAtExRepository.saveAndFlush(skAtEx);

        // Get all the skAtExList
        restSkAtExMockMvc.perform(get("/api/sk-at-exes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skAtEx.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSkAtEx() throws Exception {
        // Initialize the database
        skAtExRepository.saveAndFlush(skAtEx);

        // Get the skAtEx
        restSkAtExMockMvc.perform(get("/api/sk-at-exes/{id}", skAtEx.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(skAtEx.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllSkAtExesBySkeletonIsEqualToSomething() throws Exception {
        // Initialize the database
        Skeleton skeleton = SkeletonResourceIntTest.createEntity(em);
        em.persist(skeleton);
        em.flush();
        skAtEx.setSkeleton(skeleton);
        skAtExRepository.saveAndFlush(skAtEx);
        Long skeletonId = skeleton.getId();

        // Get all the skAtExList where skeleton equals to skeletonId
        defaultSkAtExShouldBeFound("skeletonId.equals=" + skeletonId);

        // Get all the skAtExList where skeleton equals to skeletonId + 1
        defaultSkAtExShouldNotBeFound("skeletonId.equals=" + (skeletonId + 1));
    }


    @Test
    @Transactional
    public void getAllSkAtExesByAttributeIsEqualToSomething() throws Exception {
        // Initialize the database
        Attribute attribute = AttributeResourceIntTest.createEntity(em);
        em.persist(attribute);
        em.flush();
        skAtEx.setAttribute(attribute);
        skAtExRepository.saveAndFlush(skAtEx);
        Long attributeId = attribute.getId();

        // Get all the skAtExList where attribute equals to attributeId
        defaultSkAtExShouldBeFound("attributeId.equals=" + attributeId);

        // Get all the skAtExList where attribute equals to attributeId + 1
        defaultSkAtExShouldNotBeFound("attributeId.equals=" + (attributeId + 1));
    }


    @Test
    @Transactional
    public void getAllSkAtExesByExtensionIsEqualToSomething() throws Exception {
        // Initialize the database
        Extension extension = ExtensionResourceIntTest.createEntity(em);
        em.persist(extension);
        em.flush();
        skAtEx.setExtension(extension);
        skAtExRepository.saveAndFlush(skAtEx);
        Long extensionId = extension.getId();

        // Get all the skAtExList where extension equals to extensionId
        defaultSkAtExShouldBeFound("extensionId.equals=" + extensionId);

        // Get all the skAtExList where extension equals to extensionId + 1
        defaultSkAtExShouldNotBeFound("extensionId.equals=" + (extensionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSkAtExShouldBeFound(String filter) throws Exception {
        restSkAtExMockMvc.perform(get("/api/sk-at-exes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skAtEx.getId().intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSkAtExShouldNotBeFound(String filter) throws Exception {
        restSkAtExMockMvc.perform(get("/api/sk-at-exes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSkAtEx() throws Exception {
        // Get the skAtEx
        restSkAtExMockMvc.perform(get("/api/sk-at-exes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkAtEx() throws Exception {
        // Initialize the database
        skAtExService.save(skAtEx);

        int databaseSizeBeforeUpdate = skAtExRepository.findAll().size();

        // Update the skAtEx
        SkAtEx updatedSkAtEx = skAtExRepository.findOne(skAtEx.getId());
        // Disconnect from session so that the updates on updatedSkAtEx are not directly saved in db
        em.detach(updatedSkAtEx);

        restSkAtExMockMvc.perform(put("/api/sk-at-exes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSkAtEx)))
            .andExpect(status().isOk());

        // Validate the SkAtEx in the database
        List<SkAtEx> skAtExList = skAtExRepository.findAll();
        assertThat(skAtExList).hasSize(databaseSizeBeforeUpdate);
        SkAtEx testSkAtEx = skAtExList.get(skAtExList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingSkAtEx() throws Exception {
        int databaseSizeBeforeUpdate = skAtExRepository.findAll().size();

        // Create the SkAtEx

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSkAtExMockMvc.perform(put("/api/sk-at-exes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skAtEx)))
            .andExpect(status().isCreated());

        // Validate the SkAtEx in the database
        List<SkAtEx> skAtExList = skAtExRepository.findAll();
        assertThat(skAtExList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSkAtEx() throws Exception {
        // Initialize the database
        skAtExService.save(skAtEx);

        int databaseSizeBeforeDelete = skAtExRepository.findAll().size();

        // Get the skAtEx
        restSkAtExMockMvc.perform(delete("/api/sk-at-exes/{id}", skAtEx.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SkAtEx> skAtExList = skAtExRepository.findAll();
        assertThat(skAtExList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkAtEx.class);
        SkAtEx skAtEx1 = new SkAtEx();
        skAtEx1.setId(1L);
        SkAtEx skAtEx2 = new SkAtEx();
        skAtEx2.setId(skAtEx1.getId());
        assertThat(skAtEx1).isEqualTo(skAtEx2);
        skAtEx2.setId(2L);
        assertThat(skAtEx1).isNotEqualTo(skAtEx2);
        skAtEx1.setId(null);
        assertThat(skAtEx1).isNotEqualTo(skAtEx2);
    }
}
