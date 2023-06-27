package com.urise.webapp.storage.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataStreamSerializer implements ObjectSerialization {
    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeWithException(r.getContacts().entrySet(), dos, entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            writeWithException(r.getSections().entrySet(), dos, entry -> {
                SectionType type = entry.getKey();
                AbstractSection section = entry.getValue();
                dos.writeUTF(type.name());
                switch (type) {
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) section).getText());
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            writeWithException(((ListSection) section).getList(), dos, dos::writeUTF);
                    case EXPERIENCE, EDUCATION -> {
                        writeWithException(((OrganizationSection) section).getOrganizationList(), dos, organization -> {
                            dos.writeUTF(organization.getName());
                            writeWithException(organization.getPeriods(), dos, period -> {
                                dos.writeLong(period.getStartDate().toEpochDay());
                                dos.writeLong(period.getEndDate().toEpochDay());
                                dos.writeUTF(period.getTitle());
                                dos.writeUTF(period.getDescription());
                            });
                        });
                    }
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            readElement(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readElement(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.addSection(sectionType, readSection(dis, sectionType));
            });
            return resume;
        }
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T element : collection) {
            writer.write(element);
        }
    }

    @FunctionalInterface
    private interface Writer<T> {
        void write(T t) throws IOException;
    }

    private <T> void readElement(DataInputStream dis, ElementSections sections) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            sections.read();
        }
    }

    @FunctionalInterface
    private interface ElementSections {
        void read() throws IOException;

    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> {
                return new TextSection(dis.readUTF());
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                return new ListSection(readList(dis, dis::readUTF));
            }
            case EXPERIENCE, EDUCATION -> {
                return new OrganizationSection(readList
                        (dis, () -> new Organization(dis.readUTF(), readList
                                (dis, () -> createPeriod(dis)))));
            }
            default -> throw new IllegalStateException();
        }
    }

    private <T> List<T> readList(DataInputStream dis, ElementList<T> element) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(element.read());
        }
        return list;
    }

    @FunctionalInterface
    private interface ElementList<T> {
        T read() throws IOException;

    }

    private Period createPeriod(DataInputStream dis) throws IOException {
        Period period = new Period();
        period.setStartDate(LocalDate.ofEpochDay(dis.readLong()));
        period.setEndDate(LocalDate.ofEpochDay(dis.readLong()));
        period.setTitle(dis.readUTF());
        period.setDescription(dis.readUTF());
        return period;
    }
}