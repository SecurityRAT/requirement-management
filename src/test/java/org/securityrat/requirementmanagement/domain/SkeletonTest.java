package org.securityrat.requirementmanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.securityrat.requirementmanagement.web.rest.TestUtil;

public class SkeletonTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Skeleton.class);
        Skeleton skeleton1 = new Skeleton();
        skeleton1.setId(1L);
        Skeleton skeleton2 = new Skeleton();
        skeleton2.setId(skeleton1.getId());
        assertThat(skeleton1).isEqualTo(skeleton2);
        skeleton2.setId(2L);
        assertThat(skeleton1).isNotEqualTo(skeleton2);
        skeleton1.setId(null);
        assertThat(skeleton1).isNotEqualTo(skeleton2);
    }
}
