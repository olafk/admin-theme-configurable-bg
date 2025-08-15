package de.olafkock.liferay.configurableadminbackground;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
		category = "look-and-feel",
		scope = ExtendedObjectClassDefinition.Scope.GROUP
		)
@Meta.OCD(
		id = "de.olafkock.liferay.configurableadminbackground.ConfigurableAdminBackgroundConfiguration"
	    , localization = "content/Language"
	    , name = "configurable-admin-background-name"
	    , description = "configurable-admin-background-description"
	)
public interface ConfigurableAdminBackgroundConfiguration {

	@Meta.AD(
            deflt = "${host} ${instance} ${version} ${group}",
			description = "configurable-admin-background-text-description",
            name = "configurable-admin-background-text-name",
            required = false
        )
	public String text();
	
	@Meta.AD(
            deflt = "#eeeeee",
			description = "configurable-admin-background-bodybgcolor-description",
            name = "configurable-admin-background-bodybgcolor-name",
            required = false
        )
	public String bodyBgColor();
	
	@Meta.AD(
            deflt = "",
			description = "configurable-admin-background-controlmenucolordark-description",
            name = "configurable-admin-background-controlmenucolordark-name",
            required = false
        )
	public String controlMenuColorDark();
	
	@Meta.AD(
            deflt = "", 
			description = "configurable-admin-background-controlmenucolorlight-description",
            name = "configurable-admin-background-controlmenucolorlight-name",
            required = false
        )
	public String controlMenuColorLight();
		
	@Meta.AD(
            deflt = "500",
			description = "configurable-admin-background-width-description",
            name = "configurable-admin-background-width-name",
            required = false
        )
	public Integer width();
		
	@Meta.AD(
            deflt = "150",
			description = "configurable-admin-background-height-description",
            name = "configurable-admin-background-height-name",
            required = false
        )
	public Integer height();

	@Meta.AD(
            deflt = "80",
			description = "configurable-admin-background-opacity-description",
            name = "configurable-admin-background-opacity-name",
            required = false
        )
	public Integer opacity();
	
	@Meta.AD(
            deflt = "false",
			description = "configurable-admin-background-show-on-regular-page-description",
            name = "configurable-admin-background-show-on-regular-page-name",
            required = false
        )
	public Boolean showOnRegularPage();
}
