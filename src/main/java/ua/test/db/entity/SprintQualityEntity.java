package ua.test.db.entity;


import lombok.Data;

import java.time.LocalDate;

/**
 * Class for aggregate data.
 * For TCB project
 */
@Data
public class SprintQualityEntity implements InsertMetaDataEntity, SprintData {

    private LocalDate createDate;
    private String sprintName;
    private String fixVersion;
    private LocalDate issueCreateDate;
    private LocalDate sprintStartDate;
    private LocalDate sprintEndDate;
    private String issueType;
    private String issueKey;
    private String bugEnvironment;
    private String subProjectName;

    /**
     * Prepares data for insertion to Db.
     *
     * @return array of strings
     */
    @Override
    public String[] getClassData() {
        return new String[]{
                String.valueOf(createDate),
                sprintName,
                fixVersion,
                String.valueOf(issueCreateDate),
                validateDateField(sprintStartDate),
                validateDateField(sprintEndDate),
                issueType,
                issueKey,
                bugEnvironment,
                subProjectName};
    }


}
