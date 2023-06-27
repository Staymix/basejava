package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
        String fullName = "";
        Resume r = new Resume(fullName);
        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new uuid");
        System.out.println(r);
        Method[] methods = r.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("toString")) {
                System.out.println("Вызываю метод toString: " + method.invoke(r));
                return;
            }
        }
    }
}
