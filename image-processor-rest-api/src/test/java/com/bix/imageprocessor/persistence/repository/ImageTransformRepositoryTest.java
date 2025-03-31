package com.bix.imageprocessor.persistence.repository;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
@Sql("/scripts/init.sql")
public class ImageTransformRepositoryTest {

    @Autowired ImageTransformRepository imageTransformRepository;

    @Test
    void shouldGetTotalRequestsToday() {

        var count = imageTransformRepository.getTotalRequestsToday(2L);

        assertThat(count).isEqualTo(3);
    }
}
