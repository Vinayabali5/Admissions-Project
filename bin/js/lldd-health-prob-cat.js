/**
 * This file used to add and remove LLDD Health Problem Categories from
 * applications.
 */
$(function() {

    function configureLLDDButtons() {
        $('.delete-lldd-health-prob').on('click', function(e) {
            e.preventDefault();
            var uri = '/llddHealthProb' + e.target.pathname;
            $.ajax({
                type : "DELETE",
                url : uri,
                success : function(data) {
                    reloadLLDDTable();
                }
            });
        });
    }

    function reloadLLDDTable() {
        var studentId = $('#student-id').val();
        var table = $('#lldd-table');
        var data = [];
        $.ajax({
            dataType : "json",
            type : "GET",
            url : "/llddHealthProb/getByStudent/" + studentId,
            success : function(data) {
                $('#lldd-table > tbody > tr').remove();
                $.each(data, function(key, value) {
                    $('<tr>').append(
                            $('<td>').text(value.llddDescription),
                            $('<td>').html(
                                    '<a id="lldd-' + value.llddHealthProbId + '" href="/' + studentId + '/' + value.llddHealthProbId + '" class="delete-lldd-health-prob button ui-button">X</a>'))
                            .appendTo('#lldd-table > tbody');
                });
                configureLLDDButtons();
            }

        });
    }

    // Add LLDDHealthProbCat
    $("#add-lldd-health-prob").on("click", function(e) {
        e.preventDefault();
        var studentId = $("#student-id").val();
        var llddHealthProb = $("#new-lldd-health-prob").val();
        if (llddHealthProb == -1) {
            alert("Please select an LLDD Health Problem Category to add then try again.");
        } else {
            var jsonPacket = {};
            jsonPacket.studentId = studentId;
            jsonPacket.llddHealthProbId = llddHealthProb;
    
            $.ajax({
                datatype : "json",
                contentType : "application/json",
                type : "POST",
                url : "/llddHealthProb/",
                data : JSON.stringify(jsonPacket),
                success : function(data) {
                    reloadLLDDTable();
                },
                error : function(jqXHR) {
                    if (jqXHR.status && jqXHR.status == 409) {
                        alert("The LLDD and Health Probelm already exist.");
                    }
                }
            });
        }
    });

    $("#reload-lldd-table").button().on("click", function(e) {
        e.preventDefault();
        reloadLLDDTable();
    });

    $("#new-lldd-health-prob").on( "keypress", function(evt){
        if (evt.which == 13) {
            evt.preventDefault();
            $("#add-lldd-health-prob").click();
        }
    });
    
    reloadLLDDTable();

});