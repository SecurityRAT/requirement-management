package org.securityrat.requirementmanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.securityrat.requirementmanagement.web.rest.TestUtil;

public class ExtensionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Extension.class);
        Extension extension1 = new Extension();
        extension1.setId(1L);
        Extension extension2 = new Extension();
        extension2.setId(extension1.getId());
        assertThat(extension1).isEqualTo(extension2);
        extension2.setId(2L);
        assertThat(extension1).isNotEqualTo(extension2);
        extension1.setId(null);
        assertThat(extension1).isNotEqualTo(extension2);
    }
}
