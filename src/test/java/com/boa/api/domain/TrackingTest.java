package com.boa.api.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.boa.api.web.rest.TestUtil;

public class TrackingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tracking.class);
        Tracking tracking1 = new Tracking();
        tracking1.setId(1L);
        Tracking tracking2 = new Tracking();
        tracking2.setId(tracking1.getId());
        assertThat(tracking1).isEqualTo(tracking2);
        tracking2.setId(2L);
        assertThat(tracking1).isNotEqualTo(tracking2);
        tracking1.setId(null);
        assertThat(tracking1).isNotEqualTo(tracking2);
    }
}
