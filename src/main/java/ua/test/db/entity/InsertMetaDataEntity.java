package ua.test.db.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Interface для реализации вставки данных в метабазу.
 * используя prepared stmt.
 */

public interface InsertMetaDataEntity {
    /**
     * Метод подготавливает данные.
     * в виде массива значений полей класса.
     * все значения приводятся к String
     */
    String[] getClassData();

    /**
     * Конвертирует LocalDate обьект в стринг.
     * если обьект не Null
     *
     * @param data обьект для конвертации
     * @return Строковое представление
     */
    default String validateDateField(LocalDate data) {
        if (data == null) return null;
        return data.toString();
    }

    /**
     * Конвертирует LocalDateTime обьект в стринг.
     * если обьект не Null
     *
     * @param data обьект для конвертации
     * @return Строковое представление
     */
    default String validateDateTimeField(LocalDateTime data) {
        if (data == null) return null;
        return data.toString();
    }

    /**
     * Конвертирует Double обьект в стринг.
     * если обьект не Null
     *
     * @param data обьект для конвертации
     * @return Строковое представление
     */
    default String validateDoubleField(Double data) {
        if (data == null) return null;
        return String.valueOf(data);
    }
}
