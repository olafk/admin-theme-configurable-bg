package com.liferay.sales.configurableadmintheme;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.servlet.taglib.BaseDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate=true,
		configurationPid = "com.liferay.sales.configurableadmintheme.ConfigurableAdminBackgroundConfiguration",
		service=DynamicInclude.class
)
public class ConfigurableAdminThemeDynamicInclude extends BaseDynamicInclude {

	@Override
	public void include(HttpServletRequest request, HttpServletResponse response, String key)
			throws IOException {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		String text = _text;
		try {
			text = _text.replace("${group}", HtmlUtil.escapeAttribute(themeDisplay.getScopeGroupName()));
			text = text.replace("${instance}", HtmlUtil.escapeAttribute(companyLocalService.getCompany(themeDisplay.getCompanyId()).getShortName()));
			text = text.replace("${host}", HtmlUtil.escapeAttribute(""+request.getHeader("Host")));
		} catch (PortalException e) {
		}
		if(themeDisplay.getTheme().isControlPanelTheme() || _backgroundConfiguration.showOnRegularPage()) {
			PrintWriter printWriter = response.getWriter();
			printWriter.print("<meta ");
			printWriter.print("data-identifier=\"configurableBackground\" ");
			printWriter.print("data-text=\"" + dxpcDetect(text, request) + "\" ");
			printWriter.print("data-color=\"" + _color + "\" ");
			printWriter.print("data-height=\"" + Math.max(50, _backgroundConfiguration.height()) + "\" ");		
			printWriter.print("data-width=\"" + Math.max(50, _backgroundConfiguration.width()) + "\" ");		
			printWriter.print("data-opacity=\"" + Math.max(60, _backgroundConfiguration.opacity()) + "\" ");
			printWriter.print("/>");
		}
	}
	
	private String dxpcDetect(String text, HttpServletRequest request) {
		try {
			String host = request.getHeader("Host");
			if(host!=null && host.startsWith("webserver-lct")) {
				String namePart = host.substring("webserver-lct".length(), host.indexOf('.'));
				String envPart = namePart.substring(namePart.indexOf('-')+1);
				namePart = namePart.substring(0,namePart.indexOf('-'));
				return text.replace("${dxpcname}", namePart).replace("${dxpcenv}", envPart).trim();
			}
		} catch(Throwable e) {
			// if anything goes wrong, just return what we got - dxpc detection failed.
			// it's only string operations, nothing bad could have happened apart from 
			// wasting a few CPU cycles.
		}
		return text.replace("${dxpcname}", "").replace("${dxpcenv}", "").trim();
	}
	

	@Override
	public void register(DynamicIncludeRegistry dynamicIncludeRegistry) {
		dynamicIncludeRegistry.register("/html/common/themes/top_head.jsp#pre");		
	}
	
	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		_backgroundConfiguration = ConfigurableUtil.createConfigurable(ConfigurableAdminBackgroundConfiguration.class, properties);
		String version = ReleaseInfo.getVersion();
		try {
			// the _exact_ version (for 7.4 including update) is injected into a private static field in ReleaseInfo...
			// This is intended to be ReleaseInfo.getVersionDisplayName() from DXP 7.4 U3 on (see LPS-144745)
			Class<ReleaseInfo> clazz = ReleaseInfo.class;
			Field f = clazz.getDeclaredField("_VERSION_DISPLAY_NAME");
			f.setAccessible(true);
			version = f.get(null).toString();
			
			version = version.replace(" Update ", ".U"); // shorten "Update" to "u", e.g. 7.4.13.U3 - no need for verbosity on the info
		} catch (Exception e) {
		}
		_text = HtmlUtil.escapeAttribute(_backgroundConfiguration.text().replace("${version}", version));
		_color = HtmlUtil.escapeAttribute(_backgroundConfiguration.color());
	}

	@Reference
	protected void setConfigurationProvider(ConfigurationProvider configurationProvider) {
		_log.info("new configuration detected. Will be handled in @Modified");
		// configuration update will actually be handled in the @Modified event,
		// which will only be triggered in case we have a @Reference to the
		// ConfigurationProvider
	}
	
	@Reference
	private CompanyLocalService companyLocalService;
		
	private static final Log _log = LogFactoryUtil.getLog(ConfigurableBackgroundControlMenuEntry.class);

	private volatile ConfigurableAdminBackgroundConfiguration _backgroundConfiguration;
	private volatile String _text;
	private volatile String _color;

}
