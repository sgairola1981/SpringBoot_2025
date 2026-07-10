var numOfColumns = 0;
var rowIndex="";
document.onkeydown = function(){
if ((event.keyCode == 78) && (event.ctrlKey)){
alert ("Not allowed to open a new window like this !")
event.cancelBubble = true;
event.returnValue = false;
event.keyCode = false; 
return false;
}
}

/*
 * set_DropDownCombo(name,val) will set combo box selection to val if val is one of its valid option
 * @Author Anil Kumar
 */
function set_DropDownCombo( name,  val)
{

var x=document.getElementById(name);
var optText,optVal;
if (x == undefined)
return;
for (var i=0;i<x.length;i++)
    {
    optText= x.options[i].text;
    optVal=  x.options[i].value

    if(optText==val ||optVal==val ){
      x.selectedIndex=i;
      break;
      }
    }



}

/*Following function will disable every button present in the supplied form
 * This function shall be called immediatelly after the form.submit().
 * This way it will implement synchronization token mechanism and thus avoid duplicated submission
 *
 *@author Anil Kumar
 *@parameter 0 : Name of the form containing feilds to be disabled
 */

function disableAllbuttons(frmName)
{
var vForm=document.getElementById(frmName);
var vFeilds;
for (var i=0;i<vForm.length;i++){

   if (vForm.elements[i].name!='' && (vForm.elements[i].type.indexOf("button")!=-1)  ){
   vFeilds= document.getElementsByName(vForm.elements[i].name);
   for(var j=0; j<vFeilds.length; j++)
           vFeilds[j].disabled = true ;
   } // end of if
} // end of for
  
}


/*Following function will disable all the Text, Text Area & Select present in the supplied form
 *@author Anil Kumar
 *@parameter 0 : Name of the form containing feilds to be disabled
 * Note : Disabled feilds are never submitted to the sever hence make sure to enable before submitting. 
 *        Otherwise it will result to an error. 
 *        Also if there exist some HTML ELEMENT identified only by "id" not by "name"
 *        Then use elements[i].id instead of elements[i].name i.e add another if body within the 
 *        main for loop
 */

function disableAll1234(frmName)
{
var vForm=document.getElementById(frmName);
var vFeilds;
for (var i=0;i<vForm.length;i++){
   if (vForm.elements[i].name!='' && (vForm.elements[i].type.indexOf("text")!=-1)  ){
   vFeilds= document.getElementsByName(vForm.elements[i].name);
   for(var j=0; j<vFeilds.length; j++)
           vFeilds[j].disabled = true ;
           
   } // end of if

   if (vForm.elements[i].name!='' && (vForm.elements[i].type.indexOf("select")!=-1)  ){
   vFeilds= document.getElementsByName(vForm.elements[i].name);
   for(var j=0; j<vFeilds.length; j++)
           vFeilds[j].disabled = true ;
           
   } // end of if

} // end of for
  
}
//Start change By shailendra Kumar 22012021

function enable_All(form1)
{
 var oForm = document.forms[form1];
    // alert(oForm);
    var inputs = oForm.getElementsByTagName("input"); 
    for (var i = 0; i < inputs.length; i++) { 
        inputs[i].disabled = false;
    } 
    var selects = oForm.getElementsByTagName("select");
    for (var i = 0; i < selects.length; i++) {
        selects[i].disabled = false;
    }
    var textareas = oForm.getElementsByTagName("textarea"); 
    for (var i = 0; i < textareas.length; i++) { 
        textareas[i].disabled = false;
    }
    var buttons = oForm.getElementsByTagName("button");
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].disabled = false;
    }
}
function  disableAll(form1)
{
     var oForm = document.forms[form1];
    // alert(oForm);
    var inputs = oForm.getElementsByTagName("input"); 
    for (var i = 0; i < inputs.length; i++) { 
        inputs[i].disabled = true;
    } 
    var selects = oForm.getElementsByTagName("select");
    for (var i = 0; i < selects.length; i++) {
        selects[i].disabled = true;
    }
    var textareas = oForm.getElementsByTagName("textarea"); 
    for (var i = 0; i < textareas.length; i++) { 
        textareas[i].disabled = true;
    }
    var buttons = oForm.getElementsByTagName("button");
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].disabled = true;
    }
}
    
    function call_module(msg)
            {	
             document.getElementById('msg_data').innerHTML = msg;
             document.getElementById('myModal').style.display ="block";
             disableAll('form1');
             document.forms[0].no_btn.disabled=false;
             document.forms[0].yes_btn.disabled=false;
   }
 function yes_button()
            {		

    document.getElementById('myModal').style.display = 'none';
       enableAll('form1');
      
}
	function no_button()
            {		
  
     document.getElementById('myModal').style.display = 'none';
     enableAll('form1');
       
}
	 function open_button()
            {
    document.getElementById('myModal').style.display ="block";
    enableAll('form1');
           }
//End change By shailendra Kumar 22012021
function enableAll(form1)
{
     var oForm = document.forms[form1];
    // alert(oForm);
    var inputs = oForm.getElementsByTagName("input"); 
    for (var i = 0; i < inputs.length; i++) { 
        inputs[i].disabled = false;
    } 
    var selects = oForm.getElementsByTagName("select");
    for (var i = 0; i < selects.length; i++) {
        selects[i].disabled = false;
    }
    var textareas = oForm.getElementsByTagName("textarea"); 
    for (var i = 0; i < textareas.length; i++) { 
        textareas[i].disabled = false;
    }
    var buttons = oForm.getElementsByTagName("button");
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].disabled = false;
    }
}
function disable_All(form1)
{
     var oForm = document.forms[form1];
    // alert(oForm);
    var inputs = oForm.getElementsByTagName("input"); 
    for (var i = 0; i < inputs.length; i++) { 
        inputs[i].disabled = true;
    } 
    var selects = oForm.getElementsByTagName("select");
    for (var i = 0; i < selects.length; i++) {
        selects[i].disabled = true;
    }
    var textareas = oForm.getElementsByTagName("textarea"); 
    for (var i = 0; i < textareas.length; i++) { 
        textareas[i].disabled = true;
    }
    var buttons = oForm.getElementsByTagName("button");
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].disabled = true;
    }
}
function enableAll1234(frmName)
{
var vForm=document.getElementById(frmName);
var vFeilds;

for (var i=0;i<vForm.length;i++){
   if (vForm.elements[i].name!='' && (vForm.elements[i].type.indexOf("text")!=-1)  ){
   vFeilds= document.getElementsByName(vForm.elements[i].name);
   for(var j=0; j<vFeilds.length; j++)
           vFeilds[j].disabled = false ;
           
   } // end of if

   if (vForm.elements[i].name!='' && (vForm.elements[i].type.indexOf("select")!=-1)  ){
   vFeilds= document.getElementsByName(vForm.elements[i].name);
   for(var j=0; j<vFeilds.length; j++)
           vFeilds[j].disabled = false ;
           
   } // end of if

} // end of for
  
}


/*Following function will makke all the Text & Text Area prsent in the supplied form read only
 *@author Anil Kumar
 *@parameter 0 : Name of the form containing feilds to be marked as readOnly
 */

function readOnlyAll(frmName)
{
var vForm=document.getElementById(frmName);
var vFeilds;
for (var i=0;i<vForm.length;i++){
   if (vForm.elements[i].name!='' && (vForm.elements[i].type.indexOf("text")!=-1)  ){
   vFeilds= document.getElementsByName(vForm.elements[i].name);
   for(var j=0; j<vFeilds.length; j++)
           vFeilds[j].readOnly = true ;
           
   } // end of if



} // end of for
  
}

// Following function will issue warning message for selecting No as voucher generation flag
function warnForNoVoucherGen(VOU_GEN_FLG)
{
if (VOU_GEN_FLG.value=='N')
  if(confirm("You have selected No Voucher generation. System will not generate any voucher for this transaction. Do you want to countinue."))
    return true;
  else{
    VOU_GEN_FLG.selectedIndex=1;
    VOU_GEN_FLG.focus(); 
    return false;
    }
else
  return true;
  
} // end of function warnForNoVoucherGen()
//Start change By shailendra gaiorola 05/01/2011
 
function valid_Sis_Date(formField)
{  
var msg="Please enter a valid format Date (DD/MM/YYYY)";
  if(validDate(formField,msg)==true){
    var d = new Date();
    var day = d.getDate();
    var month = d.getMonth()+1;
     var year = d.getFullYear();
     var date = day+'/'+month+'/'+year;  
     
   if(date_check_final_SIS_ST(date,formField.value))
    {   
		alert('Date should not be greater than Current date');
		formField.select();
                formField.value="";
		return false;
    }
 }   
}
function checkDatesComparison(dt1,dt2)
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
  if(commencDt>=complDt)
  {
  return false;
  }
  else
	return true;
  }
  //End change By shailendra gaiorola 05/01/2011
function fn_upper(field1)
{
  oldString = field1.value;
  var newString = oldString.replace(/[A-Z]/g,"$&").toUpperCase();
  field1.value = newString;
}

function checkValue(field,value)
{
	var numValue=parseFloat(value);
	if(field.value>numValue)
	{
		alert('Value can not be greater then '+numValue+' for this field');
		field.select();
		return false;
	}
	return true;
}
function checkLength(field,length)
{
	var fldLength=field.value.length;
	if(fldLength>length)
	{
		alert('Field Length can not be greater then '+length+'characters for this field');
		field.select();
		return false;
	}
	return true;
}
function isValidNegative(field,mode)
{
	if(mode=='F')
	{
		if(!validFloat(field))
			return false;
	}
	else if(mode=='I')
	{
		if(!validNum(field))
			return false;
	}
	var value=parseFloat(field.value);
	if (value<0)
	{
		alert('Value can not be less than 0 for this field');
		field.focus();	
		field.select();
		return false;
	}
}
function days_between(formfield1,formfield2)
{
//alert('1');
	var val2=formfield1.value;
	var val3=formfield2.value;
 //  alert(val2);
  //alert(val3);

	var l_val2=new Date();
	var l_val3=new Date();
	var elems=val2.split("/");
	l_val2.setMonth(elems[1]-1);
    //alert(l_val2);
	l_val2.setDate(elems[0]);
    //alert(l_val2);

	l_val2.setYear(elems[2]);
      //alert(l_val2);
	elems=val3.split("/");
	l_val3.setMonth(elems[1]-1);
      //alert(l_val3);
	l_val3.setDate(elems[0]);
    //alert(l_val3);
	l_val3.setYear(elems[2]);
      //alert(l_val3);
	var l_res=(l_val3-l_val2)/1000/24/3600;
	return l_res;
}
function shortCutKeysAdd()
{
	//] Key is for Help
	if(event.keyCode==221)
	{
		/*var winpop=window.open("helplovadd.html	",'WinHelp','scrollbars=no,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
		return false;*/
		parent.fn_help();
	}
	//F10 Key is for Add new record
	else if(event.keyCode==121)
	{
		if(window.Function2!=undefined)
		{
			if(document.forms[0].add_btn.disabled==false)
				Function2('N');
		}
	}
	//F9 Key is for Last
	else if(event.keyCode==120)
	{
		if(window.Function2!=undefined)
		{
			if(document.forms[0].save_btn.disabled==false)
				Function2('I');
			return false;
		}
	}
	//F12 Key is for Reset
	else if(event.keyCode==123)
	{
		if(window.Function2!=undefined)
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
		if(window.Function2!=undefined)
		{
			if(document.forms[0].search_btn.disabled==false)
			{
				Function2('S');
				return false;
			}
		}
	}
	//] Key is for Help
	else if(event.keyCode==221)
	{
		/*var winpop=window.open("helplovsearch.html	",'WinHelp','scrollbars=no,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
		return false;*/
		parent.fn_help();
	}
	//F7 Key is for Add
	else if(event.keyCode==118)
	{
		if(window.Fn_Add!=undefined)
		{
			if(document.forms[0].add_btn.disabled==false)
				Fn_Add();
		}
	}
	//F8 Key is for First
	else if(event.keyCode==119)
	{
		if(window.fn_NextPrev!=undefined)
		{
			if(document.forms[0].first_btn.disabled==false)
				fn_NextPrev('F');
		}
	}
	//F9 Key is for Prev
	else if(event.keyCode==120)
	{
		if(window.fn_NextPrev!=undefined)
		{
			if(document.forms[0].prev_btn.disabled==false)
				fn_NextPrev('P');
		}
	}
	//F10 Key is for Next
	else if(event.keyCode==121)
	{
		if(window.fn_NextPrev!=undefined)
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
function checkDates(from_dt,to_dt)
{
     if(from_dt==''|| to_dt=='')
    {
		return true;
            }
            
      var a1=from_dt;
     var dateStrArray = a1.split("/");  
     var b1=to_dt;
   var dateStrArray1 = b1.split("/");  
   // alert(a1+"---"+ g2);
      var g1 = dateStrArray[2]+add_zeroes(dateStrArray[1])+add_zeroes(dateStrArray[0]); 
   var g2 = dateStrArray1[2]+add_zeroes(dateStrArray1[1])+add_zeroes(dateStrArray1[0]); 
   //  alert(g1+"---"+ g2);
     if (parseInt(g1) > parseInt(g2))
        return false;
    else
       return true;
  
	/*if(dt1==''||dt2=='')
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
		return true;*/
}
//Start changes by shailendra gairola 31-03-2012
function checkDates12(dt1,dt2)
{
      var elems = dt1.split("/");
    var elems1 = dt2.split("/");
      var dd=removeZero(elems[0]);
      var mm=removeZero(elems[1]);
      var dd1=removeZero(elems1[0]);
      var mm1=removeZero(elems1[1]);
     var dt1  = parseInt(dd);
    var mon1 =parseInt(mm);
    var yr1  = parseInt(elems[2]);
    var dt2  = parseInt(dd1);
    var mon2 =parseInt(mm1);
    var yr2  = parseInt(elems1[2]);
    var date1 = new Date(yr1, mon1, dt1);
    var date2 = new Date(yr2, mon2, dt2);
      if(date2 < date1)
    {
          return false;
    } 
    else
    {
    return true;
    }
}
function removeZero(vNumber)
{
alert(vNumber);
if(vNumber=="")
{
}
else
{
if(vNumber.length==2)
{
    if(vNumber.substring(0,1)==0)
   {
   var v=vNumber.substring(1,2);
    return v;
    }
    else{
      return vNumber;
    }
}
else
{
 return vNumber;
}
}
} 
//End changes by shailendra gairola 31-03-2012
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
                        formField.value="";
			result = false;
		}
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
		if(!formField.disabled&&!formField.readOnly)
		{
			if (formField)
			{
				if(formField.focus != undefined) 
					formField.focus();
				if(formField.select != undefined) 
					formField.select();
                                    formField.value="";
			}
		}
		if(formField.disabled)
		{

			formField.disabled = false;

			if(formField.focus != undefined) 
				formField.focus();
			formField.disabled = true;
            if (formField.select != undefined) 
			formField.select();
                            formField.value="";



		}
		if(formField.readOnly)
		{

			formField.readOnly = false;
			if(formField.focus != undefined) 
				formField.focus();
			formField.readOnly = true;
			if(formField.select != undefined) 
				formField.select();
                            formField.value="";
			
		}

		result = false;
	}
	return result;
}


function validRequired1(formField,mesg)
{
	var result = true;
	mesg+=" is Mandatory for further processing.";
	if (formField.value == "")
	{
		alert(mesg);
		if(!formField.disabled&&!formField.readOnly)
		{
			if (formField)
				formField.focus();
		}
		if(formField.disabled)
		{
			formField.disabled = false;

			if(formField.focus != undefined) 
				formField.focus();
			formField.disabled = true;

			if(formField.select != undefined) 
				formField.select();
                            formField.value="";

		}
		if(formField.readOnly)
		{
			formField.readOnly = false;
			if(formField.focus != undefined) 
				formField.focus();
			formField.readOnly = true;

			if(formField.select != undefined) 
				formField.select();
                            formField.value="";
			
		}
		result = false;
	}
	return result;
}

/*******Help Button Function***********/
function screenHelp(screenName,title)
{

	alert(" title is ==" +title);
	window.open(screenName + '.jsp?&title='+ title,'winSearch','scrollbars=no,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
}
/*******Help Button Function***********/
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
                                formField.value="";
			}
			result = false;
		}
	} 
	return result;
}
function validDateFormat(formField)
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
			var valYr=''+parseInt(year);
			if(valYr.length!=4)
			{
				result=false;
			}
 		}
	} 
	return result;
}
function validDateToday(formField)
{
	if(!validDate(formField,''))
	{
		formField.select();
		return false;
	}
	if(!checkDates(formField.value,document.forms[0].today.value))
	{
		alert('This date can not be a future date!!');
		formField.select();
                formField.value="";
		return false;
	}
	if(!checkDates('01/01/1900',formField.value))
	{
		alert('This date can not be earlier than 1900!!');
		formField.select();
                formField.value="";
		return false;
	}
	return true;
}
function validDate(formField)
{
	var result = true;
	if(formField.value=='')
		return false;
	if(validDateFormat(formField))
		return true;
 	if (result)
 	{
 		var value=formField.value;
 		result = (formField.value.length == 8); // should be three components
 		if (result)
 		{
  			var day = value.substring(0,2);
 			var month = value.substring(2,4);
 			var year = value.substring(4,8);
			result =  allDigits(day) && allDigits(month) && (month > 0) && (month < 13) &&
					  (day > 0) && (day < 32) && (month!=2 ||day<30)&& (month!=1 ||day<32)&& (month!=3 ||day<32)&& (month!=4 ||day<31)&& (month!=5 ||day<32)&& (month!=6 ||day<31)&& (month!=7 ||day<32)&& (month!=8 ||day<32)&& (month!=9 ||day<31)&& (month!=10 ||day<32)&& (month!=11 ||day<31)&& (month!=12 ||day<32)&&	 allDigits(year) && (year.length == 4);
			var valYr=''+parseInt(year);
			if(valYr.length!=4)
			{
				result=false;
			}
			formField.value=day+'/'+month+'/'+year;
 		}
  		if (!result)
 		{
			alert('Please enter date in a valid format (DD/MM/YYYY)');
			if(!formField.disabled)
			{
				if (formField)
					formField.focus();
			}
		}
	} 
	return result;
}

function validDate(formField,msg)
{
	var result = true;
	if(formField.value=='')
		return false;
	if(validDateFormat(formField))
		return true;
 	if (result)
 	{
 		var value=formField.value;
 		result = (formField.value.length == 8); // should be three components
 		if (result)
 		{
  			var day = value.substring(0,2);
 			var month = value.substring(2,4);
 			var year = value.substring(4,8);
			result =  allDigits(day) && allDigits(month) && (month > 0) && (month < 13) &&
					  (day > 0) && (day < 32) && (month!=2 ||day<30)&& (month!=1 ||day<32)&& (month!=3 ||day<32)&& (month!=4 ||day<31)&& (month!=5 ||day<32)&& (month!=6 ||day<31)&& (month!=7 ||day<32)&& (month!=8 ||day<32)&& (month!=9 ||day<31)&& (month!=10 ||day<32)&& (month!=11 ||day<31)&& (month!=12 ||day<32)&&	 allDigits(year) && (year.length == 4);
			var valYr=''+parseInt(year);
			if(valYr.length!=4)
			{
				result=false;
			}
			formField.value=day+'/'+month+'/'+year;
 		}
  		if (!result)
 		{
 			if (msg != '')
 			{
				alert(msg);
				if(!formField.disabled && formField.type != 'hidden')
					if (formField)
						formField.focus();		
			}
			else
			{
				alert('Please enter date in a valid format (DD/MM/YYYY)');
				if(!formField.disabled && formField.type != 'hidden')
					if (formField)
						formField.focus();		
			}
		}
	} 
	return result;
}

function fn_RowDeleteBLK(block,row)
{ 

	/*
		Function added for flag setting, and, change of image on Multirow Blocks.
		Ajnam Bujanany
	*/
	var tbl = document.getElementById('addNewRowTable_Block' + block);
	var lastRow_old = tbl.rows.length;
	var rowsArray = tbl.rows;
	lastRow = lastRow_old-3;
	var arrayOfFlag = document.all("DELFLAGBLOCK" + block);
	var delete_img = document.all("delete_img_B"+block)

 	if(lastRow == 1)
	{
		
		var source = delete_img.src;
		if(source.indexOf('images/BeforeDelete.gif') != -1)
		{	
			delete_img.src='images/Delete.gif';
			if (arrayOfFlag.value == 'U')
			{
				arrayOfFlag.value = "DU";
			}
			else if (arrayOfFlag.value == 'N')
			{
				arrayOfFlag.value = "DN";
			}			
			
		}
		else if(source.indexOf('images/Delete.gif') != -1)
		{
			delete_img.src='images/BeforeDelete.gif';
			
			if (arrayOfFlag.value == 'DU')
			{
				arrayOfFlag.value = "U";
			}
			else if (arrayOfFlag.value == 'DN')
			{
				arrayOfFlag.value = "N";
			}
		}
		else
		{
			return false;
		}			
	}
	else
	{
		source = delete_img[row-1].src;
		if(source.indexOf('images/BeforeDelete.gif') != -1)
		{	
			delete_img[row-1].src='images/Delete.gif';
			
			if (arrayOfFlag[row-1].value == 'U')
			{
				arrayOfFlag[row-1].value = "DU";
			}
			else if (arrayOfFlag[row-1].value == 'N')
			{
				arrayOfFlag[row-1].value = "DN";
			}
		}
		else if(source.indexOf('images/Delete.gif') != -1)
		{
			delete_img[row-1].src='images/BeforeDelete.gif';
			
			if (arrayOfFlag[row-1].value == 'DU')
			{
				arrayOfFlag[row-1].value = "U";
			}
			else if (arrayOfFlag[row-1].value == 'DN')
			{
				arrayOfFlag[row-1].value = "N";
			}
		}
		else
		{
			return false;
		}			
	}	
}

function validInteger(intField, fieldLabel)
{
	/*
		Function added for integer validation
		Ajnam Bujanany
	*/
	var fieldValue=intField.value;
	if(isNaN(fieldValue)==true)
	{
		alert("Please enter a valid Number for " + fieldLabel);
		intField.value="";
		intField.focus();		
	}
	else
	{
		var decimalIndex=fieldValue.lastIndexOf(".");
		if (decimalIndex!=-1)
		{
			alert("Please enter a valid Integer for " + fieldLabel);
			intField.value="";
			intField.focus();
		}
	}
}


function validNumber(floatField, fieldLabel)
{
	var fieldValue=floatField.value;
	if(isNaN(fieldValue)==true)
	{
		alert("Please enter a valid Number for " + fieldLabel);
		floatField.value="";
		floatField.focus();		
	}
}
function validFloat_2(floatField,fieldLabel)
{
	/* Function added for float validation*/
	var fieldValue=floatField.value;
	if (isNaN(fieldValue))
	{
		alert("Please enter a valid Number for " +fieldLabel);
		floatField.value="";
		floatField.focus();	
		return false;	
	}
	return true;
}	
function validFloat_4(floatField, fieldLabel,totalDigits,afterDecimal)
{
	/*
		Function added for float validation
		Ajnam Bujanany
	*/
	var fieldValue=floatField.value;
	if(isNaN(fieldValue)==true)
	{
		alert("Please enter a valid Number for " + fieldLabel);
		floatField.value="";
		floatField.focus();	
		return false;	
	}
	var decimalIndex = fieldValue.lastIndexOf(".");
	if (decimalIndex!=-1)
	{
		var fieldString = "" + floatField.value;
		var fieldLength = fieldString.length;
		var afterDecimalLength=parseInt(fieldLength)-parseInt(decimalIndex+1);
		if(afterDecimalLength>afterDecimal)
		{
			alert("Please Enter only " + afterDecimal + " digits after Decimal");
			floatField.value="";
			floatField.focus();				
			return false;
		}
		else
		{
			var allowedLengthBeforeDecimal = parseInt(totalDigits) - parseInt(afterDecimal);
			var beforeDecimalLength = parseInt(fieldLength) - parseInt((afterDecimalLength+1));
			if ((beforeDecimalLength)>allowedLengthBeforeDecimal)
			{
				alert("Please Enter only " + parseInt(allowedLengthBeforeDecimal) + " digits before Decimal");
				floatField.value="";
				floatField.focus();
				return false;			
			}
		}
	}
	else
	{
		var fieldString = "" + floatField.value;
		var fieldLength = fieldString.length;
		var allowedLength = parseInt(totalDigits) - parseInt(afterDecimal);
		if (fieldLength>allowedLength)
		{
			alert("Please Enter only " + allowedLength + " digits if Decimal is not there");
			floatField.value="";
			floatField.focus();	
			return false;		
		}
	}
	return true;
}

function fn_setFlag(block,row)
{
	/*
		Function added to set Flag in multirow JSP
		Siddhartha Asthana
	*/
	var tbl = document.getElementById('addNewRowTable_Block' + block);
 	var lastRow = tbl.rows.length;
	var rowsArray = tbl.rows;
	lastRow = lastRow-3;
 	var delete_img = document.all("delete_img_B"+block)
	var arrayOfFlag = document.all("DELFLAGBLOCK"+block);
  if(lastRow == 1)
 	{  
		if (arrayOfFlag.value.indexOf('X') != -1)
		{ 
			arrayOfFlag.value = "N";
			delete_img.src='images/BeforeDelete.gif';		
		}
	}
	else
	{
		if (arrayOfFlag[row-1].value.indexOf('X') != -1)
		{
			arrayOfFlag[row-1].value = "N";
			delete_img[row-1].src='images/BeforeDelete.gif';
		}
	}	
}

function fn_markForUpdate(block,row)
{
	/*
  Function to mark the rows in multi row screen, which need updation.
  XU -> Before update i.e. this will be the value for existing records.
  U  -> Marked for Updation i.e. for records with U flag Update query will be trigered
  @author Anil Kumar
	*/
	var tbl = document.getElementById('addNewRowTable_Block' + block);
	var lastRow = tbl.rows.length;
	var rowsArray = tbl.rows;
	lastRow = lastRow-3;
	var delete_img = document.all("delete_img_B"+block)
	var arrayOfFlag = document.all("DELFLAGBLOCK"+block);
 	if(lastRow == 1)
 	{  
		if (arrayOfFlag.value.indexOf('XU') != -1)
		{ 
			arrayOfFlag.value = "U";
			delete_img.src='images/BeforeDelete.gif';		
		}
	}
	else
	{
		if (arrayOfFlag[row-1].value.indexOf('XU') != -1)
		{
			arrayOfFlag[row-1].value = "U";
			delete_img[row-1].src='images/BeforeDelete.gif';
		}
	}	
}


function fn_getLength(tableName)
{
	/*
		Function added to get Length of table
		Ajnam Bujanany
	*/
	var tbl = document.getElementById(tableName);
	var lastRow = tbl.rows.length;
	lastRow = lastRow-3;
	return lastRow;
}

function fn_getStringBeforeCarat(sourceString)
{
	/*
		Function added to get a string before carat in List JSP, primkey
		Siddhartha Asthana
	*/
	var returnString = sourceString;
	var indexOfCarat = sourceString.indexOf('^');
	if (indexOfCarat != -1)
	{
		returnString = sourceString.substring(0,parseInt(indexOfCarat));
	}
	var indexOfColon = returnString.indexOf(':');
	if (indexOfColon != -1)
	{
		returnString = returnString.substring(0,parseInt(indexOfColon));
	}
	return returnString;
}

function fn_getStringAfterCarat(sourceString)
{
	/*
		Function added to get a string after carat in List JSP, primkey
		Siddhartha Asthana
	*/
    var returnString = sourceString;
    var indexOfCarat = sourceString.indexOf('^');
	returnString = sourceString.substring(parseInt(indexOfCarat)+1)
	if (returnString.indexOf('^') == -1)
	{
		var indexOfColon = returnString.indexOf(':');
		if (indexOfColon != -1)
		{
			returnString = returnString.substring(0,parseInt(indexOfColon));
		}
	}
	return returnString;
}

function fn_lov_unit_cd(fieldName,fieldDesc,name,title,fnValue,mValue)
{
	/*
		Function added to open the popup for Unit code on the screens
		Siddhartha Asthana
	*/
	numOfColumns = 0;
	parentfield=eval("document.forms[0]." + fieldName);
	parentfield1=eval("document.forms[0]." + fieldDesc);
	var winPopup = window.open("wfmslov_Unit_Cd.jsp?name="+ name +"&title="+ title +"&fn=" + fnValue + "&mValue="+mValue,'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
}

function fn_lov_commmon(fieldName,fieldDesc,name,title,fnValue,mValue)
{
	/*
		Function added to open the popup for Unit code on the Login screen
		Siddhartha Asthana
	*/
	numOfColumns = 0;
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}
	var winPopup = window.open("wfmslov.jsp?name="+ name +"&title="+ title +"&fn=" + fnValue + "&mValue="+mValue,'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');

}
function fn_lov_commmon(fieldName,fieldDesc,name,title,fnValue,mValue,XML)
{
	/*
		Function added to ensure minmal chages are made in exisitng code to convert to new LOV
		Amol Behrani
	*/
	numOfColumns = 0;
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}
	
	var winPopup = window.open("wfmslov.jsp?name="+ name +"&title="+ title +"&fn=" + fnValue + "&mValue="+URLEncode(mValue)+"&XML="+XML,'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');

}
function fn_lov_common(fieldName,fieldDesc,name,title,fnValue,mValue)
{
	/*
		Function added to open the popup for Unit code on the Login screen
		Siddhartha Asthana
	*/
	numOfColumns = 0;
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	var winPopup = window.open("wfmslov.jsp?name="+ name +"&title="+ title +"&fn=" + fnValue + "&mValue="+mValue,'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');

}
function fn_lov_common(fieldName,fieldDesc,name,title,fnValue,mValue,XML)
{
	/*
		Function added to ensure minmal chages are made in exisitng code to convert to new LOV
		Amol Behrani
	*/
	numOfColumns = 0;
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {	
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	var winPopup = window.open("wfmslov.jsp?name="+ name +"&title="+ title +"&fn=" + fnValue + "&mValue="+URLEncode(mValue)+"&XML="+XML,'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');

}
function fn_lov_generic(fieldName,fieldDesc,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue)
{
	/*
		Function to be used for calling Generic LOV 
		* XML = "X" - XML
		        "D" - DataBase
		* mFieldName = The name of the field (1) in SEARCH_FIELDS while defining the LOV
		* sFieldName = The name of the field (2) in SEARCH_FIELDS while defining the LOV
		* In case more fields are to be populated in the Parent Screen, appropriate number of more parentfield<n> can be set IN A CUSTOM FUNCTION IN YOUR OWN JSP, NOT THIS FUNCTION , along with the number of extra fields in "numOfColumns"

		Example - 
			numOfColumns = 1;
			parentfield2=eval("document.forms[0]." + attr0);

	*/

	numOfColumns = 0;
	rowIndex="";
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}


	var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');


}
function fn_lov_generic_multi(fieldName,fieldDesc,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue,row)
{
	/*
		Function to be used for calling Generic LOV 
		* XML = "X" - XML
		        "D" - DataBase
		* mFieldName = The name of the field (1) in SEARCH_FIELDS while defining the LOV
		* sFieldName = The name of the field (2) in SEARCH_FIELDS while defining the LOV
		* In case more fields are to be populated in the Parent Screen, appropriate number of more parentfield<n> can be set IN A CUSTOM FUNCTION IN YOUR OWN JSP, NOT THIS FUNCTION , along with the number of extra fields in "numOfColumns"

		Example - 
			numOfColumns = 1;
			parentfield2=eval("document.forms[0]." + attr0);

	*/

	numOfColumns = 0;
	rowIndex = row;
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}


	var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');


}
function fn_lov_generic_attr0(fieldName,fieldDesc,attr0,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue)
{
	/*
		Function to be used for calling Generic LOV 
		* XML = "X" - XML
		        "D" - DataBase
		* mFieldName = The name of the field (1) in SEARCH_FIELDS while defining the LOV
		* sFieldName = The name of the field (2) in SEARCH_FIELDS while defining the LOV
		* In case more fields are to be populated in the Parent Screen, appropriate number of more parentfield<n> can be set IN A CUSTOM FUNCTION IN YOUR OWN JSP, NOT THIS FUNCTION , along with the number of extra fields in "numOfColumns"

		Example - 
			numOfColumns = 1;
			parentfield2=eval("document.forms[0]." + attr0);

	*/

	numOfColumns = 1;
	rowIndex="";
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	if(attr0!='') {
		parentfield2=eval("document.forms[0]." + attr0);
	} else {
		parentfield2='';
	}


	var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');


}
function fn_lov_generic_attr1(fieldName,fieldDesc,attr0,attr1,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue)
{
	/*
		Function to be used for calling Generic LOV 
		* XML = "X" - XML
		        "D" - DataBase
		* mFieldName = The name of the field (1) in SEARCH_FIELDS while defining the LOV
		* sFieldName = The name of the field (2) in SEARCH_FIELDS while defining the LOV
		* In case more fields are to be populated in the Parent Screen, appropriate number of more parentfield<n> can be set IN A CUSTOM FUNCTION IN YOUR OWN JSP, NOT THIS FUNCTION , along with the number of extra fields in "numOfColumns"

		Example - 
			numOfColumns = 1;
			parentfield2=eval("document.forms[0]." + attr0);

	*/

	numOfColumns = 2;
	rowIndex="";
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	if(attr0!='') {
		parentfield2=eval("document.forms[0]." + attr0);
	} else {
		parentfield2='';
	}

	if(attr1!='') {
		parentfield3=eval("document.forms[0]." + attr1);
	} else {
		parentfield3='';
	}

	var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');


}
function fn_lov_generic_attr1_multi_mand(fieldName,fieldDesc,attr0,attr1,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue,row,mandatory1,mesg1,mandatory2,mesg2)
{
	var go = true;
	numOfColumns = 2;
	rowIndex = row;
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	if(attr0!='') {
		parentfield2=eval("document.forms[0]." + attr0);
	} else {
		parentfield2='';
	}

	if(attr1!='') {
		parentfield3=eval("document.forms[0]." + attr1);
	} else {
		parentfield3='';
	}
	
	if(mandatory1 != '') {
		mandatoryField1 = eval("document.forms[0]." + mandatory1+".value");
		if(mandatoryField1 == '') {
			if(mesg1 != '')
				alert(mesg1);
			else 
				alert("A mandatory field has to be entered before value can be selected");
			go=false;
		} 
	}
	if(go == true){
		if(mandatory2 != '') {
			mandatoryField2 = eval("document.forms[0]." + mandatory2+".value");
			if(mandatoryField2 == '') {
				if(mesg2 != '') 
					alert(mesg2);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true) { 
		var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
	}


}
function fn_lov_generic_attr1_mand(fieldName,fieldDesc,attr0,attr1,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue,mandatory1,mesg1,mandatory2,mesg2)
{
	var go = true;
	numOfColumns = 2;
	rowIndex = "";
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	if(attr0!='') {
		parentfield2=eval("document.forms[0]." + attr0);
	} else {
		parentfield2='';
	}

	if(attr1!='') {
		parentfield3=eval("document.forms[0]." + attr1);
	} else {
		parentfield3='';
	}
	
	if(mandatory1 != '') {
		mandatoryField1 = eval("document.forms[0]." + mandatory1+".value");
		if(mandatoryField1 == '') {
			if(mesg1 != '')
				alert(mesg1);
			else 
				alert("A mandatory field has to be entered before value can be selected");
			go=false;
		} 
	}
	if(go == true){
		if(mandatory2 != '') {
			mandatoryField2 = eval("document.forms[0]." + mandatory2+".value");
			if(mandatoryField2 == '') {
				if(mesg2 != '') 
					alert(mesg2);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true) { 
		var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
	}


}
function fn_lov_generic_attr2_multi_mand(fieldName,fieldDesc,attr0,attr1,attr2,attr3,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue,row,mandatory1,mesg1,mandatory2,mesg2)
{
	var go = true;
	numOfColumns = 4;
	rowIndex = row;
	
	parentfield=eval("document.forms[0]." + fieldName);
	
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	if(attr0!='') {
		parentfield2=eval("document.forms[0]." + attr0);
	} else {
		parentfield2='';
	}

	if(attr1!='') {
		parentfield3=eval("document.forms[0]." + attr1);
	} else {
		parentfield3='';
	}
	if(attr2!='') {
		parentfield4=eval("document.forms[0]." + attr2);
	} else {
		parentfield4='';
	}
	if(attr3!='') {
		parentfield5=eval("document.forms[0]." + attr3);
	} else {
		parentfield5='';
	}
	
	if(mandatory1 != '') {
		mandatoryField1 = eval("document.forms[0]." + mandatory1+".value");
		if(mandatoryField1 == '') {
			if(mesg1 != '')
				alert(mesg1);
			else 
				alert("A mandatory field has to be entered before value can be selected");
			go=false;
		} 
	}
	if(go == true){
		if(mandatory2 != '') {
			mandatoryField2 = eval("document.forms[0]." + mandatory2+".value");
			if(mandatoryField2 == '') {
				if(mesg2 != '') 
					alert(mesg2);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true) { 
		var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
	}


}
function fn_lov_generic_mand(fieldName,fieldDesc,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue,mandatory1,mesg1,mandatory2,mesg2)
{
	var go = true;
	/*
		Function to be used for calling Generic LOV 
		* XML = "X" - XML
		        "D" - DataBase
		* mFieldName = The name of the field (1) in SEARCH_FIELDS while defining the LOV
		* sFieldName = The name of the field (2) in SEARCH_FIELDS while defining the LOV
		* In case more fields are to be populated in the Parent Screen, appropriate number of more parentfield<n> can be set IN A CUSTOM FUNCTION IN YOUR OWN JSP, NOT THIS FUNCTION , along with the number of extra fields in "numOfColumns"

		Example - 
			numOfColumns = 1;
			parentfield2=eval("document.forms[0]." + attr0);

	*/

	numOfColumns = 0;
	rowIndex="";
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	if(mandatory1 != '') {
		mandatoryField1 = eval("document.forms[0]." + mandatory1+".value");
		if(mandatoryField1 == '') {
			if(mesg1 != '')
				alert(mesg1);
			else 
				alert("A mandatory field has to be entered before value can be selected");
			go=false;
		} 
	}
	if(go == true){
		if(mandatory2 != '') {
			mandatoryField2 = eval("document.forms[0]." + mandatory2+".value");
			if(mandatoryField2 == '') {
				if(mesg2 != '') 
					alert(mesg2);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true) { 
		var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
	}

}
function fn_lov_generic_mand3(fieldName,fieldDesc,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue,ssFieldName,ssValue,mandatory1,mesg1,mandatory2,mesg2,mandatory3,mesg3)
{
	var go = true;
	var go1 = true;
	/* takes in 3 mandatory parameters and 3 values of search criteria*/
	numOfColumns = 0;
	rowIndex="";
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	if(mandatory1 != '') {
		mandatoryField1 = eval("document.forms[0]." + mandatory1+".value");
		if(mandatoryField1 == '') {
			if(mesg1 != '')
				alert(mesg1);
			else 
				alert("A mandatory field has to be entered before value can be selected");
			go=false;
		} 
	}
	if(go == true){
		if(mandatory2 != '') {
			mandatoryField2 = eval("document.forms[0]." + mandatory2+".value");
			if(mandatoryField2 == '') {
				if(mesg2 != '') 
					alert(mesg2);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true){
		if(mandatory3 != '') {
			mandatoryField3 = eval("document.forms[0]." + mandatory3+".value");
			if(mandatoryField3 == '') {
				if(mesg3 != '') 
					alert(mesg3);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}

	if(go == true) { 
		var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue)+"&"+ssFieldName+"="+URLEncode(ssValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
	}

}
function fn_lov_generic_mand4(fieldName,fieldDesc,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue,ssFieldName,ssValue,sssFieldName,sssValue,mandatory1,mesg1,mandatory2,mesg2,mandatory3,mesg3,mandatory4,mesg4)
{
	var go = true;
	var go1 = true;
	/* takes in 3 mandatory parameters and 3 values of search criteria*/
	numOfColumns = 0;
	rowIndex="";
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	if(mandatory1 != '') {
		mandatoryField1 = eval("document.forms[0]." + mandatory1+".value");
		if(mandatoryField1 == '') {
			if(mesg1 != '')
				alert(mesg1);
			else 
				alert("A mandatory field has to be entered before value can be selected");
			go=false;
		} 
	}
	if(go == true){
		if(mandatory2 != '') {
			mandatoryField2 = eval("document.forms[0]." + mandatory2+".value");
			if(mandatoryField2 == '') {
				if(mesg2 != '') 
					alert(mesg2);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true){
		if(mandatory3 != '') {
			mandatoryField3 = eval("document.forms[0]." + mandatory3+".value");
			if(mandatoryField3 == '') {
				if(mesg3 != '') 
					alert(mesg3);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true){
		if(mandatory4 != '') {
			mandatoryField4 = eval("document.forms[0]." + mandatory4+".value");
			if(mandatoryField4 == '') {
				if(mesg4 != '') 
					alert(mesg4);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}

	if(go == true) { 
		var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue)+"&"+ssFieldName+"="+URLEncode(ssValue)+"&"+sssFieldName+"="+URLEncode(sssValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
	}

}
function fn_lov_generic_mand5(fieldName,fieldDesc,LOV_ID,XML,mFieldName,mValue,sFieldName,sValue,ssFieldName,ssValue,mandatory1,mesg1,mandatory2,mesg2,mandatory3,mesg3,mandatory4,mesg4,mandatory5,mesg5)
{
	var go = true;
	var go1 = true;
	/* takes in 3 mandatory parameters and 3 values of search criteria*/
	numOfColumns = 0;
	rowIndex="";
	parentfield=eval("document.forms[0]." + fieldName);
	if(fieldDesc!='') {
		parentfield1=eval("document.forms[0]." + fieldDesc);
	} else {
		parentfield1='';
	}

	if(mandatory1 != '') {
		mandatoryField1 = eval("document.forms[0]." + mandatory1+".value");
		if(mandatoryField1 == '') {
			if(mesg1 != '')
				alert(mesg1);
			else 
				alert("A mandatory field has to be entered before value can be selected");
			go=false;
		} 
	}
	if(go == true){
		if(mandatory2 != '') {
			mandatoryField2 = eval("document.forms[0]." + mandatory2+".value");
			if(mandatoryField2 == '') {
				if(mesg2 != '') 
					alert(mesg2);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true){
		if(mandatory3 != '') {
			mandatoryField3 = eval("document.forms[0]." + mandatory3+".value");
			if(mandatoryField3 == '') {
				if(mesg3 != '') 
					alert(mesg3);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true){
		if(mandatory4 != '') {
			mandatoryField4 = eval("document.forms[0]." + mandatory4+".value");
			if(mandatoryField4 == '') {
				if(mesg4 != '') 
					alert(mesg4);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true){
		if(mandatory5 != '') {
			mandatoryField5 = eval("document.forms[0]." + mandatory5+".value");
			if(mandatoryField5 == '') {
				if(mesg4 != '') 
					alert(mesg5);	
				else 
					alert("A mandatory field has to be entered before value can be selected");
				go=false;
			}
		}
	}
	if(go == true) { 
		var winPopup = window.open("WFMSControllerServlet?actionFlag=GenericLovOpen&LOV_ID="+ LOV_ID +"&ims_flag=S&XML="+XML+"&"+mFieldName+"="+URLEncode(mValue)+"&"+sFieldName+"="+URLEncode(sValue)+"&"+ssFieldName+"="+URLEncode(ssValue),'winSearch','scrollbars=yes,dependent=yes,top=50,left=100,resizeable=yes,width=500,height=400');
	}

}
function fn_validPositiveInteger(intField, fieldLabel)
{
	/*
		Function added to check for positive integers
		Saurabh Chandla
	*/

	fieldValue=intField.value;
	if(isNaN(fieldValue)==true)
	{
		alert("Please enter a valid Number for " + fieldLabel);
		intField.value="";
		intField.focus();
		return false;		
	}	
	else
	{
		var decimalIndex=fieldValue.lastIndexOf(".");
		if (decimalIndex!=-1)
		{
			alert("Please enter a valid Integer for " + fieldLabel);
			intField.value="";
			intField.focus();
			return false;
		}
	}
	if(fieldValue<0) 
	{
		alert("Please enter a positive value for " + fieldLabel);
		intField.value="";
		intField.focus();
		return false;
	}
	return true
}

function fn_validateDate(From_DT,Till_DT)
{
	/*
		Function added to check for FromDate < TillDate
		Saurabh Chandla
	*/
	var chkdt = checkDates(From_DT.value, Till_DT.value)
	if (chkdt == false)
	{
		alert("From date should be less than Till date");
		From_DT.focus();
		return false;
	}
	else
	{
		return true;
	}
}


function restrictSpecialChar(inVal){
	var check=0; 
	for (var i=0;i<inVal.length;i++){
		var oneChar = inVal.charAt(i);
		if (!((oneChar >="!" && oneChar <="/" ) || (oneChar >=";" && oneChar <="@" )||(oneChar >="[" && oneChar <="`")||(oneChar >="{" && oneChar <="~")||oneChar ==":")){
		}else{
			check = check +1;
		}
	}
	if (check>0)
		return false;
	else
		return true;
}
function NoComma(inVal){
	var check=0; 
	for (var i=0;i<inVal.length;i++){
		var oneChar = inVal.charAt(i);
		if (((oneChar >="," && oneChar <="'" ))){
		}else{
			check = check +1;
		}
	}
	if (check>0)
		return false;
	else
		return true;
}
function positive_int_gr_zero(intField, fieldLabel)
{
	/*
		Function added to check for positive integers >0
		
	*/

	fieldValue=intField.value;
	if(isNaN(fieldValue)==true)
	{
		alert("Please enter a valid Number for " + fieldLabel);
		intField.value="";
		intField.focus();
		return false;		
	}	
	else
	{
		var decimalIndex=fieldValue.lastIndexOf(".");
		if (decimalIndex!=-1)
		{
			alert("Please enter a valid Integer for " + fieldLabel);
			intField.value="";
			intField.focus();
			return false;
		}
	}
	if(fieldValue<0) 
	{
		alert("Please enter a positive value for " + fieldLabel);
		intField.value="";
		intField.focus();
		return false;
	}
	if(fieldValue==0) 
	{
		alert("Please enter a positive value greater than 0 " + fieldLabel);
	//	intField.value="";
	//	intField.focus();
		return false;
	}
	return true
}
function Trim(TRIM_VALUE){


	if(TRIM_VALUE.length < 1){
		return"";
	}
	TRIM_VALUE = RTrim(TRIM_VALUE);
	TRIM_VALUE = LTrim(TRIM_VALUE);


	if(TRIM_VALUE==""){
		return "";
	}else{
		return TRIM_VALUE;
	}
}


function RTrim(VALUE){
	var w_space = String.fromCharCode(32);
	var v_length = VALUE.length;
	var strTemp = "";


		if(v_length < 0){
			return"";
		}
		var iTemp = v_length -1;


			while(iTemp > -1){
				if(VALUE.charAt(iTemp) == w_space){}


					else{
						strTemp = VALUE.substring(0,iTemp +1);
						break;
					}
					iTemp = iTemp-1;
				}
				return strTemp;
		}


function LTrim(VALUE){
	var w_space = String.fromCharCode(32);


		if(v_length < 1){
			return"";
		}
		var v_length = VALUE.length;
		var strTemp = "";
		var iTemp = 0;


			while(iTemp < v_length){
				if(VALUE.charAt(iTemp) == w_space){}


					else{
						strTemp = VALUE.substring(iTemp,v_length);
						break;
					}
					iTemp = iTemp + 1;
				}
				return strTemp;
		}


function fn_validateDateMsg(DateA,DateB,msg)
{
	/*
		Function added to check for DateA <= DateB. The msg argument has been incorporated into the function
		Saurabh Chandla
		Date: 5 Nov 2004
	*/

	var chkdt = checkDates(DateA.value, DateB.value)
	if (chkdt == false)
	{
		alert(msg);
		//From_DT.focus();
		return false;
	}
	else
	{
		return true;
	}
}


/**
this function is used to check that the Number entered is greater than or equal to 1
**/
function fn_validNaturalNumber(intField, fieldLabel)
{
	/*
		Function added to check for positive integers
		Saurabh Chandla
	*/

	fieldValue=intField.value;
	if(isNaN(fieldValue)==true)
	{
		alert("Please enter a valid Number for " + fieldLabel);
		intField.value="";
		intField.focus();
		return false;		
	}	
	else
	{
		var decimalIndex=fieldValue.lastIndexOf(".");
		if (decimalIndex!=-1)
		{
			alert("Please enter a valid Integer for " + fieldLabel);
			intField.value="";
			intField.focus();
			return false;
		}
	}
	if(fieldValue<=0) 
	{
		alert("Please enter a positive value greater than zero for " + fieldLabel);
		intField.value="";
		intField.focus();
		return false;
	}
	return true
}
	/* URL encoding functions */

function URLEncode(str)
{
	// The Javascript escape and unescape functions do not correspond
	// with what browsers actually do...
	var SAFECHARS = "0123456789" +					// Numeric
					"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +	// Alphabetic
					"abcdefghijklmnopqrstuvwxyz" +
					"-_.!~*'() ,/";					// RFC2396 Mark characters
	var HEX = "0123456789ABCDEF";

	var plaintext = str;
	var encoded = "";
	for (var i = 0; i < plaintext.length; i++ ) {
		var ch = plaintext.charAt(i);
	    if (ch == " ") {
		    encoded += "+";				// x-www-urlencoded, rather than %20
		} else if (SAFECHARS.indexOf(ch) != -1) {
		    encoded += ch;
		} else {
		    var charCode = ch.charCodeAt(0);
			if (charCode > 255) {
			    alert( "Unicode Character '" 
                        + ch 
                        + "' cannot be encoded using standard URL encoding.\n" +
				          "(URL encoding only supports 8-bit characters.)\n" +
						  "A space (+) will be substituted." );
				encoded += "+";
			} else {
				encoded += "%";
				encoded += HEX.charAt((charCode >> 4) & 0xF);
				encoded += HEX.charAt(charCode & 0xF);
			}
		}
	} // for

	//document.URLForm.F2.value = encoded;
	//return false;
	return encoded;
};

function URLDecode(str)
{
   // Replace + with ' '
   // Replace %xx with equivalent character
   // Put [ERROR] in output if %xx is invalid.
   var HEXCHARS = "0123456789ABCDEFabcdef"; 
   var encoded = str;
   var plaintext = "";
   var i = 0;
   while (i < encoded.length) {
       var ch = encoded.charAt(i);
	   if (ch == "+") {
	       plaintext += " ";
		   i++;
	   } else if (ch == "%") {
			if (i < (encoded.length-2) 
					&& HEXCHARS.indexOf(encoded.charAt(i+1)) != -1 
					&& HEXCHARS.indexOf(encoded.charAt(i+2)) != -1 ) {
				plaintext += unescape( encoded.substr(i,3) );
				i += 3;
			} else {
				alert( 'Bad escape combination near ...' + encoded.substr(i) );
				plaintext += "%[ERROR]";
				i++;
			}
		} else {
		   plaintext += ch;
		   i++;
		}
	} // while
   //document.URLForm.F1.value = plaintext;
   return plaintext;
   //return false;
};
/** URL Encoding funtions ends */

function  float_gr_zero(floatField, fieldLabel,totalDigits,afterDecimal)
 {
  if(floatField.value !="")
  {  	 
     if (floatField.value <= 0)   	 
      {
        alert(""+fieldLabel+"Should not be less than or equal to Zero");
        floatField.select();
        floatField.focus(); 
        return false;
      }
     else
        validFloat_4(floatField, fieldLabel,totalDigits,afterDecimal);
   }     
 };
/* Function - as required by works  allowing a-z, A-Z, 0-9,/_-. */
function isValidAlNumSp(field) {
	var SAFECHARS = "0123456789" +					// Numeric
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +	// Alphabetic
					"abcdefghijklmnopqrstuvwxyz" +
					"-_./ (),&";

	if(inValidCharSet(field.value,SAFECHARS)) {
		return true;
	} else {
		alert("These special characters in this field value are not allowed !");
		if(field.focus) 
			field.focus();
		if(field.select) 
			field.select();
	}
}
 function DATE_COMPARE(from_dt,to_dt) {
    var a1=from_dt.value;
//   alert(from_dt+"---"+ to_dt);
   var dateStrArray = a1.split("/");  
     var b1=to_dt.value;
   var dateStrArray1 = b1.split("/");  
     var from_DT_TEST= new Date(dateStrArray[2],dateStrArray[1],dateStrArray[0]);
   var to_DT_TEST=new Date(dateStrArray1[2],dateStrArray1[1],dateStrArray1[0]);
 if(from_DT_TEST.getTime() > to_DT_TEST.getTime()) {
    	  return false
} 
return true;

   }
   function DATE_BETWEEN(from_dt,to_dt,mid_dt) {
    var a1=from_dt.value;
   var dateStrArray = a1.split("/");  
     var b1=to_dt.value;
   var dateStrArray1 = b1.split("/");  
    var c1=mid_dt.value;
    var dateStrArray2 = c1.split("/");   
     var from_DT_TEST= new Date(dateStrArray[2],dateStrArray[1],dateStrArray[0]);
   var to_DT_TEST=new Date(dateStrArray1[2],dateStrArray1[1],dateStrArray1[0]);
   var betweeen_dt=new Date(dateStrArray2[2],dateStrArray2[1],dateStrArray2[0]);
   
if(betweeen_dt.getTime() > from_DT_TEST.getTime() && betweeen_dt.getTime() < to_DT_TEST.getTime())
{
return true
}
else
{
return false;
}

   }
   
   function DATE_COMPARE_FINAL(from_dt,to_dt) {
    var a1=from_dt;
   // alert(from_dt+"---"+ to_dt);
   var dateStrArray = a1.split("/");  
     var b1=to_dt;
   var dateStrArray1 = b1.split("/");  
   var dd=add_zeroes(dateStrArray[0]);
   var dd1=add_zeroes(dateStrArray1[0]);
//   alert(dd+"===="+dd1);
     var from_DT_TEST= new Date(dateStrArray[2],dateStrArray[1],dd);
   var to_DT_TEST=new Date(dateStrArray1[2],dateStrArray1[1],dd1);
  //alert(from_DT_TEST +"***********"+to_DT_TEST);
 if(from_DT_TEST.getTime() > to_DT_TEST.getTime()) {
    	  return false
} 
return true;

   }
   function DATE_COMPARE_FINAL_REV(to_dt,from_dt) {
    var a1=from_dt;
   // alert(from_dt+"---"+ to_dt);
   var dateStrArray = a1.split("/");  
     var b1=to_dt;
   var dateStrArray1 = b1.split("/");  
     var from_DT_TEST= new Date(dateStrArray[2],dateStrArray[1],dateStrArray[0]);
   var to_DT_TEST=new Date(dateStrArray1[2],dateStrArray1[1],dateStrArray1[0]);
 if(from_DT_TEST.getTime() > to_DT_TEST.getTime()) {
    	  return false
} 
return true;

   }
   function Limit_Text(field,size)
            {
                var limitNum=size;
                msg=field.value;
                  if (msg.length > size) {
              alert('Not Allow More then ' +size +' Character');
              field.value = msg.substring(0, limitNum);
                }
            }
            
 function fn_DateCompare(DateA, DateB) {    
     
     var dateStrArray = DateA.split("/");  
    var dateStrArray1 = DateB.split("/");  
        var a = new Date();
        var b = new Date();
      //  alert(DateA +"---"+DateB);
        var mm1=returnMonth(dateStrArray[1]);
        var mm2=returnMonth(dateStrArray1[1]);
       // alert(dateStrArray[0]+"********"+dateStrArray[1]+"---"+dateStrArray[2]);
      // alert(mm1+"********"+mm2);
         a.setFullYear(dateStrArray[2], mm1, dateStrArray[0]);
         b.setFullYear(dateStrArray1[2],  mm2, dateStrArray1[0]);
      //   date2.setFullYear(2020, 0, 10);
      
      if (a.getTime() > b.getTime()) 
          {
              alert('DATE One >>> DATE 2');
          }
            if (a.getTime() < b.getTime()) 
          {
             alert('DATE One<<< DATE 2');
          }
           if (a.getTime() == b.getTime()) 
          {
             alert('DATE One ====== DATE 2');
          }
          
	
     
     
    }

function returnMonth(mm)
{  
    var v=0;
  //  alert(mm.substring(0,1));
    if (mm.substring(0,1)==0)
    {
      //  alert(mm.substring(1))
         v=parseInt(mm.substring(1))+1
        return v;
    }
    else
    {
          v=parseInt(mm)+1
        return v;
    }
    
}
 function compare_dates(DateA, DateB)
 {
      var dateStrArray = DateA.split("/");  
    var dateStrArray1 = DateB.split("/");          
     var date1 = new Date(dateStrArray[1]+'/'+dateStrArray[0]+'/'+dateStrArray[2]); // MM-dd-YYYY
       var date2 = new Date(dateStrArray1[1]+'/'+dateStrArray1[0]+'/'+dateStrArray1[2]); // MM-dd-YYYY
   
     if (date1>date2) return ("Date1 > Date2");
       else if (date1<date2) return ("Date2 > Date1");
   else return ("Date1 = Date2");   
 }
  function pop_MSG() {
            document.getElementById('popDiv').style.display = 'block';
            }
            function hide_MSG() {
            document.getElementById('popDiv').style.display = 'none';
            }
            function startBlink() {
            if (document.all) setInterval("doBlink()", 500)
        }
        function doBlink() {
            var blink = document.all.tags("BLINK")
            for (var i = 0; i < blink.length; i++)
                blink[i].style.visibility = blink[i].style.visibility == "" ? "hidden" : ""
        }
        function allLetter(inputtxt)
      { 
      var letters = /^[A-Za-z ]+$/;
      if(inputtxt.value=="")
      {
           return true;
      }
      if(inputtxt.value.match(letters))
      {
        return true;
      }
      else
      {
      alert('Please input alphabet characters only');
     // inputtxt.focus();
      inputtxt.value="";
      return false;
      }
      }
      function allNumeric(inputtxt)
   {
      var numbers = /^[0-9]+$/;
      if(inputtxt.value=="")
      {
           return true;
      }
      if(inputtxt.value.match(numbers))
      {
       return true;
      }
      else
      {
      alert('Please input numeric characters only');
      //inputtxt.focus();
       inputtxt.value="";
      return false;
      }
   } 
    function allNumericWithDecimal(inputtxt)
   {
      var numbers = /^[0-9]\d{0,9}(\.\d{1,2})?%?$/;
      if(inputtxt.value=="")
      {
           return true;
      }
      if(inputtxt.value.match(numbers))
      {
       return true;
      }
      else
      {
      alert('Please input numeric characters only');
    //  inputtxt.focus();
     inputtxt.value="";
      return false;
      }
   } 
   function DATE_COMPAIR(cur_DT,from_dt,to_dt,count)
             {
                  if (cur_DT.value != '')
                 {                 
	    if (!checkDates(cur_DT.value,'<%=DateUtil.getSysDate()%>'))
            {
            alert(' Date cannot be greator than Current date');
            cur_DT.focus();
            cur_DT.value='';
            count.value='';
            return false;
            }
        }
            if ((from_dt.value != '') && (to_dt.value != ''))
            {
                var frmDate = ChangeFormatDD2MM(from_dt.value);
            var toDate = ChangeFormatDD2MM(to_dt.value);
            var d = new Date(frmDate);    //today date
            var mill=new Date(toDate);    //Next millennium start date
            var diff = mill-d;    //difference in milliseconds
            var mtg = new String(diff/86400000 + 1);    //calculate days and convert to string
          if(mtg!="")
          {
            if(parseInt(mtg)>=1)
            {
            }
            else
            {
                alert('From Date cannot be greater than To Date');
              cur_DT.focus();
            cur_DT.value='';
            count.value='';
                return false
            }
        } 
              
            }
            return true;
             }
             function Future_Year(DateA, DateB)
 {
      var dateStrArray = DateA.split("/");  
     var dateStrArray1 = DateB.split("/");    
         var sys_year = dateStrArray[2];
         var enter_year = dateStrArray1[2];
         if(parseInt(enter_year)>parseInt(sys_year))
         {
             return false;
         }
         else{
            return true ;
         }
   
   
 }
 
       function CALbindFrom_Date_TEST(entry,event,format,yfrom,yto) {
//alert(status);
 
var status=document.forms[0].EMP_VALID_TYPE.value;
//alert(status);
if(status=='DIRECT')
{    
  yfrom='1987';
  yto='1998';
}

	CALentry=entry;
	CALformatstr=format;
	dt=CALparse(entry.value, format);
	sx=0; sy=0;
	if (event) sx=event.screenX; if (sx==0) sx=300; else sx += 5; if (sx>550) sx=sx-225;
	if (event) sy=event.screenY; if (sy==0) sy=100; else sy += 5; if (sy>400) sy=sy-180;
	if (dt==null) CALgo('CALcallback',sx,sy,yfrom,yto);
	else CALgo('CALcallback',sx,sy,yfrom,yto,dt.getYear(),dt.getMonth()+1);

}


function add_zeroes(number) {

    var my_string = number;
    if(number.length==1)
    {
         my_string = '0' + number;
    }
    else
    {
        my_string = number;
    }  

    return my_string;

}
function date_test(from_dt,to_dt)
{
    
    
        var a1=from_dt;
     var dateStrArray = a1.split("/");  
     var b1=to_dt;
   var dateStrArray1 = b1.split("/");  
  
     //var g1 = new Date(dateStrArray[2] ,parseInt(dateStrArray[1])-1 , dateStrArray[0]); 
    // (YYYY-MM-DD) 
   // var g2 = new Date(dateStrArray1[2], parseInt(dateStrArray1[1])-1, dateStrArray1[0]); 
    var g1 = dateStrArray[2]+add_zeroes(dateStrArray[1])+add_zeroes(dateStrArray[0]); 
   var g2 = dateStrArray1[2]+add_zeroes(dateStrArray1[1])+add_zeroes(dateStrArray1[0]); 
   
    // alert(g1+"---"+ g2);
     if (parseInt(g1) > parseInt(g2))
        return false;
    else
       return true;
  
}


function valid_Sis_Date_CHECK(dd)
{
    
   // alert('22222222222');
     var date2 = dd.value;
    var d = new Date();
    var day = d.getDate();
    var month = d.getMonth()+1;
      var year = d.getFullYear();
     var date1 = day+'/'+month+'/'+year;  
     
       var a1=date2;
     var dateStrArray = a1.split("/");  
     var b1=date1;
   var dateStrArray1 = b1.split("/");    
  
    var g1 = dateStrArray[2]+add_zeroes(dateStrArray[1])+add_zeroes(dateStrArray[0]); 
   var g2 = dateStrArray1[2]+add_zeroes(dateStrArray1[1])+add_zeroes(dateStrArray1[0]);    
    // alert(g1+"---"+ g2);
     if (parseInt(g1) > parseInt(g2))
        return false;
    else
       return true;
}

function DAY_DIFFERENCE_FINAL(from_dt,to_dt)
{   
    
               var lvfrmdt=from_dt;
		var lvtodt=to_dt;
		var one_day=1000*60*60*24;    

		var x=lvfrmdt.split("/");
		var y=lvtodt.split("/");
        
		var date1=new Date(x[2],(x[1]-1),x[0]);
		var date2=new Date(y[2],(y[1]-1),y[0])
   var Diff=Math.ceil((date2.getTime()-date1.getTime())/(one_day));
       return Diff;
  
}

function date_check_final(from_dt,to_dt)
{   
    
        var a1=from_dt;
     var dateStrArray = a1.split("/");  
     var b1=to_dt;
   var dateStrArray1 = b1.split("/");    
  
    var g1 = dateStrArray[2]+add_zeroes(dateStrArray[1])+add_zeroes(dateStrArray[0]); 
   var g2 = dateStrArray1[2]+add_zeroes(dateStrArray1[1])+add_zeroes(dateStrArray1[0]);    
    // alert(g1+"---"+ g2);
     if (parseInt(g1) > parseInt(g2))
        return false;
    else
       return true;
  
}

function date_check_final_SIS_ST(from_dt,to_dt)
{   
    
        var a1=from_dt;
     var dateStrArray = a1.split("/");  
     var b1=to_dt;
   var dateStrArray1 = b1.split("/");    
  
    var g1 = dateStrArray[2]+add_zeroes(dateStrArray[1])+add_zeroes(dateStrArray[0]); 
   var g2 = dateStrArray1[2]+add_zeroes(dateStrArray1[1])+add_zeroes(dateStrArray1[0]);    
   //  alert(g1+"---"+ g2);
     if (parseInt(g1) >= parseInt(g2))
        return false;
    else
       return true;
  
}


function specialcharecterCheck(field)

            {

                var iChars = "!`@#$%^&*()+=-[]\\\';,/{}|\":<>?~_";   

                var data = field.value;

                for (var i = 0; i < data.length; i++)

                {      

                    if (iChars.indexOf(data.charAt(i)) != -1)

                    {    

                    alert ("Your string has special characters. \nThese are not allowed.");

                        field.value = "";

                    return false; 

                    } 

                }

            }
 function validDecimal(intField, fieldLabel)
        {
                var fieldValue=intField.value;
                if(isNaN(fieldValue)==true)
                {
                        alert("Please enter a valid Number for " + fieldLabel);
                        intField.value="";
                        intField.focus();		
                }
                else
                {
                        var decimalIndex=fieldValue.lastIndexOf(".");
                  if (fieldValue.lastIndexOf("+")!=-1)
                        {
                                alert("Please enter a valid Integer for " + fieldLabel);
                                intField.value="";
                                intField.focus();
                        }
            if (fieldValue.lastIndexOf("-")!=-1)
                        {
                                alert("Please enter a valid Integer for " + fieldLabel);
                               intField.value="";
                                intField.focus();
                        }
          if (fieldValue.lastIndexOf(".")!=-1)
		   {
		   var decimalStr = fieldValue.toString().split('.')[1];
          if(decimalStr.length>2)
		  {
		        alert("Please enter the amount in two decimal only for " + fieldLabel);
                               intField.value="";
                                intField.focus();
		   }
		   }


                }
        }