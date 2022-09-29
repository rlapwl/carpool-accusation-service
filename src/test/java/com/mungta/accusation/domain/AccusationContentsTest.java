package com.mungta.accusation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccusationContentsTest {

    @DisplayName("equals test")
    @Test
    void testEquals_true() {
        AccusationContents accusationContents = new AccusationContents("title", "desc");
        AccusationContents other = new AccusationContents("title", "desc");

        assertThat(accusationContents.equals(other)).isEqualTo(true);
        assertThat(accusationContents.hashCode() == other.hashCode()).isEqualTo(true);
    }

    @DisplayName("not equals test")
    @Test
    void testEquals_false() {
        AccusationContents accusationContents = new AccusationContents("title", "desc");
        AccusationContents other = new AccusationContents("title1", "desc");

        assertThat(accusationContents.equals(other)).isEqualTo(false);
        assertThat(accusationContents.hashCode() == other.hashCode()).isEqualTo(false);
    }

}
