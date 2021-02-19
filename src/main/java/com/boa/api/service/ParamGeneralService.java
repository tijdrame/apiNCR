package com.boa.api.service;

import com.boa.api.domain.ParamGeneral;
import com.boa.api.repository.ParamGeneralRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ParamGeneral}.
 */
@Service
@Transactional
public class ParamGeneralService {

    private final Logger log = LoggerFactory.getLogger(ParamGeneralService.class);

    private final ParamGeneralRepository paramGeneralRepository;

    public ParamGeneralService(ParamGeneralRepository paramGeneralRepository) {
        this.paramGeneralRepository = paramGeneralRepository;
    }

    /**
     * Save a paramGeneral.
     *
     * @param paramGeneral the entity to save.
     * @return the persisted entity.
     */
    public ParamGeneral save(ParamGeneral paramGeneral) {
        log.debug("Request to save ParamGeneral : {}", paramGeneral);
        return paramGeneralRepository.save(paramGeneral);
    }

    /**
     * Get all the paramGenerals.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParamGeneral> findAll() {
        log.debug("Request to get all ParamGenerals");
        return paramGeneralRepository.findAll();
    }


    /**
     * Get one paramGeneral by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParamGeneral> findOne(Long id) {
        log.debug("Request to get ParamGeneral : {}", id);
        return paramGeneralRepository.findById(id);
    }

    /**
     * Delete the paramGeneral by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParamGeneral : {}", id);
        paramGeneralRepository.deleteById(id);
    }

	public Optional<ParamGeneral> findByCode(String code) {
		return paramGeneralRepository.findByCode(code);
	}
}
