package com.boa.api.repository;

import java.util.Optional;

import com.boa.api.domain.ParamEndPoint;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ParamEndPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParamEndPointRepository extends JpaRepository<ParamEndPoint, Long> {

	Optional<ParamEndPoint> findByCodeParam(String codeParam);
}
