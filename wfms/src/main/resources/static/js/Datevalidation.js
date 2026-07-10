function checkDatesCommon(from_dt,to_dt)
{
    if(from_dt==''|| to_dt=='')
    {
		return true;
            }
            
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
//Start changes by shailendra gairola 31-03-2012