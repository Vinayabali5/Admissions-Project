
$(function() {
	
	/**
     * This allows Automatic request capitialisation
     */
  
	$("#new-request").on('input', function(evt) {
		  var input = $(this);
		  var start = input[0].selectionStart;
		  $(this).val(function (_, val) {
		    return val.toUpperCase();
		  });
		  input[0].selectionStart = input[0].selectionEnd = start;
		});
	
/** This allows the valid format for the request field */
	$("#new-request").keyup(function() {
		 $('span.error-keyup').remove();
		    var inputVal = $(this).val();
		    var characterReg = /^[FLUPIS]\-[A-Z]{3}$/;
		    if(!characterReg.test(inputVal)) {
		     $(this).next('button').after('<span class="error error-keyup"> Please Enter the valid request code.</span>');
		    }
	});
	
    function configureRequestButtons() {
        $('.delete-request').on('click', function(e) {
           e.preventDefault();
           var requestId = e.target.id.split('-')[1];
           deleteRequest(requestId);
        });
    }
        
    function reloadRequestTable() {
        var studentId = $('#student-id').val();
        var table = $('#request-table');
        var data = [];
        $.ajax({
            dataType: "json",
            type : "GET",
            url : "/requests/getByStudent/" + studentId,
            success : function(data) {
                $('#request-table > tbody > tr').remove();
                $.each(data, function(key, value) {
                    $('<tr>').append(
                            $('<td>').text(value.request),
                            $('<td>').text(value.description),
                            $('<td>').html('<a id="request-' + value.id + '" class="delete-request button ui-button">X</a>')
                        ).appendTo('#request-table > tbody');
                });
                configureRequestButtons();
            }
        
        });
    }
    
    function deleteRequest(requestId) {
        $.ajax({
            type: 'DELETE',
            url: '/requests/' + requestId,
            success: function(data) {
                reloadRequestTable();
            }
         });
    }
    
    /**
     * This allows extra requests to be added to the list of request for an
     * application
     */

    $("#add-request").on("click", function(e) {
        e.preventDefault(); 
        var studentId = $("#student-id").val();
        var request = $("#new-request").val();
        var jsonPacket = {};
        jsonPacket.studentId = studentId;
        jsonPacket.request = request; 
        
        $.ajax({
            datatype: "json",
            contentType: "application/json",
            type: "POST",
            url: "/requests/",
            data: JSON.stringify(jsonPacket),
            success: function (data) {
                reloadRequestTable();
                $("#new-request").val('');
            },
            error: function (jqXHR) {
            	 if(jqXHR.status && jqXHR.status==409){
            		 alert("Request Code already exist."+"\n"+"Please Enter different Request Code");
            	 }
            	//alert("Request Code already exist."+"\n"+"Please Enter different Request Code");
            }
        });	
    });
    
    $("#new-request").on( "keypress", function(evt){
        if (evt.which == 13) {
            evt.preventDefault();
            $("#add-request").click();
        }
    });
       
    $("#reload-request-table").button().on("click", function(e) {
        e.preventDefault();
        reloadRequestTable();
    });

    reloadRequestTable();
    
 });