package com.liferay.sales.configurableadmintheme;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(
		id = "com.liferay.sales.configurableadmintheme.ConfigurableAdminBackgroundConfiguration"
	    , localization = "content/Language"
	    , name = "configurable-admin-background-name"
	    , description = "configurable-admin-background-description"
	)
public interface ConfigurableAdminBackgroundConfiguration {

	@Meta.AD(
            deflt = "${instance} ${version} ${dxpcname}-${dxpcenv}",
			description = "configurable-admin-background-text-description",
            name = "configurable-admin-background-text-name",
            required = false
        )
	public String text();
	
	@Meta.AD(
            deflt = "#eeeeee",
			description = "configurable-admin-background-color-description",
            name = "configurable-admin-background-color-name",
            required = false
        )
	public String color();
		
	@Meta.AD(
            deflt = "440",
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
            deflt = "true",
			description = "configurable-admin-background-show-in-control-menu-description",
            name = "configurable-admin-background-show-in-control-menu-name",
            required = false
        )
	public Boolean showInControlMenu();
	
	@Meta.AD(
            deflt = "false",
			description = "configurable-admin-background-show-on-regular-page-description",
            name = "configurable-admin-background-show-on-regular-page-name",
            required = false
        )
	public Boolean showOnRegularPage();
}
