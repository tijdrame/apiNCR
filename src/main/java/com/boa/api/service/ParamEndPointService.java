package com.boa.api.service;

import com.boa.api.domain.ParamEndPoint;
import com.boa.api.repository.ParamEndPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ParamEndPoint}.
 */
@Service
@Transactional
public class ParamEndPointService {

    private final Logger log = LoggerFactory.getLogger(ParamEndPointService.class);

    private final ParamEndPointRepository paramEndPointRepository;

    public ParamEndPointService(ParamEndPointRepository paramEndPointRepository) {
        this.paramEndPointRepository = paramEndPointRepository;
    }

    /**
     * Save a paramEndPoint.
     *
     * @param paramEndPoint the entity to save.
     * @return the persisted entity.
     */
    public ParamEndPoint save(ParamEndPoint paramEndPoint) {
        log.debug("Request to save ParamEndPoint : {}", paramEndPoint);
        return paramEndPointRepository.save(paramEndPoint);
    }

    /**
     * Get all the paramEndPoints.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParamEndPoint> findAll() {
        log.debug("Request to get all ParamEndPoints");
        return paramEndPointRepository.findAll();
    }


    /**
     * Get one paramEndPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParamEndPoint> findOne(Long id) {
        log.debug("Request to get ParamEndPoint : {}", id);
        return paramEndPointRepository.findById(id);
    }

    /**
     * Delete the paramEndPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParamEndPoint : {}", id);
        paramEndPointRepository.deleteById(id);
    }
}
