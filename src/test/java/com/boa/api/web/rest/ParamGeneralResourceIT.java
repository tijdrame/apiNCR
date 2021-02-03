package com.boa.api.web.rest;

import com.boa.api.ApiNcrApp;
import com.boa.api.domain.ParamGeneral;
import com.boa.api.repository.ParamGeneralRepository;
import com.boa.api.service.ParamGeneralService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ParamGeneralResource} REST controller.
 */
@SpringBootTest(classes = ApiNcrApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ParamGeneralResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_VAR_STRING_1 = "AAAAAAAAAA";
    private static final String UPDATED_VAR_STRING_1 = "BBBBBBBBBB";

    private static final String DEFAULT_VAR_STRING_2 = "AAAAAAAAAA";
    private static final String UPDATED_VAR_STRING_2 = "BBBBBBBBBB";

    private static final String DEFAULT_VAR_STRING_3 = "AAAAAAAAAA";
    private static final String UPDATED_VAR_STRING_3 = "BBBBBBBBBB";

    private static final Integer DEFAULT_VAR_INTEGER_1 = 1;
    private static final Integer UPDATED_VAR_INTEGER_1 = 2;

    private static final Integer DEFAULT_VAR_INTEGER_2 = 1;
    private static final Integer UPDATED_VAR_INTEGER_2 = 2;

    private static final Integer DEFAULT_VAR_INTEGER_3 = 1;
    private static final Integer UPDATED_VAR_INTEGER_3 = 2;

    private static final Double DEFAULT_VAR_DOUBLE_1 = 1D;
    private static final Double UPDATED_VAR_DOUBLE_1 = 2D;

    private static final Double DEFAULT_VAR_DOUBLE_2 = 1D;
    private static final Double UPDATED_VAR_DOUBLE_2 = 2D;

    private static final Double DEFAULT_VAR_DOUBLE_3 = 1D;
    private static final Double UPDATED_VAR_DOUBLE_3 = 2D;

    private static final Instant DEFAULT_VAR_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VAR_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_VAR_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VAR_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_VAR_BOOLEAN = false;
    private static final Boolean UPDATED_VAR_BOOLEAN = true;

    @Autowired
    private ParamGeneralRepository paramGeneralRepository;

    @Autowired
    private ParamGeneralService paramGeneralService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParamGeneralMockMvc;

    private ParamGeneral paramGeneral;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParamGeneral createEntity(EntityManager em) {
        ParamGeneral paramGeneral = new ParamGeneral()
            .code(DEFAULT_CODE)
            .pays(DEFAULT_PAYS)
            .varString1(DEFAULT_VAR_STRING_1)
            .varString2(DEFAULT_VAR_STRING_2)
            .varString3(DEFAULT_VAR_STRING_3)
            .varInteger1(DEFAULT_VAR_INTEGER_1)
            .varInteger2(DEFAULT_VAR_INTEGER_2)
            .varInteger3(DEFAULT_VAR_INTEGER_3)
            .varDouble1(DEFAULT_VAR_DOUBLE_1)
            .varDouble2(DEFAULT_VAR_DOUBLE_2)
            .varDouble3(DEFAULT_VAR_DOUBLE_3)
            .varInstant(DEFAULT_VAR_INSTANT)
            .varDate(DEFAULT_VAR_DATE)
            .varBoolean(DEFAULT_VAR_BOOLEAN);
        return paramGeneral;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParamGeneral createUpdatedEntity(EntityManager em) {
        ParamGeneral paramGeneral = new ParamGeneral()
            .code(UPDATED_CODE)
            .pays(UPDATED_PAYS)
            .varString1(UPDATED_VAR_STRING_1)
            .varString2(UPDATED_VAR_STRING_2)
            .varString3(UPDATED_VAR_STRING_3)
            .varInteger1(UPDATED_VAR_INTEGER_1)
            .varInteger2(UPDATED_VAR_INTEGER_2)
            .varInteger3(UPDATED_VAR_INTEGER_3)
            .varDouble1(UPDATED_VAR_DOUBLE_1)
            .varDouble2(UPDATED_VAR_DOUBLE_2)
            .varDouble3(UPDATED_VAR_DOUBLE_3)
            .varInstant(UPDATED_VAR_INSTANT)
            .varDate(UPDATED_VAR_DATE)
            .varBoolean(UPDATED_VAR_BOOLEAN);
        return paramGeneral;
    }

    @BeforeEach
    public void initTest() {
        paramGeneral = createEntity(em);
    }

    @Test
    @Transactional
    public void createParamGeneral() throws Exception {
        int databaseSizeBeforeCreate = paramGeneralRepository.findAll().size();
        // Create the ParamGeneral
        restParamGeneralMockMvc.perform(post("/api/param-generals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paramGeneral)))
            .andExpect(status().isCreated());

        // Validate the ParamGeneral in the database
        List<ParamGeneral> paramGeneralList = paramGeneralRepository.findAll();
        assertThat(paramGeneralList).hasSize(databaseSizeBeforeCreate + 1);
        ParamGeneral testParamGeneral = paramGeneralList.get(paramGeneralList.size() - 1);
        assertThat(testParamGeneral.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testParamGeneral.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testParamGeneral.getVarString1()).isEqualTo(DEFAULT_VAR_STRING_1);
        assertThat(testParamGeneral.getVarString2()).isEqualTo(DEFAULT_VAR_STRING_2);
        assertThat(testParamGeneral.getVarString3()).isEqualTo(DEFAULT_VAR_STRING_3);
        assertThat(testParamGeneral.getVarInteger1()).isEqualTo(DEFAULT_VAR_INTEGER_1);
        assertThat(testParamGeneral.getVarInteger2()).isEqualTo(DEFAULT_VAR_INTEGER_2);
        assertThat(testParamGeneral.getVarInteger3()).isEqualTo(DEFAULT_VAR_INTEGER_3);
        assertThat(testParamGeneral.getVarDouble1()).isEqualTo(DEFAULT_VAR_DOUBLE_1);
        assertThat(testParamGeneral.getVarDouble2()).isEqualTo(DEFAULT_VAR_DOUBLE_2);
        assertThat(testParamGeneral.getVarDouble3()).isEqualTo(DEFAULT_VAR_DOUBLE_3);
        assertThat(testParamGeneral.getVarInstant()).isEqualTo(DEFAULT_VAR_INSTANT);
        assertThat(testParamGeneral.getVarDate()).isEqualTo(DEFAULT_VAR_DATE);
        assertThat(testParamGeneral.isVarBoolean()).isEqualTo(DEFAULT_VAR_BOOLEAN);
    }

    @Test
    @Transactional
    public void createParamGeneralWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paramGeneralRepository.findAll().size();

        // Create the ParamGeneral with an existing ID
        paramGeneral.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParamGeneralMockMvc.perform(post("/api/param-generals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paramGeneral)))
            .andExpect(status().isBadRequest());

        // Validate the ParamGeneral in the database
        List<ParamGeneral> paramGeneralList = paramGeneralRepository.findAll();
        assertThat(paramGeneralList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllParamGenerals() throws Exception {
        // Initialize the database
        paramGeneralRepository.saveAndFlush(paramGeneral);

        // Get all the paramGeneralList
        restParamGeneralMockMvc.perform(get("/api/param-generals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paramGeneral.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].varString1").value(hasItem(DEFAULT_VAR_STRING_1)))
            .andExpect(jsonPath("$.[*].varString2").value(hasItem(DEFAULT_VAR_STRING_2)))
            .andExpect(jsonPath("$.[*].varString3").value(hasItem(DEFAULT_VAR_STRING_3)))
            .andExpect(jsonPath("$.[*].varInteger1").value(hasItem(DEFAULT_VAR_INTEGER_1)))
            .andExpect(jsonPath("$.[*].varInteger2").value(hasItem(DEFAULT_VAR_INTEGER_2)))
            .andExpect(jsonPath("$.[*].varInteger3").value(hasItem(DEFAULT_VAR_INTEGER_3)))
            .andExpect(jsonPath("$.[*].varDouble1").value(hasItem(DEFAULT_VAR_DOUBLE_1.doubleValue())))
            .andExpect(jsonPath("$.[*].varDouble2").value(hasItem(DEFAULT_VAR_DOUBLE_2.doubleValue())))
            .andExpect(jsonPath("$.[*].varDouble3").value(hasItem(DEFAULT_VAR_DOUBLE_3.doubleValue())))
            .andExpect(jsonPath("$.[*].varInstant").value(hasItem(DEFAULT_VAR_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].varDate").value(hasItem(DEFAULT_VAR_DATE.toString())))
            .andExpect(jsonPath("$.[*].varBoolean").value(hasItem(DEFAULT_VAR_BOOLEAN.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getParamGeneral() throws Exception {
        // Initialize the database
        paramGeneralRepository.saveAndFlush(paramGeneral);

        // Get the paramGeneral
        restParamGeneralMockMvc.perform(get("/api/param-generals/{id}", paramGeneral.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paramGeneral.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.varString1").value(DEFAULT_VAR_STRING_1))
            .andExpect(jsonPath("$.varString2").value(DEFAULT_VAR_STRING_2))
            .andExpect(jsonPath("$.varString3").value(DEFAULT_VAR_STRING_3))
            .andExpect(jsonPath("$.varInteger1").value(DEFAULT_VAR_INTEGER_1))
            .andExpect(jsonPath("$.varInteger2").value(DEFAULT_VAR_INTEGER_2))
            .andExpect(jsonPath("$.varInteger3").value(DEFAULT_VAR_INTEGER_3))
            .andExpect(jsonPath("$.varDouble1").value(DEFAULT_VAR_DOUBLE_1.doubleValue()))
            .andExpect(jsonPath("$.varDouble2").value(DEFAULT_VAR_DOUBLE_2.doubleValue()))
            .andExpect(jsonPath("$.varDouble3").value(DEFAULT_VAR_DOUBLE_3.doubleValue()))
            .andExpect(jsonPath("$.varInstant").value(DEFAULT_VAR_INSTANT.toString()))
            .andExpect(jsonPath("$.varDate").value(DEFAULT_VAR_DATE.toString()))
            .andExpect(jsonPath("$.varBoolean").value(DEFAULT_VAR_BOOLEAN.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingParamGeneral() throws Exception {
        // Get the paramGeneral
        restParamGeneralMockMvc.perform(get("/api/param-generals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParamGeneral() throws Exception {
        // Initialize the database
        paramGeneralService.save(paramGeneral);

        int databaseSizeBeforeUpdate = paramGeneralRepository.findAll().size();

        // Update the paramGeneral
        ParamGeneral updatedParamGeneral = paramGeneralRepository.findById(paramGeneral.getId()).get();
        // Disconnect from session so that the updates on updatedParamGeneral are not directly saved in db
        em.detach(updatedParamGeneral);
        updatedParamGeneral
            .code(UPDATED_CODE)
            .pays(UPDATED_PAYS)
            .varString1(UPDATED_VAR_STRING_1)
            .varString2(UPDATED_VAR_STRING_2)
            .varString3(UPDATED_VAR_STRING_3)
            .varInteger1(UPDATED_VAR_INTEGER_1)
            .varInteger2(UPDATED_VAR_INTEGER_2)
            .varInteger3(UPDATED_VAR_INTEGER_3)
            .varDouble1(UPDATED_VAR_DOUBLE_1)
            .varDouble2(UPDATED_VAR_DOUBLE_2)
            .varDouble3(UPDATED_VAR_DOUBLE_3)
            .varInstant(UPDATED_VAR_INSTANT)
            .varDate(UPDATED_VAR_DATE)
            .varBoolean(UPDATED_VAR_BOOLEAN);

        restParamGeneralMockMvc.perform(put("/api/param-generals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedParamGeneral)))
            .andExpect(status().isOk());

        // Validate the ParamGeneral in the database
        List<ParamGeneral> paramGeneralList = paramGeneralRepository.findAll();
        assertThat(paramGeneralList).hasSize(databaseSizeBeforeUpdate);
        ParamGeneral testParamGeneral = paramGeneralList.get(paramGeneralList.size() - 1);
        assertThat(testParamGeneral.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testParamGeneral.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testParamGeneral.getVarString1()).isEqualTo(UPDATED_VAR_STRING_1);
        assertThat(testParamGeneral.getVarString2()).isEqualTo(UPDATED_VAR_STRING_2);
        assertThat(testParamGeneral.getVarString3()).isEqualTo(UPDATED_VAR_STRING_3);
        assertThat(testParamGeneral.getVarInteger1()).isEqualTo(UPDATED_VAR_INTEGER_1);
        assertThat(testParamGeneral.getVarInteger2()).isEqualTo(UPDATED_VAR_INTEGER_2);
        assertThat(testParamGeneral.getVarInteger3()).isEqualTo(UPDATED_VAR_INTEGER_3);
        assertThat(testParamGeneral.getVarDouble1()).isEqualTo(UPDATED_VAR_DOUBLE_1);
        assertThat(testParamGeneral.getVarDouble2()).isEqualTo(UPDATED_VAR_DOUBLE_2);
        assertThat(testParamGeneral.getVarDouble3()).isEqualTo(UPDATED_VAR_DOUBLE_3);
        assertThat(testParamGeneral.getVarInstant()).isEqualTo(UPDATED_VAR_INSTANT);
        assertThat(testParamGeneral.getVarDate()).isEqualTo(UPDATED_VAR_DATE);
        assertThat(testParamGeneral.isVarBoolean()).isEqualTo(UPDATED_VAR_BOOLEAN);
    }

    @Test
    @Transactional
    public void updateNonExistingParamGeneral() throws Exception {
        int databaseSizeBeforeUpdate = paramGeneralRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParamGeneralMockMvc.perform(put("/api/param-generals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paramGeneral)))
            .andExpect(status().isBadRequest());

        // Validate the ParamGeneral in the database
        List<ParamGeneral> paramGeneralList = paramGeneralRepository.findAll();
        assertThat(paramGeneralList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParamGeneral() throws Exception {
        // Initialize the database
        paramGeneralService.save(paramGeneral);

        int databaseSizeBeforeDelete = paramGeneralRepository.findAll().size();

        // Delete the paramGeneral
        restParamGeneralMockMvc.perform(delete("/api/param-generals/{id}", paramGeneral.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParamGeneral> paramGeneralList = paramGeneralRepository.findAll();
        assertThat(paramGeneralList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
