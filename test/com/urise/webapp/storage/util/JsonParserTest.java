package com.urise.webapp.storage.util;

import com.urise.webapp.model.AbstractSection;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.TextSection;
import com.urise.webapp.util.JsonParser;
import org.junit.Assert;
import org.junit.Test;

import static com.urise.webapp.storage.ResumeTestData.createResume;

public class JsonParserTest {
    @Test
    public void read() throws Exception {
        Resume r = createResume("qwer", "name");
        String json = JsonParser.write(r);
        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);
        Assert.assertEquals(r, resume);
    }

    @Test
    public void write() throws Exception {
        AbstractSection section = new TextSection("qwry");
        String json = JsonParser.write(section, AbstractSection.class);
        System.out.println(json);
        AbstractSection section2 = JsonParser.read(json, AbstractSection.class);
        Assert.assertEquals(section, section2);
    }
}