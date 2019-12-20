package org.securityrat.requirementmanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.securityrat.requirementmanagement.web.rest.TestUtil;

public class ExtensionKeyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtensionKey.class);
        ExtensionKey extensionKey1 = new ExtensionKey();
        extensionKey1.setId(1L);
        ExtensionKey extensionKey2 = new ExtensionKey();
        extensionKey2.setId(extensionKey1.getId());
        assertThat(extensionKey1).isEqualTo(extensionKey2);
        extensionKey2.setId(2L);
        assertThat(extensionKey1).isNotEqualTo(extensionKey2);
        extensionKey1.setId(null);
        assertThat(extensionKey1).isNotEqualTo(extensionKey2);
    }
}
