var showConfiguredControlPanelBackground = async function(event){
  try {
	var conf = document.querySelector("[data-identifier=\"configurableBackground\"]");
	if(conf == null) {
		return;
	}
	var configurableBackgroundText = conf.dataset.text;
	var configurableBackgroundColor = conf.dataset.bodybgcolor;
	var configurableControlMenuColorDark = conf.dataset.controlmenucolordark;
	var configurableControlMenuColorLight = conf.dataset.controlmenucolorlight;
	var configurableBackgroundHeight = conf.dataset.height;
	var configurableBackgroundWidth = conf.dataset.width;
	var configurableBackgroundOpacity = conf.dataset.opacity;

	if(configurableBackgroundWidth > 0) {
		var adminthemebackgroundbody = document.querySelector("body");
	
		var canvas = document.createElement("canvas");
		var fontSize = 24;
		var angle = -5;
		canvas.setAttribute('height', configurableBackgroundHeight);
		canvas.setAttribute('width', configurableBackgroundWidth);

		var context = canvas.getContext('2d');
		if(configurableBackgroundColor != null) {
			context.fillStyle = configurableBackgroundColor;
		}
		context.fillRect(0, 0, canvas.width, canvas.height);
		context.translate(0, canvas.height - fontSize - 5 );
		context.rotate(angle * (Math.PI / 180));
		context.fillStyle    = 'rgba(0,0,0,0.15)';
		context.font         = fontSize + 'px sans-serif';
		context.fillText(configurableBackgroundText, 0, fontSize);

		adminthemebackgroundbody.style.background = "url(" + canvas.toDataURL("image/png")+ ")";

		var colorizedArea;
		if(configurableControlMenuColorLight != null) {
			colorizedArea = document.querySelector(".control-menu-level-1-light");
			if(colorizedArea != null) {
				colorizedArea.style.backgroundColor = configurableControlMenuColorLight;
			}
//			colorizedArea = document.querySelector(".management-bar-light");
//			if(colorizedArea != null) {
//				colorizedArea.style.backgroundColor = configurableControlMenuColorLight;
//			}
		}
		if(configurableControlMenuColorDark != null) {
			colorizedArea = document.querySelector(".control-menu-level-1-dark");
			if(colorizedArea != null) {
				colorizedArea.style.backgroundColor = configurableControlMenuColorDark;
			}
			colorizedArea = document.querySelector("#_com_liferay_product_navigation_product_menu_web_portlet_ProductMenuPortlet_site_administrationHeading");
			if(colorizedArea != null) {
				colorizedArea.style.backgroundColor = configurableControlMenuColorDark;
			}
			colorizedArea = document.querySelector("#p_p_id_com_liferay_product_navigation_product_menu_web_portlet_ProductMenuPortlet_ .sidebar-header");
			if(colorizedArea != null) {
				colorizedArea.style.backgroundColor = configurableControlMenuColorDark;
			}
//			colorizedArea = document.querySelector(".management-bar-dark");
//			if(colorizedArea != null) {
//				colorizedArea.style.backgroundColor = configurableControlMenuColorDark;
//			}
		}
		var setSheetOpacity = function() {
			var selectors = [".sheet", ".list-group-card", ".list-group-item", ".main-content-card", 
				".panel:not(.panel-secondary)", ".card", ".bg-white", ".contributor-container", 
				"iframe", "table", ".data-set-content-wrapper"];
			selectors.forEach(function(selector){
				sheets = document.querySelectorAll(selector);
				sheets.forEach(function(sheet) {
					sheet.style.opacity = configurableBackgroundOpacity + "%";
				});
			})
		}
		setSheetOpacity();
		// some elements are rendered asynchronously
		// quick & stupid hack - good enough for this purpose
		setTimeout(setSheetOpacity, 3000);
	}
  } catch(e) {
	  console.log("ignoring " + e + " for configurable background");
  }
};

Liferay.on("endNavigate", showConfiguredControlPanelBackground);
Liferay.on("allPortletsReady", showConfiguredControlPanelBackground);

