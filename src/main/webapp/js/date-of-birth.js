$(function() {

	 $(document).ready(function() {
		 $('.error').hide();
		 $("#dob").blur(function(){
	    	   var dtVal=$('#dob').val();
	    	   if(ValidateDate(dtVal))
	    	   {
	    	      $('.error').hide();
	    	   }
	    	   else
	    	   {
	    	     $('.error').show();
	    	     
	    	   }
	    	   });
	    	});
	
	function ValidateDate(dtValue){
	var dtRegex = new RegExp(/\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/);
	return dtRegex.test(dtValue);
	}
	
});


