package org.securityrat.requirementmanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.securityrat.requirementmanagement.web.rest.TestUtil;

public class AttributeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attribute.class);
        Attribute attribute1 = new Attribute();
        attribute1.setId(1L);
        Attribute attribute2 = new Attribute();
        attribute2.setId(attribute1.getId());
        assertThat(attribute1).isEqualTo(attribute2);
        attribute2.setId(2L);
        assertThat(attribute1).isNotEqualTo(attribute2);
        attribute1.setId(null);
        assertThat(attribute1).isNotEqualTo(attribute2);
    }
}
