package com.example.web.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.web.IntegrationTest;
import com.example.web.domain.Appuser;
import com.example.web.domain.Notification;
import com.example.web.domain.User;
import com.example.web.repository.AppuserRepository;
import com.example.web.service.criteria.AppuserCriteria;
import com.example.web.service.dto.AppuserDTO;
import com.example.web.service.mapper.AppuserMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppuserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppuserResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PLATE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PLATE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/appusers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppuserRepository appuserRepository;

    @Autowired
    private AppuserMapper appuserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppuserMockMvc;

    private Appuser appuser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appuser createEntity(EntityManager em) {
        Appuser appuser = new Appuser()
            .date(DEFAULT_DATE)
            .plateNumber(DEFAULT_PLATE_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        appuser.setUser(user);
        return appuser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appuser createUpdatedEntity(EntityManager em) {
        Appuser appuser = new Appuser()
            .date(UPDATED_DATE)
            .plateNumber(UPDATED_PLATE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        appuser.setUser(user);
        return appuser;
    }

    @BeforeEach
    public void initTest() {
        appuser = createEntity(em);
    }

    @Test
    @Transactional
    void createAppuser() throws Exception {
        int databaseSizeBeforeCreate = appuserRepository.findAll().size();
        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);
        restAppuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeCreate + 1);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAppuser.getPlateNumber()).isEqualTo(DEFAULT_PLATE_NUMBER);
        assertThat(testAppuser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAppuser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAppuser.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createAppuserWithExistingId() throws Exception {
        // Create the Appuser with an existing ID
        appuser.setId(1L);
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        int databaseSizeBeforeCreate = appuserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appuserRepository.findAll().size();
        // set the field null
        appuser.setDate(null);

        // Create the Appuser, which fails.
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        restAppuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlateNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = appuserRepository.findAll().size();
        // set the field null
        appuser.setPlateNumber(null);

        // Create the Appuser, which fails.
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        restAppuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppusers() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].plateNumber").value(hasItem(DEFAULT_PLATE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get the appuser
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL_ID, appuser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appuser.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.plateNumber").value(DEFAULT_PLATE_NUMBER))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getAppusersByIdFiltering() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        Long id = appuser.getId();

        defaultAppuserShouldBeFound("id.equals=" + id);
        defaultAppuserShouldNotBeFound("id.notEquals=" + id);

        defaultAppuserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppuserShouldNotBeFound("id.greaterThan=" + id);

        defaultAppuserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppuserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppusersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where date equals to DEFAULT_DATE
        defaultAppuserShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the appuserList where date equals to UPDATED_DATE
        defaultAppuserShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppusersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where date in DEFAULT_DATE or UPDATED_DATE
        defaultAppuserShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the appuserList where date equals to UPDATED_DATE
        defaultAppuserShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppusersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where date is not null
        defaultAppuserShouldBeFound("date.specified=true");

        // Get all the appuserList where date is null
        defaultAppuserShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByPlateNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where plateNumber equals to DEFAULT_PLATE_NUMBER
        defaultAppuserShouldBeFound("plateNumber.equals=" + DEFAULT_PLATE_NUMBER);

        // Get all the appuserList where plateNumber equals to UPDATED_PLATE_NUMBER
        defaultAppuserShouldNotBeFound("plateNumber.equals=" + UPDATED_PLATE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppusersByPlateNumberIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where plateNumber in DEFAULT_PLATE_NUMBER or UPDATED_PLATE_NUMBER
        defaultAppuserShouldBeFound("plateNumber.in=" + DEFAULT_PLATE_NUMBER + "," + UPDATED_PLATE_NUMBER);

        // Get all the appuserList where plateNumber equals to UPDATED_PLATE_NUMBER
        defaultAppuserShouldNotBeFound("plateNumber.in=" + UPDATED_PLATE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppusersByPlateNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where plateNumber is not null
        defaultAppuserShouldBeFound("plateNumber.specified=true");

        // Get all the appuserList where plateNumber is null
        defaultAppuserShouldNotBeFound("plateNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByPlateNumberContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where plateNumber contains DEFAULT_PLATE_NUMBER
        defaultAppuserShouldBeFound("plateNumber.contains=" + DEFAULT_PLATE_NUMBER);

        // Get all the appuserList where plateNumber contains UPDATED_PLATE_NUMBER
        defaultAppuserShouldNotBeFound("plateNumber.contains=" + UPDATED_PLATE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppusersByPlateNumberNotContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where plateNumber does not contain DEFAULT_PLATE_NUMBER
        defaultAppuserShouldNotBeFound("plateNumber.doesNotContain=" + DEFAULT_PLATE_NUMBER);

        // Get all the appuserList where plateNumber does not contain UPDATED_PLATE_NUMBER
        defaultAppuserShouldBeFound("plateNumber.doesNotContain=" + UPDATED_PLATE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppusersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where firstName equals to DEFAULT_FIRST_NAME
        defaultAppuserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the appuserList where firstName equals to UPDATED_FIRST_NAME
        defaultAppuserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppusersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultAppuserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the appuserList where firstName equals to UPDATED_FIRST_NAME
        defaultAppuserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppusersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where firstName is not null
        defaultAppuserShouldBeFound("firstName.specified=true");

        // Get all the appuserList where firstName is null
        defaultAppuserShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where firstName contains DEFAULT_FIRST_NAME
        defaultAppuserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the appuserList where firstName contains UPDATED_FIRST_NAME
        defaultAppuserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppusersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultAppuserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the appuserList where firstName does not contain UPDATED_FIRST_NAME
        defaultAppuserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppusersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where lastName equals to DEFAULT_LAST_NAME
        defaultAppuserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the appuserList where lastName equals to UPDATED_LAST_NAME
        defaultAppuserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppusersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultAppuserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the appuserList where lastName equals to UPDATED_LAST_NAME
        defaultAppuserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppusersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where lastName is not null
        defaultAppuserShouldBeFound("lastName.specified=true");

        // Get all the appuserList where lastName is null
        defaultAppuserShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where lastName contains DEFAULT_LAST_NAME
        defaultAppuserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the appuserList where lastName contains UPDATED_LAST_NAME
        defaultAppuserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppusersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where lastName does not contain DEFAULT_LAST_NAME
        defaultAppuserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the appuserList where lastName does not contain UPDATED_LAST_NAME
        defaultAppuserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppusersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where email equals to DEFAULT_EMAIL
        defaultAppuserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the appuserList where email equals to UPDATED_EMAIL
        defaultAppuserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppusersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultAppuserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the appuserList where email equals to UPDATED_EMAIL
        defaultAppuserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppusersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where email is not null
        defaultAppuserShouldBeFound("email.specified=true");

        // Get all the appuserList where email is null
        defaultAppuserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllAppusersByEmailContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where email contains DEFAULT_EMAIL
        defaultAppuserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the appuserList where email contains UPDATED_EMAIL
        defaultAppuserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppusersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where email does not contain DEFAULT_EMAIL
        defaultAppuserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the appuserList where email does not contain UPDATED_EMAIL
        defaultAppuserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppusersByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = appuser.getUser();
        appuserRepository.saveAndFlush(appuser);
        Long userId = user.getId();

        // Get all the appuserList where user equals to userId
        defaultAppuserShouldBeFound("userId.equals=" + userId);

        // Get all the appuserList where user equals to (userId + 1)
        defaultAppuserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllAppusersByNotificationIsEqualToSomething() throws Exception {
        Notification notification;
        if (TestUtil.findAll(em, Notification.class).isEmpty()) {
            appuserRepository.saveAndFlush(appuser);
            notification = NotificationResourceIT.createEntity(em);
        } else {
            notification = TestUtil.findAll(em, Notification.class).get(0);
        }
        em.persist(notification);
        em.flush();
        appuser.addNotification(notification);
        appuserRepository.saveAndFlush(appuser);
        Long notificationId = notification.getId();

        // Get all the appuserList where notification equals to notificationId
        defaultAppuserShouldBeFound("notificationId.equals=" + notificationId);

        // Get all the appuserList where notification equals to (notificationId + 1)
        defaultAppuserShouldNotBeFound("notificationId.equals=" + (notificationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppuserShouldBeFound(String filter) throws Exception {
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].plateNumber").value(hasItem(DEFAULT_PLATE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppuserShouldNotBeFound(String filter) throws Exception {
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppuser() throws Exception {
        // Get the appuser
        restAppuserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser
        Appuser updatedAppuser = appuserRepository.findById(appuser.getId()).get();
        // Disconnect from session so that the updates on updatedAppuser are not directly saved in db
        em.detach(updatedAppuser);
        updatedAppuser
            .date(UPDATED_DATE)
            .plateNumber(UPDATED_PLATE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL);
        AppuserDTO appuserDTO = appuserMapper.toDto(updatedAppuser);

        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appuserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAppuser.getPlateNumber()).isEqualTo(UPDATED_PLATE_NUMBER);
        assertThat(testAppuser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAppuser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAppuser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appuserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppuserWithPatch() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser using partial update
        Appuser partialUpdatedAppuser = new Appuser();
        partialUpdatedAppuser.setId(appuser.getId());

        partialUpdatedAppuser.plateNumber(UPDATED_PLATE_NUMBER).firstName(UPDATED_FIRST_NAME).email(UPDATED_EMAIL);

        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppuser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppuser))
            )
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAppuser.getPlateNumber()).isEqualTo(UPDATED_PLATE_NUMBER);
        assertThat(testAppuser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAppuser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAppuser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateAppuserWithPatch() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser using partial update
        Appuser partialUpdatedAppuser = new Appuser();
        partialUpdatedAppuser.setId(appuser.getId());

        partialUpdatedAppuser
            .date(UPDATED_DATE)
            .plateNumber(UPDATED_PLATE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL);

        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppuser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppuser))
            )
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAppuser.getPlateNumber()).isEqualTo(UPDATED_PLATE_NUMBER);
        assertThat(testAppuser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAppuser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAppuser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appuserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();
        appuser.setId(count.incrementAndGet());

        // Create the Appuser
        AppuserDTO appuserDTO = appuserMapper.toDto(appuser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppuserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appuserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        int databaseSizeBeforeDelete = appuserRepository.findAll().size();

        // Delete the appuser
        restAppuserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appuser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
