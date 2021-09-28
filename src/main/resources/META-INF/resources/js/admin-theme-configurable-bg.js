var showConfiguredControlPanelBackground = function(event){
//	console.log("configurable bg firing");
	var conf = document.querySelector("a[data-identifier=\"configurableBackground\"]");
	if(conf == null) {
//		console.log("nothing to configure");
		return;
	}
	var configurableBackgroundText = conf.dataset.text;
	var configurableBackgroundColor = conf.dataset.color;
	var configurableBackgroundHeight = conf.dataset.height;
	var configurableBackgroundWidth = conf.dataset.width;
	var configurableBackgroundOpacity = conf.dataset.opacity;

//	console.log(configurableBackgroundText + " " + configurableBackgroundColor + " " + configurableBackgroundHeight + " " + configurableBackgroundWidth)
	
	if(configurableBackgroundWidth > 0 && Liferay.ThemeDisplay.isControlPanel()) {
		var adminthemebackgroundbody = document.querySelector("body");
	
		var canvas = document.createElement("canvas");
		var fontSize = 24;
		var angle = -10;
		canvas.setAttribute('height', configurableBackgroundHeight);
		canvas.setAttribute('width', configurableBackgroundWidth);

		var context = canvas.getContext('2d');
		context.fillStyle = configurableBackgroundColor;
		context.fillRect(0, 0, canvas.width, canvas.height);
		context.translate(0, canvas.height - fontSize);
		context.rotate(angle * (Math.PI / 180));
		context.fillStyle    = 'rgba(0,0,0,0.1)';
		context.font         = fontSize + 'px sans-serif';
		context.fillText(configurableBackgroundText, 0, fontSize);
	
		adminthemebackgroundbody.style.background = "url(" + canvas.toDataURL("image/png")+ ")";

		var setSheetOpacity = function() {
			var selectors = [".sheet", ".list-group-card", ".list-group-item", ".main-content-card", 
				".panel:not(.panel-secondary)", ".card", ".bg-white", ".contributor-container", 
				"iframe", "table"];
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

