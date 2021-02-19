package com.boa.api.repository;

import java.util.Optional;

import com.boa.api.domain.ParamGeneral;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ParamGeneral entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParamGeneralRepository extends JpaRepository<ParamGeneral, Long> {

	Optional<ParamGeneral> findByCode(String code);
}
