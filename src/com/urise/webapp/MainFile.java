package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {

    private static StringBuilder indentation = new StringBuilder(" ");

    public static void main(String[] args) {
        File file = new File(".\\.gitignore");
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error file not found", e);
        }
        File dir = new File(".\\src");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : dir.list()) {
                System.out.println(name);
            }
        }
        try (FileInputStream fis = new FileInputStream(".\\.gitignore")) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recursionFunc(dir);
    }

    public static void recursionFunc(File fileDirectory) {
        indentation.append(" ");
        File[] files = fileDirectory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                System.out.println(indentation + "  File: " + file.getName());
            } else {
                System.out.println(indentation + "Directory: " + file.getName());
                recursionFunc(file);
            }
        }
    }
}
