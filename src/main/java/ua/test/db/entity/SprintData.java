package ua.test.db.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface to determine get Sprint data method.
 */
public interface SprintData {

    default Map<String, Object> getSprintData(List sprint) {
        Map<String, Object> map = new HashMap<>();
        if (sprint != null && !sprint.isEmpty()) {
            LocalDate sprintStartDate;
            LocalDate sprintEndDate;
            LocalDate sprintCompleteDate;

            String[] sprintData = ((String) sprint.get(0)).split(",");
            map.put("sprintStatus", sprintData[2].split("=")[1]);
            map.put("sprintName", sprintData[3].split("=")[1]);

            try {
                sprintStartDate = OffsetDateTime.parse(sprintData[4].split("=")[1]).toLocalDate();
            } catch (DateTimeParseException e) {
                sprintStartDate = null;
            }
            map.put("sprintStartDate", sprintStartDate);

            try {
                sprintEndDate = OffsetDateTime.parse(sprintData[5].split("=")[1]).toLocalDate();
            } catch (DateTimeParseException e) {
                sprintEndDate = null;
            }
            map.put("sprintEndDate", sprintEndDate);

            try {
                sprintCompleteDate = OffsetDateTime.parse(sprintData[6].split("=")[1]).toLocalDate();
            } catch (DateTimeParseException e) {
                sprintCompleteDate = null;
            }
            map.put("sprintCompleteDate", sprintCompleteDate);
        }
        return map;
    }
}
