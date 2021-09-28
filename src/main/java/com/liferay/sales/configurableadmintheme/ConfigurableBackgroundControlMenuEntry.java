package com.liferay.sales.configurableadmintheme;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.product.navigation.control.menu.BaseProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.constants.ProductNavigationControlMenuCategoryKeys;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author olaf
 */

@Component(
	immediate = true,
	configurationPid = "com.liferay.sales.configurableadmintheme.ConfigurableAdminBackgroundConfiguration",
	property = {
		"product.navigation.control.menu.category.key=" + ProductNavigationControlMenuCategoryKeys.USER,
		"product.navigation.control.menu.entry.order:Integer=100"
	},
	service = ProductNavigationControlMenuEntry.class
)
public class ConfigurableBackgroundControlMenuEntry 
	extends BaseProductNavigationControlMenuEntry
	implements ProductNavigationControlMenuEntry {

	@Override
	public boolean isShow(HttpServletRequest request) throws PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		return themeDisplay.getTheme().isControlPanelTheme();
	}

	@Override
	public Map<String, Object> getData(HttpServletRequest httpServletRequest) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("identifier", "configurableBackground");
		result.put("text", HtmlUtil.escapeAttribute(_backgroundConfiguration.text()));
		result.put("color", HtmlUtil.escapeAttribute(_backgroundConfiguration.color()));
		result.put("height", ""+_backgroundConfiguration.height());		
		result.put("width", ""+_backgroundConfiguration.width());		
		result.put("opacity", ""+_backgroundConfiguration.opacity());		
		return result;
	}
	
	@Override
	public String getLabel(Locale locale) {
		return "Configurable Background";
	}

	@Override
	public String getURL(HttpServletRequest httpServletRequest) {
		return "/group/control_panel/manage?p_p_id=com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet&"
				+ "p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&"
				+ "_com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_factoryPid=com.liferay.sales.configurableadmintheme.ConfigurableAdminBackgroundConfiguration&"
				+ "_com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_mvcRenderCommandName=%2Fconfiguration_admin%2Fedit_configuration&"
				+ "_com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_pid=com.liferay.sales.configurableadmintheme.ConfigurableAdminBackgroundConfiguration";
	}

	@Override
		public String getIcon(HttpServletRequest httpServletRequest) {
			return "color-picker";
		}
	
	
	@Activate
	@Modified
	protected void activate(Map<Object, Object> properties) {
		_backgroundConfiguration = ConfigurableUtil.createConfigurable(ConfigurableAdminBackgroundConfiguration.class, properties);
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

}
