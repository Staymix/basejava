<%@ page import="com.urise.webapp.util.JsonParser" %>
<%@ page import="javax.swing.*" %>
<%@ page import="com.urise.webapp.model.*" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resumes" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Редактирование контактов ${resumes.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resumes.uuid}">
        <dl>
            <dt>ФИО:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resumes.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30
                           value="${resumes.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resumes.getSection(type)}"/>
            <jsp:useBean id="section" type="com.urise.webapp.model.AbstractSection"/>
            <h3><a>${type.title}</a></h3>
            <c:choose>
                <c:when test="${type=='OBJECTIVE' || type=='PERSONAL'}">
                    <input type="text" name="${type}" size="70" value="<%=((TextSection) section).getText()%>">
                </c:when>
                <c:when test="${type=='ACHIEVEMENT' || type=='QUALIFICATIONS'}">
                    <textarea name='${type}' cols=75
                              rows=5><%=String.join("\n", ((ListSection) section).getList())%></textarea>
                </c:when>
                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizationList()%>" varStatus="counter">
                        <dl>
                            <dt>Название организации:</dt>
                            <dd><input type="text" name="${type}" size=75 value="${org.name}"></dd>
                        </dl>
                        <c:forEach var="period" items="${org.periods}" varStatus="counter">
                            <dl>
                                <dt>Должность:</dt>
                                <dd><input type="text" name="${type}${counter.index}title" size=75 value="${period.title}"></dd>
                            </dl>
                            <dl>
                                <dt>Обязанности:</dt>
                                <dd><input type="text" name="${type}${counter.index}description" size=75 value="${period.description}"></dd>
                            </dl>
                            <dl>
                                <dt>Период с:</dt>
                                <dd><input type="text" name="${type}${counter.index}startDate" size=75 value="${DateUtil.format(period.startDate)}"></dd>
                            </dl>
                            <dl>
                                <dt>Период до:</dt>
                                <dd><input type="text" name="${type}${counter.index}endDate" size=75 value="${DateUtil.format(period.endDate)}"></dd>
                            </dl>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>