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
            deflt = "configurable system indicator",
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
            deflt = "420",
			description = "configurable-admin-background-width-description",
            name = "configurable-admin-background-width-name",
            required = false
        )
	public Integer width();
		
	@Meta.AD(
            deflt = "360",
			description = "configurable-admin-background-height-description",
            name = "configurable-admin-background-height-name",
            required = false
        )
	public Integer height();

	@Meta.AD(
            deflt = "60",
			description = "configurable-admin-background-opacity-description",
            name = "configurable-admin-background-opacity-name",
            required = false
        )
	public Integer opacity();
}
