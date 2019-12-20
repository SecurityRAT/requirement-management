package org.securityrat.requirementmanagement.repository;
import org.securityrat.requirementmanagement.domain.SkAtEx;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SkAtEx entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkAtExRepository extends JpaRepository<SkAtEx, Long>, JpaSpecificationExecutor<SkAtEx> {

}
