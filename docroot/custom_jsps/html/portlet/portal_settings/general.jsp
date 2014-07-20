<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="general.portal.jsp" %>

<h3><liferay-ui:message key="hsts.configuration" /></h3>
<%
String hstsAllValue  = PrefsPropsUtil.getString(company.getCompanyId(), "hsts.all", "0");
String hstsUserValue = PrefsPropsUtil.getString(company.getCompanyId(), "hsts.user", "0");
Boolean hstsIncludeSubdomain = 
                       PrefsPropsUtil.getBoolean(company.getCompanyId(), "hsts.include.subdomain", false);
%>

<aui:row>
	<aui:col width="<%= 50 %>">
		<aui:input name="settings--hsts.all--"  value="<%=hstsAllValue%>" type="text">
			<aui:validator name="number"/>
		</aui:input>

		<aui:input name="settings--hsts.user--" value="<%=hstsUserValue%>" type="text">
			<aui:validator name="number"/>
		</aui:input>

		<aui:input name="settings--hsts.include.subdomain--" value="<%=hstsIncludeSubdomain%>" type="checkbox">
		</aui:input>
	</aui:col>
	<aui:col width="<%= 50 %>">
		<p>
			<liferay-ui:message key="hsts.configuration.help.basic"/>
		</p>
		<p>
			<liferay-ui:message key="hsts.configuration.help.certificate"/>
		</p>
	</aui:col>
</aui:row>