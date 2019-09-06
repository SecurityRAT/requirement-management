package org.securityrat.requirementmanagement.repository;

import org.securityrat.requirementmanagement.domain.AttributeKey;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AttributeKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeKeyRepository extends JpaRepository<AttributeKey, Long>, JpaSpecificationExecutor<AttributeKey> {

}
