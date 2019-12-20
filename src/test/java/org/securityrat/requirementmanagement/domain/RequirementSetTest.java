package org.securityrat.requirementmanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.securityrat.requirementmanagement.web.rest.TestUtil;

public class RequirementSetTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequirementSet.class);
        RequirementSet requirementSet1 = new RequirementSet();
        requirementSet1.setId(1L);
        RequirementSet requirementSet2 = new RequirementSet();
        requirementSet2.setId(requirementSet1.getId());
        assertThat(requirementSet1).isEqualTo(requirementSet2);
        requirementSet2.setId(2L);
        assertThat(requirementSet1).isNotEqualTo(requirementSet2);
        requirementSet1.setId(null);
        assertThat(requirementSet1).isNotEqualTo(requirementSet2);
    }
}
