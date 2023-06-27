package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serialization.ObjectSerialization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final ObjectSerialization objectSerialization;
    private final Path directory;

    protected PathStorage(String dir, ObjectSerialization objectSerialization) {
        this.objectSerialization = objectSerialization;
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void clear() {
        getFileList().forEach(this::doDelete);
    }

    @Override
    protected void doUpdate(Path path, Resume r) {
        try {
            objectSerialization.doWrite(r, Files.newOutputStream(path));
        } catch (IOException e) {
            throw new StorageException("Error write resume (update)", path.toString(), e);
        }
    }

    @Override
    protected void doSave(Path path, Resume r) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Error write resume (save)", path.toString(), e);
        }
        doUpdate(path, r);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return objectSerialization.doRead(Files.newInputStream(path));
        } catch (IOException e) {
            throw new StorageException("Error read resume (get)", path.toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Error delete resume", path.toString());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        return getFileList().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return (int) getFileList().filter(Files::isRegularFile).count();
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExit(Path path) {
        return Files.exists(path);
    }

    private Stream<Path> getFileList() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }
}
