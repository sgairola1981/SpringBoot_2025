var CALdays=new Array('Sun','Mon','Tue','Wed','Thu','Fri','Sat');
var CALmonths=new Array('Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
var CALfmonths=new Array('January','February','March','April','May','June','July','August','September','October','November','December');
var CALmaster=new Array(
	new Array(1,8,22, 1,28,-1),
	new Array(1,8,29, 1,-1,-1),
	new Array(2,9,23, 2,29, 5),
	new Array(2,9,30, 2,-1, 5),
	new Array(0,2,23, 2,29,-1),
	new Array(30,2,23, 2,-1,-1),
	new Array(3,10,24, 3,30, 4),
	new Array(3,10,31, 3,-1, 4),
	new Array(0,3,24,  3,30,-1),
	new Array(31,3,24,  3,-1,-1),
	new Array(4,11,25, 4,-1, 3),
	new Array(0,4,25,  4,-1,-1),
	new Array(5,12,26, 5,-1, 2),
	new Array(0,5,26,  5,-1,-1),
	new Array(6,13,27, 6,-1, 1),
	new Array(0,6,27,  6,-1,-1),
	new Array(7,14,28, 7,-1, 0),
	new Array(0,7,28,  7,-1,-1)
);
var CALbuff='';
var monthDisable = false;
var firstDayOfMonth = false;
var lastDayOfMonth = false;
function CALwriteln(txt) { CALbuff += txt + '\n'; }
function CALwrite(txt) { CALbuff += txt; }
function CALflush(doc) { doc.write(CALbuff); CALbuff = ''; }
function CALgenMo(doc, start, end) {
	/* ORIGINAL CODE START 
	 	for (i=start; i<end; i++) {
	CALwrite('<a class="MO" href="javascript:monthSet(' + i + ');">' + (i+1) + '</a>');
		if (i<end-1) CALwrite('&nbsp;');
	} 
	 ORIGINAL CODE END */
	
/*  FA CHANGES  START  IN ORDER TO GET THE SPECIFIC MONTH
	if(monthDisable == false)
{
	for (i=start; i<end; i++) {
	CALwrite('<a class="MO" href="javascript:monthSet(' + i + ');">' + (i+1) + '</a>');
		if (i<end-1) CALwrite('&nbsp;');
	}
}
else
    {
    	 var str=top.document.frames[1].document.forms[0].MM.value - 1
   
   	CALwrite('<a class="MO" href="javascript:monthSet(' + i + ');">'+ ""+ '</a>');
     }

}
	// disable the month  - for FA -> Master -> Week Start End Date Screen
FA CHANGED END*/
	
if(monthDisable == false)
{
	for (i=start; i<end; i++) {
	CALwrite('<a class="MO" href="javascript:monthSet(' + i + ');">' + (i+1) + '</a>');
		if (i<end-1) CALwrite('&nbsp;');
	}
}
else
    {
    	 //var str=top.document.frames[1].document.forms[0].MM.value - 1
         
   	CALwrite('<a class="MO" href="javascript:monthSet(' + i + ');">'+ ""+ '</a>');
     }

}
function CALgenSt(doc, w, b) {
	ret='var ma=new Array(';
	doc.writeln('div.M {position: absolute; top: 65px; visibility: visible;}');
	for (i=0; i<CALdays.length; i++) {
		doc.writeln('#M' + i + ' {left: ' + (i*w+b) + 'px;}');
	}
	doc.writeln('div.C {position: absolute; top: 85; left: 0px; visibility: hidden; width: ' + w + ';}');
	for (i=0; i<CALmaster.length; i++) {
		ret += 'new Array("C' + i + '", ' + CALmaster[i][3] + ',' + CALmaster[i][4] + ',' + CALmaster[i][5] + ')\n';
		if (i!=CALmaster.length-1) ret += ',';
	}
	ret += ');';
	return ret;
}
function CALgen(doc) {
	//alert(lastDayOfMonth);
	for (i=0; i<CALmaster.length; i++) {
		CALwriteln('<DIV CLASS="C" ID="C' + i + '">');
		if (CALmaster[i][0] != 0) 
		{	if(firstDayOfMonth)		
				CALwrite('<a class="DA" href="javascript:done(' + CALmaster[0][0] + ')">' + CALmaster[i][0] + '</a>');
			else if(lastDayOfMonth)
				CALwrite('<a class="DA" href="javascript:done1()">' + CALmaster[i][0] + '</a>');	
			else
			CALwrite('<a class="DA" href="javascript:done(' + CALmaster[i][0] + ')">' + CALmaster[i][0] + '</a>');
		}
		CALwriteln('<BR>');
		for (j=CALmaster[i][1]; j<=CALmaster[i][2]; j+=7)
		{
		if(firstDayOfMonth)
			CALwriteln('<a class="DA" href="javascript:done(' + CALmaster[0][0] + ')">' + j + '</a><BR>');
		else if(lastDayOfMonth)
			CALwriteln('<a class="DA" href="javascript:done1()">' + j + '</a><BR>');
		else
			CALwriteln('<a class="DA" href="javascript:done(' + j + ')">' + j + '</a><BR>');
		}
		CALwriteln('</DIV>');
		
	}
}
var CALwin=null;
function CALgo (callback,posx,posy,sy,ey,yr,mo) {
	width=30; base=20;
	now=new Date();
	if (!sy) sy=now.getYear();
	if (!ey) ey=now.getYear();
	if (yr) thisYear=yr; else thisYear=(now.getYear()); if (thisYear<999) thisYear+=1900;
	if (mo) thisMonth=mo-1; else thisMonth=(now.getMonth()); if (thisMonth>11) thisMonth=11;
	if (CALwin!=null && !CALwin.closed) CALwin.close();
	CALwin=window.open("", "Calendar",
		"menubar=no,toolbar=no,directories=no,width=240,height=160,left="+posx+",top="+posy);
	CALwin.focus();
	doc=CALwin.document;
	doc.clear();
	doc.open();
	doc.writeln('<html><head>');
	doc.writeln('<link rel="STYLESHEET" type="text/css" href="scripts/calendar.css">');
	doc.writeln('<style>');
	mstr = CALgenSt(doc,width,base);
	doc.writeln('</style><' + 'script language=JavaScript>');
	doc.writeln('var width=' + width + '; var base=' + base + '; var curr=new Array("","","","","","","");' + mstr);
	doc.writeln('var mdays=new Array(31,28,31,30,31,30,31,31,30,31,30,31);');
	if (navigator.appName == "Netscape") {
		if (!document.layers) {
			doc.writeln('function vis(l,v) {document.getElementById(l).style.visibility=v;}');
			doc.writeln('function xloc(l,x) {document.getElementById(l).style.left=x;}');
		} else {
			doc.writeln('function vis(l,v) {document.layers[l].visibility=v;}');
			doc.writeln('function xloc(l,x) {document.layers[l].left=x;}');
		}
	} else {
		doc.writeln('function vis(l,v) {document.all[l].style.visibility=v;}');
		doc.writeln('function xloc(l,x) {document.all[l].style.left=x;}');
	}
	doc.writeln('function getDays(y,m) {ret=mdays[m];if(m==1&&y%4==0) return 29;return ret;}');
	doc.writeln('function done(d) {f=document.forms[0];y=f.year.options[f.year.selectedIndex].text;m=f.month.selectedIndex+1;window.opener.' + callback + '(y,m,d);window.close();}');
	doc.writeln('function done1() {f=document.forms[0];y=f.year.options[f.year.selectedIndex].text;m=f.month.selectedIndex+1;d=getDays(y,m-1);window.opener.' + callback + '(y,m,d);window.close();}');
	doc.writeln('function yearChange(delta) {f=document.forms[0];optl=f.year.options.length;seli=f.year.selectedIndex;newi=seli+delta;if (newi>0 && newi<optl) f.year.options[newi].selected=true;r();}');
	doc.writeln('function yearSet(y) {f=document.forms[0];f.year.options[y].selected=true;r();}');
	doc.writeln('function monthSet(mon) {f=document.forms[0];f.month.options[mon].selected=true;r();}');
	doc.writeln('function clear() {for(p=0;p<7;p++){if(curr[p]!=\'\')vis(curr[p],\'hidden\');}}');
	doc.writeln('function show(l,p,x) {curr[p]=l;xloc(l,x);vis(l,\'visible\');}');
	doc.writeln('function r() { \
		f=document.forms[0];y=f.year.options[f.year.selectedIndex].text;m=f.month.selectedIndex; \
		dt=new Date(y,m,1);start=dt.getDay();nodays=getDays(y,m); \
		clear(); \
		for (i=0,d=1;d<=7;d++) { \
			pos=(d+start-1)%7; \
			for (;i<ma.length;i++) { \
				if (ma[i][1]==d&&(ma[i][2]==-1||nodays<=ma[i][2])&&(ma[i][3]==-1||start<=ma[i][3])) { \
					show(ma[i][0],pos,pos*width+base); break; \
				} \
			} \
		} \
	}');
	doc.writeln('</' + 'script></' + 'head>');
	CALwriteln('<body onLoad="r()"><form><center>');
	CALwrite('<a href=javascript:yearChange(-1)><img align=bottom src="scripts/l.gif" border=0></a>');
	CALwriteln('<a href="javascript:yearChange(+1)"><img align=middle src="scripts/r.gif" border=0></a>');
	CALwriteln('<select name=year onChange="r()">');
	sely=-1; selm=-1; selyl='';
	for (y=sy; y<=ey; y++) {
		sel='';
		if (y==thisYear) { sel=' selected'; selyl=y + ''; sely=y-sy; }
		CALwriteln('<option' + sel + '>' + y + '</option>');
	}
	CALwrite('</select>');
	
	/* ORIGINAL CODE START */
	// CALwriteln('<select name=month onChange="r()">');
	/* ORIGINAL CODE END */
	
	/*  FA CHANGES  START */
	// disable the month  - for FA -> Master -> Week Start End Date Screen
	if(monthDisable == false ) {
		CALwriteln('<select name=month onChange="r()">');
	}
	else {
		thisMonth = top.document.frames[1].document.forms[0].MM.value - 1;
		CALwriteln('<select name=month disabled onChange="r()">');
	}
	/*  FA CHANGES  END */
	for (y=0; y<12; y++) {
		sel='';
		if (y==thisMonth) { sel=' selected'; selm=y; }
		CALwriteln('<option' + sel + '>' + CALmonths[y] + '</option>');
	}
	CALwriteln('</select><br>');
	CALgenMo(doc,0,12);
	if (selm!=-1 && sely!=-1) {
		CALwrite('&nbsp;&nbsp;&nbsp;');
		CALwrite('<a class="MO" href="javascript:monthSet(' + selm + ');yearSet(' + sely + ');">(' +
			CALmonths[selm] + '-' + selyl.substring(2,4) + ')</a>');
	}
	CALwriteln('</center><br>');
	CALwriteln('<DIV CLASS="M" ID="M0"><b class="sun">' + CALdays[0] + '</b></DIV>');
	for (i=1; i<7; i++) CALwriteln('<DIV CLASS="M" ID="M' + i + '"><b class="mon">' + CALdays[i] + '</b></DIV>');
	CALgen(doc);
	CALwriteln('</html>');
	CALflush(doc);
	doc.close();
}
var CALentry=null;
var CALformatstr='';
function CALparse(string, format) {
	delims=new Array('/','-',' ');
	for (i=0;i<delims.length;i++) { if (format.indexOf(delims[i]) > 0) break; }
	if (i==delims.length) return null;
	ftoks=format.split(delims[i]);
	stoks=string.split(delims[i]);
	for (i=0; i<stoks.length; i++) {
		while (stoks[i].length>1 && stoks[i].charAt(0)=='0')
			stoks[i]=stoks[i].substring(1,stoks[i].length);
	}
	y=-1; m=-1; d=1;
	for (i=0; i<ftoks.length; i++) {
		if (ftoks[i].charAt(0).toLowerCase()=='m' && i<stoks.length) {
			m=parseInt(stoks[i]);
			if (m+''=='NaN') {
				tmp=stoks[i].substring(0,1).toUpperCase()+stoks[i].substring(1,3).toLowerCase();
				for (j=0; j<CALmonths.length; j++) {
					if (CALmonths[j]==tmp) { m=j; break; }
				}
			} else if (m>=1 || m<=12) m -= 1;
			else m=-1;
		} else if (ftoks[i].charAt(0).toLowerCase()=='y' && i<stoks.length) {
			y=parseInt(stoks[i]);
			if (y+''!='NaN') { if (y<40) y+=2000; else if (y<100) y+=1900; }
			else y=-1;
		} else if (ftoks[i].charAt(0).toLowerCase()=='d' && i<stoks.length) {
			d=parseInt(stoks[i]);
			if (d+''=='NaN') { d=-1; }
			else if (d<1 || d>31) d=-1;
		}
	}
	if (d==-1 || m==-1 || y==-1) return null;
	return (new Date(y,m,d));
}
function CALformat(y,m,d,format) {

	ret='';
	if (m<1 || m>12) return ret;
	delims=new Array('/','-',' ');
	for (i=0;i<delims.length;i++) { if (format.indexOf(delims[i]) > 0) break; }
	if (i==delims.length) { ftoks=new Array(format); delim=' '; }
	else { delim=delims[i]; ftoks=format.split(delim); }
	for (i=0;i<ftoks.length;i++) {
		if (i!=0) ret+=delim;
		tok=ftoks[i].toLowerCase();
		if (tok=='mm')
		{ 
		//* start* chages done for displaying the mon in two digits 
			  if(m > 0 && m <10)
	 	    	      m = '0'+m;	
	        //*end  
	 	    ret += m;
	        }	
		else if (ftoks[i]=='MON') ret += CALmonths[m-1].toUpperCase();
		else if (ftoks[i]=='MONTH') ret += CALfmonths[m-1].toUpperCase();
		else if (tok=='mon') ret += CALmonths[m-1];
		else if (tok=='month') ret += CALfmonths[m-1];
		else if (tok=='dd') ret += d;
		else if (tok=='yy') ret += y.substring(2,4);
		else if (tok=='yyyy') ret += y;
	     	
	}
	return ret;
}
function CALbind (entry,event,format,yfrom,yto) {
	CALentry=entry;
	CALformatstr=format;
	dt=CALparse(entry.value, format);
	sx=0; sy=0;
	if (event) sx=event.screenX; if (sx==0) sx=300; else sx += 5; if (sx>550) sx=sx-225;
	if (event) sy=event.screenY; if (sy==0) sy=100; else sy += 5; if (sy>400) sy=sy-180;
	if (dt==null) CALgo('CALcallback',sx,sy,yfrom,yto);
	else CALgo('CALcallback',sx,sy,yfrom,yto,dt.getYear(),dt.getMonth()+1);
}
/* made for fa payments screen */ 
function CALbind_modify (entry,event,format,yfrom,yto) {
	if(document.forms[0].MODIFY != undefined) {
		 modify  = document.forms[0].MODIFY.value;
		if(modify=="") {
			alert(mod_mesg);
			return;
		}else {
			CALentry=entry;
			CALformatstr=format;
			dt=CALparse(entry.value, format);
			sx=0; sy=0;
			if (event) sx=event.screenX; if (sx==0) sx=300; else sx += 5; if (sx>550) sx=sx-225;
			if (event) sy=event.screenY; if (sy==0) sy=100; else sy += 5; if (sy>400) sy=sy-180;
			if (dt==null) CALgo('CALcallback',sx,sy,yfrom,yto);
			else CALgo('CALcallback',sx,sy,yfrom,yto,dt.getYear(),dt.getMonth()+1);
		}
	} else {
		CALentry=entry;
		CALformatstr=format;
		dt=CALparse(entry.value, format);
		sx=0; sy=0;
		if (event) sx=event.screenX; if (sx==0) sx=300; else sx += 5; if (sx>550) sx=sx-225;
		if (event) sy=event.screenY; if (sy==0) sy=100; else sy += 5; if (sy>400) sy=sy-180;
		if (dt==null) CALgo('CALcallback',sx,sy,yfrom,yto);
		else CALgo('CALcallback',sx,sy,yfrom,yto,dt.getYear(),dt.getMonth()+1);
	}
}

function CALcallback (y,m,d) {
	if (CALentry) CALentry.value=CALformat(y,m,d,CALformatstr);
	if(!CALentry.disabled&&!CALentry.readOnly)
		CALentry.focus();
}
