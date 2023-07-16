package com.urise.webapp.storage;

import com.urise.webapp.model.*;

import java.time.Month;

public class ResumeTestData {
    public static Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        resume.addContact(ContactType.PHONE_NUMBER, "1234567890");
        resume.addContact(ContactType.EMAIL, "qwert@qw");
        resume.addContact(ContactType.SKYPE, "qwert");
        resume.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума"));
        resume.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок"));
        resume.addSection(SectionType.ACHIEVEMENT, new ListSection("Организация команды", "С 2013 года"));
        resume.addSection(SectionType.QUALIFICATIONS, new ListSection("JEE AS", "Version control"));
        resume.addSection(SectionType.EXPERIENCE, new OrganizationSection(
                new Organization("Wrike",
                        new Period(2013, Month.MARCH, 2013, Month.MAY, "Старший разработчик",
                                "Проектирование и разработка"))));
        resume.addSection(SectionType.EDUCATION, new OrganizationSection(
                new Organization("Coursera",
                        new Period(2014, Month.OCTOBER, 2016, Month.JANUARY, "Student",
                                "'Functional Programming Principles in Scala' by Martin Odersky"))));
        return resume;
    }
}