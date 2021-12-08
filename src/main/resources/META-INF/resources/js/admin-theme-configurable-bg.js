var showConfiguredControlPanelBackground = function(event){
	var conf = document.querySelector("[data-identifier=\"configurableBackground\"]");
	if(conf == null) {
		return;
	}
	var configurableBackgroundText = conf.dataset.text;
	var configurableBackgroundColor = conf.dataset.color;
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
		context.fillStyle = configurableBackgroundColor;
		context.fillRect(0, 0, canvas.width, canvas.height);
		context.translate(0, canvas.height - fontSize - 5 );
		context.rotate(angle * (Math.PI / 180));
		context.fillStyle    = 'rgba(0,0,0,0.1)';
		context.font         = fontSize + 'px sans-serif';
		context.fillText(configurableBackgroundText, 0, fontSize);
	
		adminthemebackgroundbody.style.background = "url(" + canvas.toDataURL("image/png")+ ")";

		var setSheetOpacity = function() {
			var selectors = [".sheet", ".list-group-card", ".list-group-item", ".main-content-card", 
				".panel:not(.panel-secondary)", ".card", ".bg-white", ".contributor-container", 
				"iframe", "table", "#wrapper"];
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
};

Liferay.on("endNavigate", showConfiguredControlPanelBackground);
Liferay.on("allPortletsReady", showConfiguredControlPanelBackground);

