<%@ page import="com.health.types.ErrorType" %>
<%@ page import="com.google.common.base.Strings" %>
<%@ page import="org.codehaus.jackson.map.ObjectMapper" %>
<%@ page contentType="application/json" pageEncoding="UTF-8" session="false" trimDirectiveWhitespaces="true" isErrorPage="true"  %>
<%
final ObjectMapper objectMapper = new ObjectMapper();
final ErrorType error = new ErrorType();
final Object errorMessage = request.getAttribute("javax.servlet.error.message");

if (errorMessage != null && !Strings.isNullOrEmpty((String)errorMessage)) {
    error.setMessage((String)errorMessage);
}
else if (exception != null && !Strings.isNullOrEmpty(exception.getMessage())) {
    error.setMessage(exception.getMessage().replaceAll("\"", "'"));
}
else {
    error.setMessage(""); // an empty message
}
%>
<%= objectMapper.writeValueAsString(error) %>