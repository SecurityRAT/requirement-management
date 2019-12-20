package org.securityrat.requirementmanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.securityrat.requirementmanagement.web.rest.TestUtil;

public class SkAtExTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkAtEx.class);
        SkAtEx skAtEx1 = new SkAtEx();
        skAtEx1.setId(1L);
        SkAtEx skAtEx2 = new SkAtEx();
        skAtEx2.setId(skAtEx1.getId());
        assertThat(skAtEx1).isEqualTo(skAtEx2);
        skAtEx2.setId(2L);
        assertThat(skAtEx1).isNotEqualTo(skAtEx2);
        skAtEx1.setId(null);
        assertThat(skAtEx1).isNotEqualTo(skAtEx2);
    }
}
