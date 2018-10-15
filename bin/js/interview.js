/**
 * The Interview module for displaying and editing the fields of Interview
 */
var app = angular.module('InterviewApp', []);
app.controller('InterviewCtrl', function($scope, $http) {
	var self = this;
	self.searchTerm = '';
	self.noResults = true;
	$scope.noResults = true;
	$scope.interview = {} ;

	/* Event to search */
	this.search = function() {
		var search = this.searchTerm;
		var url = "/applications/search/" + search;
		var appList = this.applicationList;
		$http.get(url).success(function(response) {
			if (response.length !== 0) {
				$scope.applicationList = response;
				this.noResults = false;
				$scope.noResults = false;
			} else {
				 $scope.applicationList = {};
                    this.noResults = true;
                    $scope.noResults = true;
			}
			$('#interview-results').show();
		});
	};

	/* Event to load the data into the fields */
	self.loadInterviewData = function(id) {
		var url = "/interviews/getByStudent/" + id;
		$("#student-id").val(id);
		$http.get(url).success(function(data) {
			$scope.interview = data;
			reloadInterviewRequestTable();
			$("#interview-details-section").show();
			$('#interview-results').hide();
		});
		//
		//$('#reload-interview-request-table').click();
	};

	/* Event to configureRequestInterviewButtons */
	function configureRequestInterviewButtons() {
        $('.delete-interview-request').on('click', function(e) {
           e.preventDefault();
           var requestId = e.target.id.split('-')[1];
           deleteInterviewRequest(requestId);
        });
    }

	/* Event to delete the request from the table */
	function deleteInterviewRequest(requestId) {
	    $.ajax({
	        type: 'DELETE',
	        url: '/requests/' + requestId,
	        success: function(data) {
	        	reloadInterviewRequestTable();
	        }
		});
	}

	/* Event to reload the request table to the interview field */
	function reloadInterviewRequestTable() {
		var studentId = $('#student-id').val();
		var table = $('#request-interview-table');
		var data = [];
		$.ajax({
            dataType: "json",
            type : "GET",
            url : "/requests/getByStudent/" + studentId,
            success : function(data) {
                $('#request-interview-table > tbody > tr').remove();
                $.each(data, function(key, value) {
                    $('<tr>').append(
                            $('<td>').text(value.request),
                            $('<td>').text(value.description),
                            $('<td>').html('<a id="request-' + value.id + '" class="delete-interview-request button ui-button">X</a>')
                        ).appendTo('#request-interview-table > tbody');
                });
                configureRequestInterviewButtons();
            }

        });
	}

	/*
	 * Date Format
	*/
	$('#dob,#interviewDate').attr('type', 'text').datepicker({
        dateFormat: "dd/mm/yy",
        changeMonth : true,
        changeYear : true
    });
    $('input[type="date"]').each(function(i, obj) {
        $(obj).attr('type', 'text');
        $(obj).attr('pattern', '(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}');
        $(obj).datepicker({
            dateFormat: "dd/mm/yy",
            changeMonth : true,
            constrainInput: false
         });
     });

	 /*
 	 * Save The Interview
 	*/
 	$("#saveInterview").on("click", function() {
 		var method = "PUT";
         var url = "/interviews/" + $scope.interview.studentId;
         $.ajax({
             datatype: "json",
             contentType: "application/json",
             type: method,
             url: url,
             data: JSON.stringify($scope.interview),
             success: function (data, status, jqXHR) {
            	 if(jqXHR.status == 200) {
            		 alert('Successfully updated. Reference No: ' + data.studentRef);
            	 } else {
            		 alert('A problem occurred.');
            	 } 
            		 
             }
 	 	});
 	});


	$('#toggle-interview-results').on('click', function() {
		$('#interview-results').toggle();
	});

	/* Event to add the request to the interview field */
	$("#add-interview-request").on("click", function() {
		var studentId = $("#student-id").val();
		var request = $("#new-interview-request").val();
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
				reloadInterviewRequestTable();
				$("#new-interview-request").val('');
			},
			error: function (jqXHR) {
				if(jqXHR.status && jqXHR.status==409){
					alert("Request Code already exist."+"\n"+"Please Enter different Request Code");
				}
			}
		});
	});


	 /* This allows Automatic request capitialisation */
 	$("#new-interview-request").on('input', function(evt) {
 		  var input = $(this);
 		  var start = input[0].selectionStart;
 		  $(this).val(function (_, val) {
 		    return val.toUpperCase();
 		  });
 		  input[0].selectionStart = input[0].selectionEnd = start;
 	});

 	/** This allows the valid format for the request field */
 	$("#new-interview-request").keyup(function() {
 		$('span.error-keyup').remove();
 	    var inputVal = $(this).val();
 	    var characterReg = /^[FLUPIS]\-[A-Z]{3}$/;
 	    if(!characterReg.test(inputVal)) {
 	    	$(this).after('<span class="error error-keyup"> Please Enter the valid request code.</span>');
 	    }
 	});

	$("#reload-interview-request-table").button().on("click", function(e) {
	    e.preventDefault();
	    reloadInterviewRequestTable();
	});


});

$("#interview-details-section").hide();

$( document ).ready(function() {
    $("#search").trigger("focus");
});
