
$(document).ready(function() {
	
	$('.contact-address-dialog').hide();
	$(".contact-dialog-container .dialog-button").click(function() {
		  $(this).hide();
       $(this).parent().children('.contact-address-dialog').show();
	});
	
  var blnManualInputDisabled = !$('#contact0-line1').is('readonly');
	    $('#contact0-manual-lookup').click(function() {
	        blnManualInputDisabled = !blnManualInputDisabled;
	        $('#contact0-line1').prop('readonly', blnManualInputDisabled);
	        $('#contact0-line2').prop('readonly', blnManualInputDisabled);
	        $('#contact0-line3').prop('readonly', blnManualInputDisabled);
	        $('#contact0-line4').prop('readonly', blnManualInputDisabled);
	        $('#contact0-line5').prop('readonly', blnManualInputDisabled);
	        $('#contact0-town').prop('readonly', blnManualInputDisabled);
	        $('#contact0-county').prop('readonly', blnManualInputDisabled);
	    });

	    var blnManualInputDisabled = !$('#contact1-line1').is('readonly');
	    $('#contact1-manual-lookup').click(function() {
	        blnManualInputDisabled = !blnManualInputDisabled;
	        $('#contact1-line1').prop('readonly', blnManualInputDisabled);
	        $('#contact1-line2').prop('readonly', blnManualInputDisabled);
	        $('#contact1-line3').prop('readonly', blnManualInputDisabled);
	        $('#contact1-line4').prop('readonly', blnManualInputDisabled);
	        $('#contact1-line5').prop('readonly', blnManualInputDisabled);
	        $('#contact1-town').prop('readonly', blnManualInputDisabled);
	        $('#contact1-county').prop('readonly', blnManualInputDisabled);
	    });
	 
	$('#postcode-lookup').click(function() {
		
	});
	    
	    
});

