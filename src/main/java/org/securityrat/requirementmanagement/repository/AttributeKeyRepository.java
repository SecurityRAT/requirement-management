package org.securityrat.requirementmanagement.repository;

import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AttributeKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeKeyRepository extends JpaRepository<AttributeKey, Long>, JpaSpecificationExecutor<AttributeKey> {

}
