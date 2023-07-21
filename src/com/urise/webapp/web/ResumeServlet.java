package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/resume")
public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        storage = Config.getInstance().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "create":
                r = Resume.EMPTY;
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    AbstractSection section = r.getSection(type);
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            if (section == null) {
                                section = TextSection.EMPTY;
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (section == null) {
                                section = ListSection.EMPTY;
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            OrganizationSection organizationSection = (OrganizationSection) section;
                            List<Organization> emptyFirstOrganization = new ArrayList<>();
                            emptyFirstOrganization.add(Organization.EMPTY);
                            if (organizationSection != null) {
                                for (Organization org : organizationSection.getOrganizationList()) {
                                    List<Period> emptyFirstPeriod = new ArrayList<>();
                                    emptyFirstPeriod.add(Period.EMPTY);
                                    emptyFirstPeriod.addAll(org.getPeriods());
                                    emptyFirstOrganization.add(new Organization(org.getName(), emptyFirstPeriod));
                                }
                            }
                            section = new OrganizationSection(emptyFirstOrganization);
                            break;
                    }
                    r.addSection(type, section);
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resumes", r);
        request.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        final boolean isCreate = (uuid == null || uuid.length() == 0);
        Resume r;
        if (isCreate) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (value == null || value.trim().length() == 0 && values.length < 2) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.addSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.addSection(type, new ListSection(value.split("\\n")));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> organizations = new ArrayList<>();
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (name != null || name.trim().length() != 0) {
                                List<Period> periods = new ArrayList<>();
                                String count = type.name() + i;
                                String[] startDates = request.getParameterValues(count + "startDate");
                                String[] endDates = request.getParameterValues(count + "endDate");
                                String[] titles = request.getParameterValues(count + "title");
                                String[] descriptions = request.getParameterValues(count + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (titles[j] != null || titles[j].trim().length() != 0) {
                                        periods.add(new Period(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]),
                                                titles[j], descriptions[j]));
                                    }
                                }
                                organizations.add(new Organization(name, periods));
                            }
                        }
                        r.addSection(type, new OrganizationSection(organizations));
                        break;
                }
            }
        }
        if (isCreate) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }
}