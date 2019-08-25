package org.securityrat.requirementmanagement.repository;

import org.securityrat.requirementmanagement.domain.RequirementSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RequirementSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequirementSetRepository extends JpaRepository<RequirementSet, Long>, JpaSpecificationExecutor<RequirementSet> {

}
