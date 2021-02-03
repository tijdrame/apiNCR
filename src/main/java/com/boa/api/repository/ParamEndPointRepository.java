package com.boa.api.repository;

import com.boa.api.domain.ParamEndPoint;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ParamEndPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParamEndPointRepository extends JpaRepository<ParamEndPoint, Long> {
}
