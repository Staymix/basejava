package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionType;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static com.urise.webapp.storage.ResumeTestData.createResume;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.getInstance().getStorageDir();

    protected Storage storage;


    private static final String UUID_1 = UUID.randomUUID().toString();
    private static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();
    private static final String UUID_4 = UUID.randomUUID().toString();
    private static final String UUID_NOT_EXIST = "dummy";
    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;
    private static final Resume RESUME_NOT_EXIST;

    static {
        RESUME_1 = createResume(UUID_1, "Name1");
        RESUME_2 = createResume(UUID_2, "Name2");
        RESUME_3 = createResume(UUID_3, "Name3");
        RESUME_4 = createResume(UUID_4, "Name4");
        RESUME_NOT_EXIST = createResume(UUID_NOT_EXIST, "Name5");
    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertArrayEquals(storage.getAllSorted().toArray(), new Resume[0]);
    }

    @Test
    public void update() throws Exception {
        Resume resumeNew = new Resume(UUID_1, "Name1");
        storage.update(resumeNew);
        assertEquals(resumeNew, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(RESUME_NOT_EXIST);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistStorage() throws Exception {
        storage.save(RESUME_1);
    }

    @Test
    public void save() throws Exception {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test
    public void get() throws Exception {
        assertGet(RESUME_1);
        Resume r = RESUME_1;
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        assertGet(RESUME_NOT_EXIST);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_2);
        assertGet(RESUME_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistStorage() throws Exception {
        storage.delete(UUID_NOT_EXIST);
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> list = storage.getAllSorted();
        assertEquals(3, list.size());
        assertArrayEquals(list.toArray(new Resume[0]), new Resume[]{RESUME_1, RESUME_2, RESUME_3});
    }

    @Test
    public void size() throws Exception {
        assertSize(3);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    public void assertGet(Resume resume) throws Exception {
        storage.get(resume.getUuid());
    }
}