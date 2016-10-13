package de.learnlib.alex.integrationtests;

import de.learnlib.alex.core.repositories.SettingsRepository;
import de.learnlib.alex.core.entities.Settings;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SettingsRepositoryIT extends AbstractRepositoryIT {

    @Inject
    private SettingsRepository settingsRepository;

    @Test
    public void shouldSaveTheSettings() {
        Settings settings = new Settings();

        settings = settingsRepository.save(settings);

        assertTrue(settings.getId() > 0);
    }

    @Test
    public void shouldNotSaveTwoSettings() {
        Settings settings1 = new Settings();
        Settings settings2 = new Settings();
        //
        settingsRepository.save(settings1);

        settingsRepository.save(settings2); // should update and not create

        assertThat(settingsRepository.count(), is(equalTo(1L)));
    }

    @Test
    public void shouldGetTheSettings() {
        Settings settings = new Settings();
        //
        settings = settingsRepository.save(settings);

        Settings settingsFromDB = settingsRepository.get();

        assertThat(settingsFromDB, is(equalTo(settings)));
    }

}
