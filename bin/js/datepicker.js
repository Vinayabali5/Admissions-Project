$(function() {
	
    $('#dob').attr('type', 'text').datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth : true,
        changeYear : true,
        yearRange: "-22:-14"
    });
    $('.date-picker').each(function(i, obj) {
        $(obj).attr('type', 'text');  
        $(obj).attr('pattern', '(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}');  
        $(obj).datepicker({
            dateFormat: "dd/mm/yy",
            changeMonth : true,
            constrainInput: false
         });
//        $(obj).on("blur", function(e) {
//            var input = e.target;
//            var date = input.value;
//            input.value = new Date(date).toISOString();
//        });
     });
    
    $('.time-picker').timepicker({ 'timeFormat': 'H:i', 'step': 15});
});


