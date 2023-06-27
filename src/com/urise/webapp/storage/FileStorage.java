package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serialization.ObjectSerialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final ObjectSerialization objectSerialization;
    private final File directory;

    protected FileStorage(File directory, ObjectSerialization objectSerialization) {
        Objects.requireNonNull(directory, "directory must not be null");
        this.objectSerialization = objectSerialization;
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    public void clear() {
        for (File file : getFileArray()) {
            doDelete(file);
        }
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        try {
            objectSerialization.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error write resume (update)", file.getName(), e);
        }
    }

    @Override
    protected void doSave(File file, Resume r) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Error write resume (save)", file.getName(), e);
        }
        doUpdate(file, r);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return objectSerialization.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error read resume (get)", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Error delete file", file.getName());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> list = new ArrayList<>();
        for (File file : getFileArray()) {
            list.add(doGet(file));
        }
        return list;
    }

    @Override
    public int size() {
        return getFileArray().length;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExit(File file) {
        return file.exists();
    }

    private File[] getFileArray() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Error files not found", null);
        }
        return files;
    }
}
