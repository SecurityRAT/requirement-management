package org.securityrat.requirementmanagement.repository;

import org.securityrat.requirementmanagement.domain.Extension;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Extension entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtensionRepository extends JpaRepository<Extension, Long>, JpaSpecificationExecutor<Extension> {

}
