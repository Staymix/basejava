package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@WebServlet("/resume")
public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        storage = Config.getInstance().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        Writer out = response.getWriter();
        out.write("""
                <html>
                <head>
                    <title>Список резюме</title>
                </head>
                <body>
                    <table border="3">
                        <tr>
                            <th>UUID</th>
                            <th>NAME</th>
                        </tr>""");
        for (Resume resume : storage.getAllSorted()) {
            out.write("<tr>\n" +
                    "            <td>" + resume.getUuid() + "</td>\n" +
                    "            <td>" + resume.getFullName() + "</td>\n" +
                    "        </tr>");
        }
        out.write("""
                 </table>
                </body>
                </html>""");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}