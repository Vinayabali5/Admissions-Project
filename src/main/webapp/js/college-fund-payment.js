/**
 * 
 */
$(function() {	
	/**
	  * This allows College Fund Payments to be added to the list of College Fund Entry
	  */ 

		 function addCollegeFund() {
		var jsonPacket = new Object();
		jsonPacket.id = $("#ncfp-id").val();
		jsonPacket.studentId = $("#ncfp-student-id").val();
		jsonPacket.paymentDate = $("#ncfp-paymentDate").val();
		jsonPacket.amount = $("#ncfp-amount").val();
		jsonPacket.chequeDate = $("#ncfp-chequeDate").val();
		jsonPacket.payee = $("#ncfp-payee").val();
		jsonPacket.giftAid = $("#ncfp-giftAid").is(":checked");
		jsonPacket.cash = $("#ncfp-cash").is(":checked");
		var method = "POST";
		var url = "/collegeFundPayments/"
		if (jsonPacket.id != "" && jsonPacket.id != null) {
			method = "PUT";
			url = url + jsonPacket.id;
		}
		$.ajax({
			datatype : "json",
			contentType : "application/json",
			type : method,
			url : url,
			data : JSON.stringify(jsonPacket),
			success : function(data) {
				reloadCollegeFundTable();
			}
		});

		dialog.dialog("close");
		return true;
	}
		 function editCollegeFund(id) {
		$.ajax({
			dataType : "json",
			type : "GET",
			url : "/collegeFundPayments/" + id,
			success : function(data) {
				$('#ncfp-id').val(data.id);
				$('#ncfp-student-id').val(data.studentId);
				$('#ncfp-paymentDate').val(data.paymentDate);
				$('#ncfp-amount').val(data.amount);
				$('#ncfp-chequeDate').val(data.chequeDate);
				$('#ncfp-payee').val(data.payee);
				$('#ncfp-giftAid').attr("checked", data.giftAid);
				$('#ncfp-cash').attr("checked", data.cash);
				dialog.dialog("open");
			}
		});
	}
	 
	function reloadCollegeFundTable() {
        var studentId = $('#student-id').val();
        var table = $('#collegeFund-table');
        var data = [];
        $.ajax({
            dataType : "json",
            type : "GET",
            url : "/collegeFundPayments/getByStudent/" + studentId,
            success : function(data) {
                $('#collegeFund-table > tbody > tr').remove();
                $.each(data, function(key, value) {
                    $('<tr>').append(
                            $('<td>').text(value.paymentDate),
                            $('<td>').text(value.amount),
                            $('<td>').text(value.chequeDate),
                            $('<td>').text(value.payee),
                            $('<td>').text(value.giftAid ? 'Yes' : 'No'),
                            $('<td>').text(value.cash ? 'Yes' : 'No'),
                            $('<td>').html('<a id="' + value.id + '" class="edit-collegeFund btn btn-default" href="/collegeFundPayments/get/' + '" role="button">Edit</a>')
                    ).appendTo('#collegeFund-table > tbody');
            });
                configureCollegeFundButtons();
            }
        });
	}
	
	/**
	  * This allows configureCollegeFundButtons
	  */ 
		 function configureCollegeFundButtons() {
		$(".edit-collegeFund").button().on("click", function(e) {
			e.preventDefault();
			editCollegeFund(this.id);
		});
	}
	 /* This creates the dialog box */ 
	 dialog = $("#college-fund-payment-dialog").dialog({
		  autoOpen : false,
	        modal: true,
	        title: "Create/edit college fund payment",
	        height : 350,
	        width : 500,
	        buttons: {
	        	"Save" : addCollegeFund,
	                "Cancel": function () {
	                	dialog.dialog('close'); 
	            }
	        }
	    });

	 /**
	  * This hides the create/edit college fund payment dialog box
	  */
	 $("#college-fund-payment-dialog").hide();
	
	 $("#create-collegeFund").button().on("click", function(e) {
		 e.preventDefault();
		 $('#ncfp-id').val(null);
	        $('#ncfp-student-id').val($('#student-id').val());
		 dialog.dialog("open");
	 });
	 
	 $("#reload-collegeFund-table").button().on("click", function(e) {
	        e.preventDefault();
	        reloadCollegeFundTable();
	    });
	 
	 reloadCollegeFundTable();
	 
});