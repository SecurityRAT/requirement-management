package org.securityrat.requirementmanagement.repository;

import org.securityrat.requirementmanagement.domain.Skeleton;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Skeleton entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkeletonRepository extends JpaRepository<Skeleton, Long>, JpaSpecificationExecutor<Skeleton> {

}
