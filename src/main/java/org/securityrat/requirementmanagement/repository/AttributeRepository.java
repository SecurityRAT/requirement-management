package org.securityrat.requirementmanagement.repository;
import org.securityrat.requirementmanagement.domain.Attribute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Attribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long>, JpaSpecificationExecutor<Attribute> {

}
