/* 
  This function is for supporting the multilingual funda
*/
function days_between(formfield1,formfield2)
{
//alert('2');

	var val2=formfield1.value;
	var val3=formfield2.value;
 //alert(val2);
  //alert(val3);
	var l_val2=new Date();
	var l_val3=new Date();
	var elems=val2.split("/");
	l_val2.setMonth(elems[1]-1);
    //  alert(l_val2);
	l_val2.setDate(elems[0]);
    //alert(l_val2);

	l_val2.setYear(elems[2]);
      //alert(l_val2);
	elems=val3.split("/");
  	l_val3.setMonth(elems[1]-1);
  //alert(l_val3);
	l_val3.setDate(elems[0]);
    //  alert(l_val3);
	l_val3.setYear(elems[2]);
      //alert(l_val3);

	var l_res=(l_val3-l_val2)/1000/24/3600;
	return l_res;
}
function shortCutKeysAdd()
{
	//H Key is for Help
	/*
	if(event.keyCode==72)
	{
		var winpop=window.open("helplovadd.html	",'WinHelp','scrollbars=no,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
		return false;
	}*/
	//F10 Key is for Add new record
	//else
	if(event.keyCode==121)
	{
		if(Function2!=undefined)
		{
			if(document.forms[0].add_btn.disabled==false)
				Function2('N');
		}
	}
	//F9 Key is for Last
	else if(event.keyCode==120)
	{
		if(Function2!=undefined)
		{
			if(document.forms[0].save_btn.disabled==false)
				Function2('I');
			return false;
		}
	}
	//F12 Key is for Reset
	else if(event.keyCode==123)
	{
		if(Function2!=undefined)
		{
			if(document.forms[0].cancel_btn.disabled==false)
				Function2('C');
		}
	}
}
function shortCutKeys()
{
	//F2 Key is for search
	if(event.keyCode==113)
	{
		if(Function2!=undefined)
		{
			if(document.forms[0].search_btn.disabled==false)
			{
				Function2('S');
				return false;
			}
		}
	}
	//H Key is for Help
	else if(event.keyCode==72)
	{
		var winpop=window.open("helplovsearch.html	",'WinHelp','scrollbars=no,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
		return false;
	}
	//F7 Key is for Add
	else if(event.keyCode==118)
	{
		if(Fn_Add!=undefined)
		{
			if(document.forms[0].add_btn.disabled==false)
				Fn_Add();
		}
	}
	//F8 Key is for First
	else if(event.keyCode==119)
	{
		if(fn_NextPrev!=undefined)
		{
			if(document.forms[0].first_btn.disabled==false)
				fn_NextPrev('F');
		}
	}
	//F9 Key is for Prev
	else if(event.keyCode==120)
	{
		if(fn_NextPrev!=undefined)
		{
			if(document.forms[0].prev_btn.disabled==false)
				fn_NextPrev('P');
		}
	}
	//F10 Key is for Next
	else if(event.keyCode==121)
	{
		if(fn_NextPrev!=undefined)
		{
			if(document.forms[0].next_btn.disabled==false)
				fn_NextPrev('N');
		}
	}
	//F12 Key is for Reset
	else if(event.keyCode==123)
	{
			if(resetValues!=undefined)
				resetValues();
	}
}
function checkDates(dt1,dt2)
{
	if(dt1==''||dt2=='')
		return true;
	var commencDt=new Date();
	if(dt1!='')
	{
	var fulldt=dt1;
	var elems = dt1.split("/");
	var dt=elems[0];
	var mth=elems[1]-1;
	var yr=elems[2];
	commencDt.setYear(yr);
	commencDt.setMonth(mth);
	commencDt.setDate(dt);
}

var complDt=new Date();
if(dt2!='')
{
		elems = dt2.split("/");
		fulldt=dt2;
		dt=elems[0];
		mth=elems[1]-1;
		yr=elems[2];
		complDt.setYear(yr);
		complDt.setMonth(mth);
		complDt.setDate(dt);
}
if(commencDt>complDt)
{
return false;
}
else
	return true;
}

function SelectMode(mode,clientServer_mode)
{
	var prevMode=document.forms[0].SelectedMode.value;

	if(mode=='S')
	{
		divS.style.display="block";
		divT.style.display="block";
		divA.style.display="none";
		divAB.style.display="none";
		divSB.style.display="block";
		divMB.style.display="none";
		document.forms[0].r1[0].checked=true;
		document.forms[0].r1[2].disabled=false;
		document.forms[0].r1[3].disabled=false;

	}
	else if(mode=='A')
	{
		if(document.forms[0].csDml_mode.value!='modify')
		emptyAdd();
		divA.style.display="block";
		divS.style.display="none";
		divT.style.display="none";
		divSB.style.display="none";
		divMB.style.display="none";
		divAB.style.display="block";
		document.forms[0].r1[1].checked=true;
		document.forms[0].r1[2].disabled=true;
		document.forms[0].r1[3].disabled=true;
	}
	else if(mode=='M')
	{
		
		if(document.forms[0].csSelected_row.value==''||document.forms[0].csSelected_row.value==null)
		{
			alert('Please select a record to modify!!');
			return false;
		}
		if(clientServer_mode=='C')
		populateAddFields();

		divMB.style.display="block";
		divA.style.display="block";
		divS.style.display="none";
		divT.style.display="none";
		divSB.style.display="none";
		divAB.style.display="none";
		document.forms[0].r1[2].checked=true;
	}
	else if(mode=='V')
	{
		if(document.forms[0].rownum.value==0)
		{
			alert('Please select a record to modify!!');
			return false;
		}
		populateAddFields();

		divA.style.display="block";
		divS.style.display="none";
		divT.style.display="none";
		divAB.style.display="none";
		divSB.style.display="none";
		divMB.style.display="none";
		document.forms[0].r1[3].checked=true;
	}
	if(mode!='V')
	{
		makeFieldsReadOnly(false);
	}
	else
	{
		makeFieldsReadOnly(true);
	}
	if(mode!='A' && document.forms[0].csDml_mode.value=='modify')
		document.forms[0].csDml_mode.value='insert';

	document.forms[0].SelectedMode.value=mode;
}


function validFloat(formField)
{
	var result = true;
	var mesg_2='Please enter a valid number for this field';

 	if (result && formField.value!='')
 	{

 		//var num = parseFloat(formField.value,10);
 		if (isNaN(formField.value))
 		{
			alert(mesg_2,mesg_2);
			if(!formField.disabled)
			formField.focus();	
			formField.select();
			result = false;
		}
	} 
	
	return result;
}
function validInt(formField)
{
	var result = true;
	var mesg_2='Please enter a valid integer for this field';

 	if (result && formField.value!='')
 	{

 		var num = parseInt(formField.value);
 		if (isNaN(formField.value))
 		{
			alert(mesg_2,mesg_2);
			if(!formField.disabled)
			formField.focus();	
			formField.select();
			result = false;
		}
	} 
	
	return result;
}

function statusReset(fld)
{
	fld.title="Press Reset button to reset all the fields in the Search Criteria.";
}
function statusSearch(fld)
{
	fld.title="Press Search button to display records.";
}
function statusSave(fld)
{
	fld.title="Press Save button to post the changes into the database.";
}
function statusDelete(fld)
{
	fld.title="Press Delete button to delete the selected records.";
}
function statusNext(fld)
{
	fld.title="Press Next button to display next set of records.";
}
function statusPrev(fld)
{
	fld.title="Press Prev button to display previous set of records.";
}
function statusFirst(fld)
{
	fld.title="Press First button to display first set of records.";
}
function statusLast(fld)
{
	fld.title="Press Last button to display last set of records.";
}
function localeAlert(def,loc)
{
	if(loc!="")
		alert(loc);
	else
		alert(def);
}



function isEmailAddr(email)
{
  var result = false;
  var theStr = new String(email);
  var index = theStr.indexOf("@");
  if (index > 0)
  {
    var pindex = theStr.indexOf(".",index);
    if ((pindex > index+1) && (theStr.length > pindex+1))
	result = true;
  }
  return result;
}

function validRequired(formField,mesg)
{
	var result = true;
	if(mesg=='')
	mesg="This Field is Mandatory for further processing.";
	if (formField.value == "")
	{
		alert(mesg);
		if(!formField.disabled&!formField.readOnly)
		{
			formField.focus();
		}
		result = false;
	}
	
	return result;
}

function allDigits(str)
{
	return inValidCharSet(str,"0123456789");
}




function inValidCharSet(str,charset)
{
	var result = true;
	// Note: doesn't use regular expressions to avoid early Mac browser bugs	
	for (var i=0;i<str.length;i++)
		if (charset.indexOf(str.substr(i,1))<0)
		{
			result = false;
			break;
		}
	
	return result;
}

function validEmail(formField,fieldLabel,required)
{
	var result = true;
	
	if (required && !validRequired(formField,fieldLabel))
		result = false;

	if (result && ((formField.value.length < 3) || !isEmailAddr(formField.value)) )
	{
		localeAlert(errCodeDefault_651,errCode_651);
		if(!formField.disabled)
		formField.focus();
		result = false;
	}
   
  return result;

}

function validNum(formField)
{
	var result = true;
  	var mesg_2='Please enter a valid integer for this field';
 	if (result)
 	{
 		if (!allDigits(formField.value))
 		{
			alert(mesg_2);
			if(!formField.disabled)
			{
				formField.focus();		
				formField.select();		
			}
			result = false;
		}
	} 
	
	return result;
}





function validDate(formField)
{
	var result = true;
	if(formField.value=='')
		result=false;
  
 	if (result)
 	{
 		var elems = formField.value.split("/");
 		
 		result = (elems.length == 3); // should be three components
 		
 		if (result)
 		{
  			var day = parseInt(elems[0],10);
 			var month = parseInt(elems[1],10);
 			var year = parseInt(elems[2],10);

			result = allDigits(elems[0]) && (month > 0) && (month < 13) &&
					 allDigits(elems[1]) && (day > 0) && (day < 32) && (month!=2 ||day<30)&& (month!=1 ||day<32)&& (month!=3 ||day<32)&& (month!=4 ||day<31)&& (month!=5 ||day<32)&& (month!=6 ||day<31)&& (month!=7 ||day<32)&& (month!=8 ||day<32)&& (month!=9 ||day<31)&& (month!=10 ||day<32)&& (month!=11 ||day<31)&& (month!=12 ||day<32)&&	 allDigits(elems[2]) && (elems[2].length == 4);
 		}
 		
  		if (!result)
 		{
			alert('Please enter date in a valid format (DD/MM/YYYY)');
			if(!formField.disabled)
			formField.focus();		
		}
	} 
	
	return result;
}

function validDate(formField,msg)
{
	var result = true;
	if(formField.value=='')
		result=false;
  
 	if (result)
 	{
 		var elems = formField.value.split("/");
 		
 		result = (elems.length == 3); // should be three components
 		
 		if (result)
 		{
  			var day = parseInt(elems[0],10);
 			var month = parseInt(elems[1],10);
 			var year = parseInt(elems[2],10);

			result = allDigits(elems[0]) && (month > 0) && (month < 13) &&
					 allDigits(elems[1]) && (day > 0) && (day < 32) && (month!=2 ||day<30)&& (month!=1 ||day<32)&& (month!=3 ||day<32)&& (month!=4 ||day<31)&& (month!=5 ||day<32)&& (month!=6 ||day<31)&& (month!=7 ||day<32)&& (month!=8 ||day<32)&& (month!=9 ||day<31)&& (month!=10 ||day<32)&& (month!=11 ||day<31)&& (month!=12 ||day<32)&&	 allDigits(elems[2]) && (elems[2].length == 4);
 		}
 		
  		if (!result)
 		{
 			if (msg != '')
			{
				alert(msg);
				if(!formField.disabled)
				formField.focus();		
			}
			else
			{
				alert('Please enter date in a valid format (DD/MM/YYYY)');
				if(!formField.disabled)
				formField.focus();		
			}
		}
	} 
	
	return result;
}


/*
function validateForm(theForm)
{
	// Customize these calls for your form

	// Start ------->
	if (!validRequired(theForm.fullname,"Name"))
		return false;

	if (!validEmail(theForm.email,"Email Address",true))
		return false;

	if (!validDate(theForm.available,"Date Available",true))
		return false;

	if (!validNum(theForm.yearsexperience,"Years Experience",true))
		return false;
	// <--------- End
	
	return true;
}
*/
