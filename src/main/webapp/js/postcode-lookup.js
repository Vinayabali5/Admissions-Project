/**
 * This Javascript is used to disable the manual button
 */

$(document).ready(function() {
    var blnManualInputDisabled = !$('#line1').is('readonly');

    $('#manual-lookup').click(function() {
        blnManualInputDisabled = !blnManualInputDisabled;
        $('#buildingName').prop('readonly', blnManualInputDisabled);
        $('#subBuilding').prop('readonly', blnManualInputDisabled);
        $('#line1').prop('readonly', blnManualInputDisabled);
        $('#line2').prop('readonly', blnManualInputDisabled);
        $('#line3').prop('readonly', blnManualInputDisabled);
        $('#line4').prop('readonly', blnManualInputDisabled);
        $('#line5').prop('readonly', blnManualInputDisabled);
        $('#town').prop('readonly', blnManualInputDisabled);
        $('#county').prop('readonly', blnManualInputDisabled);
    });

    /**
     * This Javascript is used to process the postcode lookup
     */
    $('.postcode-lookup').click(function() {
        // Storing return form information
        addressListDialog.targetForm = $(this).parents('div.postcode-lookup-address').attr('id');
        console.log(addressListDialog);
        // Get Postcode
        var postcode
        switch (addressListDialog.targetForm) {
            case 'main-address-form':
            case 'contact-address-dialog-form':
                postcode = $("#" + addressListDialog.targetForm + " input[name='postcode']").val();
                break;
            case 'contact0-address-form':
                postcode = $('#contact0-postcode').val();
                break;
            case 'contact1-address-form':
                postcode = $('#contact1-postcode').val();
                break;
        }

        // #TODO refine error handing here
        if (postcode != undefined && postcode != "" && postcode != null) {
            var url = '/postcodes/search/' + postcode;
            $.ajax({
                dataType : "json",
                contentType : "application/json",
                type : "GET",
                url : url,
                success : function(data) {
                    console.log("- retrieved data");
                    var options = $('#pl-address-list');
                    options.find("option").remove();
                    $.each(data, function(key, value) {
                        options.append($("<option></option>").attr("value", value.id).text(value.streetAddress + ", " + value.place));
                    });
                    addressListDialog.dialog("open");
                }
            });
        } else {
            console.log("Invalid postcode");
        }
    });

    // Select Address
    function selectAddress() {
        var id = $('#pl-address-list').val();
        // #TODO refine error handing here
        if (id != undefined && id != null) {
            var url = '/postcodes/retrieve/' + id;
            $.ajax({
                dataType : "json",
                contentType : "application/json",
                type : "GET",
                url : url,
                success : function(data) {
                    console.log("- retrieved data");

                    var form = $('#' + addressListDialog.targetForm);
                    console.log(form);

                    var selector
                    var control = $(selector);
                    switch (addressListDialog.targetForm) {
                        case 'main-address-form':
                        case 'contact-address-dialog-form':
                            buildingName = $('#' + addressListDialog.targetForm + ' input[name="buildingName"]').val(data.buildingName);
                            subBuilding = $('#' + addressListDialog.targetForm + ' input[name="subBuilding"]').val(data.subBuilding);
                            line1 = $('#' + addressListDialog.targetForm + ' input[name="line1"]').val(data.line1);
                            line2 = $('#' + addressListDialog.targetForm + ' input[name="line2"]').val(data.line2);
                            line3 = $('#' + addressListDialog.targetForm + ' input[name="line3"]').val(data.line3);
                            line4 = $('#' + addressListDialog.targetForm + ' input[name="line4"]').val(data.line4);
                            line5 = $('#' + addressListDialog.targetForm + ' input[name="line5"]').val(data.line5);
                            town = $('#' + addressListDialog.targetForm + ' input[name="town"]').val(data.town);
                            county = $('#' + addressListDialog.targetForm + ' input[name="county"]').val(data.county);
                            postcode = $('#' + addressListDialog.targetForm + ' input[name="postcode"]').val(data.postcode);
                            break;
                        case 'contact0-address-form':
                            buildingName = $('#contact0-buildingName').val(data.buildingName);
                            subBuilding = $('#contact0-subBuilding').val(data.subBuilding);
                            line1 = $('#contact0-line1').val(data.line1);
                            line2 = $('#contact0-line2').val(data.line2);
                            line3 = $('#contact0-line3').val(data.line3);
                            line4 = $('#contact0-line4').val(data.line4);
                            line5 = $('#contact0-line5').val(data.line5);
                            town = $('#contact0-town').val(data.town);
                            county = $('#contact0-county').val(data.county);
                            postcode = $('#contact0-postcode').val(data.postcode);
                            break;
                        case 'contact1-address-form':
                            buildingName = $('#contact1-buildingName').val(data.buildingName);
                            subBuilding = $('#contact1-subBuilding').val(data.subBuilding);
                            line1 = $('#contact1-line1').val(data.line1);
                            line2 = $('#contact1-line2').val(data.line2);
                            line3 = $('#contact1-line3').val(data.line3);
                            line4 = $('#contact1-line4').val(data.line4);
                            line5 = $('#contact1-line5').val(data.line5);
                            town = $('#contact1-town').val(data.town);
                            county = $('#contact1-county').val(data.county);
                            postcode = $('#contact1-postcode').val(data.postcode);
                            break;
                    }

                    addressListDialog.dialog("close");
                }
            });
        } else {
            console.log("Invalid ID");
        }

    }

    function configureButtons() {
        $(".add-user").button().on("click", function(e) {
            e.preventDefault();
            addUser();
        });
    }
    var addressListDialog = $("#address-list-dialog").dialog({
        autoOpen : false,
        height : 600,
        width : 550,
        modal : true,
        buttons : {
            "Select" : selectAddress,
            Cancel : function() {
                addressListDialog.dialog("close");
            }
        },
        close : function() {
            addressListForm[0].reset();
            $('#pl-address-list').find("option").remove();
        }
    });

    var addressListForm = addressListDialog.find("form").on("submit", function(event) {
        event.preventDefault();
        addUser();
    });

    $('#pl-address-list').on('dblclick', function(e) {
        selectAddress();
    });
    $('#pl-address-list').on('keypress', function(e) {
        if (e.which == 13) {
            e.preventDefault();
            selectAddress();
        }
    });

    $('.postcode').on('keypress', function(e) {
        if (e.keyCode == 13) {
            e.preventDefault();
            addressListDialog.targetForm = $(this).parents('div.postcode-lookup-address').attr('id');
            var button
            switch (addressListDialog.targetForm) {
                case 'main-address-form':
                    $('#postcode-lookup').trigger('click');
                    break;
                case 'contact-address-dialog-form':
                    $('#nc-postcode-lookup').trigger('click');
                    break;
                case 'contact0-address-form':
                    $('#contact0-postcode-lookup').trigger('click');
                    break;
                case 'contact1-address-form':
                    $('#contact1-postcode-lookup').trigger('click');
                    break;
            }
        }
    });

});
