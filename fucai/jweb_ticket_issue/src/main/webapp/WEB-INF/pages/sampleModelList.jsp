
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${empty sampleModelList}">
        <p>sample list is empty.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
                <tr>
                    <th>Last Name</th>
                    <th>First Name</th>
                    <th>E-Mail</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="sampleModel" items="${sampleModelList}">
                    <tr>
                        <td>${sampleModel.lastName}</td>
                        <td>${sampleModel.firstName}</td>
                        <td>${sampleModel.email}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>