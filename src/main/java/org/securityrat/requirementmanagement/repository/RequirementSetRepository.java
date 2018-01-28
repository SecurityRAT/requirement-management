package org.securityrat.requirementmanagement.repository;

import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RequirementSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequirementSetRepository extends JpaRepository<RequirementSet, Long>, JpaSpecificationExecutor<RequirementSet> {

}
