package de.olafkock.liferay.configurableadminbackground;

import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.servlet.taglib.BaseDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate=true,
		service=DynamicInclude.class
)
public class ConfigurableAdminThemeDynamicInclude extends BaseDynamicInclude {

	@Override
	public void include(HttpServletRequest request, HttpServletResponse response, String key)
			throws IOException {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		ConfigurableAdminBackgroundConfiguration backgroundConfiguration;
		try {
			backgroundConfiguration = _configurationProvider.getGroupConfiguration(
					ConfigurableAdminBackgroundConfiguration.class, themeDisplay.getScopeGroupId());
		} catch (ConfigurationException e) {
			_log.warn(e);
			backgroundConfiguration = new ConfigurableAdminBackgroundConfiguration() {
				
				@Override
				public Integer width() {
					return 500;
				}
				
				@Override
				public String text() {
					return e.getClass().getName() + " " + e.getMessage();
				}
				
				@Override
				public Boolean showOnRegularPage() {
					return false;
				}
				
				@Override
				public Integer opacity() {
					return 90;
				}
				
				@Override
				public Integer height() {
					return 200;
				}
				
				@Override
				public String bodyBgColor() {
					return "#ffdddd";
				}

				@Override
				public String controlMenuColorDark() {
					return "";
				}

				@Override
				public String controlMenuColorLight() {
					return "";
				}
			};
		}
		String text = HtmlUtil.escapeAttribute(backgroundConfiguration.text());
		String bodyBgColor = "";
		String controlMenuColorDark = "";
		String controlMenuColorLight = "";
		try {
			text = text.replace("${version}", HtmlUtil.escapeAttribute(ReleaseInfo.getVersionDisplayName()));
			text = text.replace("${group}", HtmlUtil.escapeAttribute(themeDisplay.getScopeGroupName()));
			text = text.replace("${instance}", HtmlUtil.escapeAttribute(companyLocalService.getCompany(themeDisplay.getCompanyId()).getShortName()));
			text = text.replace("${host}", HtmlUtil.escapeAttribute(""+request.getHeader("Host")));
			bodyBgColor = HtmlUtil.escapeAttribute(backgroundConfiguration.bodyBgColor());
			controlMenuColorDark = HtmlUtil.escapeAttribute(backgroundConfiguration.controlMenuColorDark());
			controlMenuColorLight = HtmlUtil.escapeAttribute(backgroundConfiguration.controlMenuColorLight());
		} catch (PortalException e) {
		}
		
		boolean showAdminBackground = themeDisplay.getTheme().isControlPanelTheme() || backgroundConfiguration.showOnRegularPage();
		if(themeDisplay.getUser().getExpandoBridge().hasAttribute("adminBackground")) {
			showAdminBackground &= (Boolean)(themeDisplay.getUser().getExpandoBridge().getAttribute("adminBackground"));
		}

		if(showAdminBackground) {
			PrintWriter printWriter = response.getWriter();
			printWriter.print("<meta ");
			printWriter.print("data-identifier=\"configurableBackground\" ");
			printWriter.print("data-text=\"" + dxpcDetect(text, request) + "\" ");
			printWriter.print("data-height=\"" + Math.max(50, backgroundConfiguration.height()) + "\" ");		
			printWriter.print("data-width=\"" + Math.max(50, backgroundConfiguration.width()) + "\" ");		
			printWriter.print("data-opacity=\"" + Math.max(60, backgroundConfiguration.opacity()) + "\" ");
			if(bodyBgColor.length()>0) {
				printWriter.print("data-bodybgcolor=\"" + bodyBgColor + "\" ");
			}
			// ControlMenu colors are tricky to get right - leave an option to not set them at all
			if(controlMenuColorDark.length()>0) {
				printWriter.print("data-controlmenucolordark=\"" + controlMenuColorDark + "\" ");
			}
			if(controlMenuColorLight.length()>0) {
				printWriter.print("data-controlmenucolorlight=\"" + controlMenuColorLight + "\" ");
			}
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

	@Reference
	private CompanyLocalService companyLocalService;

	@Reference
	private ConfigurationProvider _configurationProvider;
		
	private static final Log _log = LogFactoryUtil.getLog(ConfigurableAdminThemeDynamicInclude.class);
}
