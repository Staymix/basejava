package com.urise.webapp.model;

import com.google.gson.annotations.JsonAdapter;
import com.urise.webapp.util.LocalDateAdapter;
import com.urise.webapp.util.LocalDateAdapterJson;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import static com.urise.webapp.util.DateUtil.NOW;

@XmlAccessorType(XmlAccessType.FIELD)
public class Period implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonAdapter(LocalDateAdapterJson.class)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate startDate;
    @JsonAdapter(LocalDateAdapterJson.class)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate endDate;
    private String title;
    private String description;

    public Period() {
    }

    public Period(int startYear, Month startMonth, String title, String description) {
        Objects.requireNonNull(startMonth, "startMonth must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(description, "description must not be null");
        this.startDate = LocalDate.of(startYear, startMonth, 1);
        this.endDate = NOW;
        this.title = title;
        this.description = description;
    }

    public Period(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
        Objects.requireNonNull(startMonth, "startMonth must not be null");
        Objects.requireNonNull(endMonth, "startMonth must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(description, "description must not be null");
        this.startDate = LocalDate.of(startYear, startMonth, 1);
        this.endDate = LocalDate.of(endYear, endMonth, 1);
        this.title = title;
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!Objects.equals(startDate, period.startDate)) return false;
        if (!Objects.equals(endDate, period.endDate)) return false;
        if (!Objects.equals(title, period.title)) return false;
        return Objects.equals(description, period.description);
    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
