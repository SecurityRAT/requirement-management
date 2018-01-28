package org.securityrat.requirementmanagement.repository;

import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ExtensionKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtensionKeyRepository extends JpaRepository<ExtensionKey, Long>, JpaSpecificationExecutor<ExtensionKey> {

}
