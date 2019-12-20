package org.securityrat.requirementmanagement.repository;
import org.securityrat.requirementmanagement.domain.ExtensionKey;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExtensionKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtensionKeyRepository extends JpaRepository<ExtensionKey, Long>, JpaSpecificationExecutor<ExtensionKey> {

}
