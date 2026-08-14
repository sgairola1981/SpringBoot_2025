if (!document.getElementById)
    document.getElementById = function() { return null; }

function initializeMenu(menuId, actuatorId) {
	var menu = document.getElementById(menuId);
    var actuator = document.getElementById(actuatorId);

    if (menu == null || actuator == null) return;

	actuator.onclick = function() {

        var display = menu.style.display;
        menu.style.display = (display == "block") ? "none" : "block";
		fn_expand_contract(this.name);

        return false;
    }
}

function fn_expand_contract(name){
	var img = document.all(name+"_img");
	var src = img.src.substring(img.src.lastIndexOf("/")+1,img.src.length);
	if(src == "plus.gif") {
		img.src=img.src.substring(0,img.src.lastIndexOf("/")+1)+"minus.gif";
	} else {
		img.src=img.src.substring(0,img.src.lastIndexOf("/")+1)+"plus.gif";
	}
}