<%@ page import="com.urise.webapp.model.TextSection" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resumes" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resumes.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resumes.fullName}&nbsp;<a href="resume?uuid=${resumes.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <a href="resume?action=create">Добавить резюме</a>
    <p>
        <c:forEach var="contactEntry" items="${resumes.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    <p>
    <hr>
    <table>
        <c:forEach var="sectionEntry" items="${resumes.sections}">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.AbstractSection>"/>
        <c:set var="type" value="${sectionEntry.key}"/>
        <c:set var="section" value="${sectionEntry.value}"/>
        <jsp:useBean id="section" type="com.urise.webapp.model.AbstractSection"/>
        <tr>
            <td><h2><a name="type.name">${type.title}</a></h2></td>
            <c:if test="${type=='OBJECTIVE' || type=='PERSONAL'}">
                <tr>
                    <td>
                    <%=((TextSection) section).getText()%>
                    </td>
                </tr>
            </c:if>
            <c:if test="${type!='OBJECTIVE' && type!='PERSONAL'}">
                <c:choose>
                    <c:when test="${type=='ACHIEVEMENT' || type=='QUALIFICATIONS'}">
                            <tr>
                                <td>
                                    <ul>
                                        <c:forEach var="text" items="${section.getList()}">
                                            <li><c:out value="${text}"/></li>
                                        </c:forEach>
                                    </ul>
                                </td>
                            </tr>
                    </c:when>
                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <c:forEach var="organization" items="${section.getOrganizationList()}">
                        <jsp:useBean id="organization"
                                     type="com.urise.webapp.model.Organization"/>
                        <tr>
                        <td><%=organization.name%></td>
                        </tr>
                        <c:forEach var="period" items="${organization.getPeriods()}">
                            <jsp:useBean id="period"
                                         type="com.urise.webapp.model.Period"/>
                            <tr>
                                <td>
                                    <ul>
                                        <li><c:out value="${period.title}"/></li>
                                        <li><c:out value="${period.description}"/></li>
                                        <li><c:out value="${period.endDate}"/></li>
                                        <li><c:out value="${period.startDate}"/></li>
                                    </ul>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </c:when>
                </c:choose>
            </c:if>
            </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>