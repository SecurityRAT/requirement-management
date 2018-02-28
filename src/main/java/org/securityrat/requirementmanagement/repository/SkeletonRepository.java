package org.securityrat.requirementmanagement.repository;

import org.securityrat.requirementmanagement.domain.Skeleton;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Skeleton entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkeletonRepository extends JpaRepository<Skeleton, Long>, JpaSpecificationExecutor<Skeleton> {

}
