package com.liferay.sales.configurableadmintheme;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.servlet.taglib.BaseDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.PrintWriter;
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

		if(themeDisplay.getTheme().isControlPanelTheme() || _backgroundConfiguration.showOnRegularPage()) {
			PrintWriter printWriter = response.getWriter();
			printWriter.print("<meta ");
			printWriter.print("data-identifier=\"configurableBackground\" ");
			printWriter.print("data-text=\"" + _text + "\" ");
			printWriter.print("data-color=\"" + _color + "\" ");
			printWriter.print("data-height=\"" + _backgroundConfiguration.height() + "\" ");		
			printWriter.print("data-width=\"" + _backgroundConfiguration.width() + "\" ");		
			printWriter.print("data-opacity=\"" + _backgroundConfiguration.opacity() + "\" ");
			printWriter.print("/>");
		}
	}

	@Override
	public void register(DynamicIncludeRegistry dynamicIncludeRegistry) {
		dynamicIncludeRegistry.register("/html/common/themes/top_head.jsp#pre");		
	}
	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		_backgroundConfiguration = ConfigurableUtil.createConfigurable(ConfigurableAdminBackgroundConfiguration.class, properties);
		_text = HtmlUtil.escapeAttribute(_backgroundConfiguration.text().replace("${version}", ReleaseInfo.getVersion()));
		_color = HtmlUtil.escapeAttribute(_backgroundConfiguration.color());
	}

	@Reference
	protected void setConfigurationProvider(ConfigurationProvider configurationProvider) {
		_log.info("new configuration detected. Will be handled in @Modified");
		// configuration update will actually be handled in the @Modified event,
		// which will only be triggered in case we have a @Reference to the
		// ConfigurationProvider
	}
	
	private static final Log _log = LogFactoryUtil.getLog(ConfigurableBackgroundControlMenuEntry.class);

	private volatile ConfigurableAdminBackgroundConfiguration _backgroundConfiguration;
	private volatile String _text;
	private volatile String _color;

}
