package uk.ac.reigate.service.admissions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import uk.ac.reigate.domain.system.Setting
import uk.ac.reigate.repositories.system.SettingRepository

@Service
class SettingAdmissionsService {
    
    @Autowired
    SettingRepository settingRepository
    
    /**
     * The getSetting method is used to retrieve a setting from the database based on the settings name.
     *
     * @param setting the name of the Setting to retrieve
     * @return the value of the Setting from the database
     */
    Setting getSetting(String settingName) {
        return settingRepository.findBySetting(settingName)
    }
    
    /**
     * The setSetting method is used to store a setting in the database based on a Setting object.
     *
     * @param setting the Setting object to save to the database
     */
    void setSetting(Setting setting) {
        Setting existingSetting = settingRepository.findBySetting(settingName)
        if (existingSetting == null) {
            existingSetting = new Setting(settingName, value)
        } else {
            existingSetting.update(setting)
        }
        settingRepository.save(existingSetting)
    }
    
    /**
     * The setSetting method is used to store a setting in the database based on a Setting object.
     *
     * @param settingName the name of the setting to save to the database
     * @param value the value of the setting to save to the database
     */
    void setSetting(String settingName, String value) {
        Setting existingSetting = settingRepository.findBySetting(settingName)
        if (existingSetting == null) {
            existingSetting = new Setting(settingName, value)
        } else {
            existingSetting.update(value)
        }
        settingRepository.save(existingSetting)
    }
}
