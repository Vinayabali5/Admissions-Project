/**
 * This JavaScript library is used to process the New Contact Dialog form.
 */

$(function() {
    var dialog, form,

    // http://www.whatwg.org/specs/web-apps/current-work/multipage/states-of-the-type-attribute.html#e-mail-state-%28type=email%29
    emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/, name = $("#name"), email = $("#email"), password = $("#password"), allFields = $(
            []).add(name).add(email).add(password), tips = $(".validateTips");

    /**
     * This Javascript is used to disable the manual button
     */
    var blnManualInputDisabled = !$('#nc-line1').is('readonly');

    $('#nc-manual-lookup').click(function() {
        blnManualInputDisabled = !blnManualInputDisabled;
        $('#nc-line1').prop('readonly', blnManualInputDisabled);
        $('#nc-line2').prop('readonly', blnManualInputDisabled);
        $('#nc-line3').prop('readonly', blnManualInputDisabled);
        $('#nc-line4').prop('readonly', blnManualInputDisabled);
        $('#nc-line5').prop('readonly', blnManualInputDisabled);
        $('#nc-town').prop('readonly', blnManualInputDisabled);
        $('#nc-county').prop('readonly', blnManualInputDisabled);
    });

    function updateTips(t) {
        tips.text(t).addClass("ui-state-highlight");
        setTimeout(function() {
            tips.removeClass("ui-state-highlight", 1500);
        }, 500);
    }

    function checkLength(o, n, min, max) {
        if (o.val().length > max || o.val().length < min) {
            o.addClass("ui-state-error");
            updateTips("Length of " + n + " must be between " + min + " and " + max + ".");
            return false;
        } else {
            return true;
        }
    }

    function checkRegexp(o, regexp, n) {
        if (!(regexp.test(o.val()))) {
            o.addClass("ui-state-error");
            updateTips(n);
            return false;
        } else {
            return true;
        }
    }

    function validate() {
        var valid = true;
        allFields.removeClass("ui-state-error");

        valid = valid && checkLength(name, "username", 3, 16);
        valid = valid && checkLength(email, "email", 6, 80);
        valid = valid && checkLength(password, "password", 5, 16);

        valid = valid && checkRegexp(name, /^[a-z]([0-9a-z_\s])+$/i, "Username may consist of a-z, 0-9, underscores, spaces and must begin with a letter.");
        valid = valid && checkRegexp(email, emailRegex, "eg. ui@jquery.com");
        valid = valid && checkRegexp(password, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9");
        return valid;
    }

    function addContact() {
        // #TODO Add the client side validation routine checks
        var jsonPacket = new Object();
        jsonPacket.id = $("#nc-id").val();
        jsonPacket.personId = $("#nc-person-id").val();
        jsonPacket.title = {};
        jsonPacket.title.id = $("#nc-title").val();
        jsonPacket.firstName = $("#nc-first-name").val();
        jsonPacket.surname = $("#nc-surname").val();
        jsonPacket.home = $("#nc-home").val();
        jsonPacket.mobile = $("#nc-mobile").val();
        jsonPacket.work = $("#nc-work").val();
        jsonPacket.email = $("#nc-email").val();
        if ($("#nc-postcode").val() != '' && $("#nc-postcode").val() != undefined && $("#nc-postcode").val() != null) {
            jsonPacket.address = {};
            jsonPacket.address.line1 = $("#nc-line1").val();
            jsonPacket.address.line2 = $("#nc-line2").val();
            jsonPacket.address.line3 = $("#nc-line3").val();
            jsonPacket.address.line4 = $("#nc-line4").val();
            jsonPacket.address.line5 = $("#nc-line5").val();
            jsonPacket.address.town = $("#nc-town").val();
            jsonPacket.address.county = $("#nc-county").val();
            jsonPacket.address.postcode = $("#nc-postcode").val();
        }
        jsonPacket.contactType = {};
        jsonPacket.contactType.id = $("#nc-type").val();
        jsonPacket.contactable = $("#nc-contactable").is(":checked");
        jsonPacket.preferred = $("#nc-preferred").is(":checked");
        var method = "POST";
        var url = "/contacts/"
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
                reloadContactTable();
            }
        });

        dialog.dialog("close");
        return true;
    }

    function editContact(id) {
        $.ajax({
            dataType : "json",
            type : "GET",
            url : "/contacts/" + id,
            success : function(data) {
                $('#nc-id').val(data.id);
                $('#nc-person-id').val(data.personId);
                $('#nc-type').val(data.contactType != null ? data.contactType.id : null);
                $('#nc-title').val(data.title != null ? data.title.id : null);
                $('#nc-first-name').val(data.firstName);
                $('#nc-surname').val(data.surname);
                $('#nc-home').val(data.home);
                $('#nc-mobile').val(data.mobile);
                $('#nc-work').val(data.work);
                $('#nc-email').val(data.email);
                if (data.address != null) {
                    $('#nc-line1').val(data.address.line1);
                    $('#nc-line2').val(data.address.line2);
                    $('#nc-line3').val(data.address.line3);
                    $('#nc-line4').val(data.address.line4);
                    $('#nc-line5').val(data.address.line5);
                    $('#nc-town').val(data.address.town);
                    $('#nc-county').val(data.address.county);
                    $('#nc-postcode').val(data.address.postcode);
                } else {
                    $('#nc-line1').val('');
                    $('#nc-line2').val('');
                    $('#nc-line3').val('');
                    $('#nc-line4').val('');
                    $('#nc-line5').val('');
                    $('#nc-town').val('');
                    $('#nc-county').val('');
                    $('#nc-postcode').val('');
                }
                $('#nc-contactable').attr("checked", data.contactable);
                $('#nc-preferred').attr("checked", data.preferred);
                dialog.dialog("open");
            }
        });
    }

    function reloadContactTable() {
        var personId = $('#person-id').val();
        var table = $('#contact-table');
        var data = [];
        $.ajax({
            dataType : "json",
            type : "GET",
            url : "/contacts/getByPerson/" + personId,
            success : function(data) {
                $('#contact-table > tbody > tr').remove();
                $.each(data, function(key, value) {
                    $('<tr>').append(
                            $('<td>').text(value.contactType.description),
                            $('<td>').text(value.title == null ? '' : value.title.description) ,
                            $('<td>').text(value.firstName),
                            $('<td>').text(value.surname),
                            $('<td>').text(value.home == null ? '' : value.home),
                            $('<td>').text(value.mobile == null ? '' : value.mobile),
                            $('<td>').text(value.email == null ? '' : value.email),
                            $('<td>').text(value.address == null ? '' : 'Yes'),
                            $('<td>').text(value.contactable ? 'Yes' : 'No'),
                            $('<td>').text(value.preferred ? 'Yes' : 'No'),
                            $('<td>').html(
                                    '<a id="' + value.id + '" class="edit-contact btn btn-sm btn-default" href="/contacts/get/' + value.id
                                            + '" role="button">Edit</a> <a id="' + value.id + '" class="delete-contact btn btn-sm btn-default">Delete</a>')).appendTo('#contact-table > tbody')
                  
                });
                configureButtons();
            }
        });
    }

    function configureButtons() {
        $(".edit-contact").button().on("click", function(e) {
            e.preventDefault();
            editContact(this.id);
        });
        
        $('.delete-contact').on('click', function(e) {
            e.preventDefault();
            var id = e.target.id.split('-');
            deleteContact(id);
         });
       
    }

    function deleteContact(id){
    	 $.ajax({
             type: 'DELETE',
             url: '/contacts/' + id,
             success: function(data) {
                 reloadContactTable();
             }
          });
    }
    
    
    dialog = $("#contact-dialog-form").dialog({
        autoOpen : false,
        height : 750,
        width : 800,
        modal : true,
        buttons : {
            "Save" : addContact,
            Cancel : function() {
                dialog.dialog("close");
            }
        },
        close : function() {
            form[0].reset();
            allFields.removeClass("ui-state-error");
        }
    });

    form = dialog.find("form").on("submit", function(event) {
        event.preventDefault();
        addContact();
    });

    $("#create-contact").button().on("click", function(e) {
        e.preventDefault();
        $('#nc-id').val(null);
        $('#nc-person-id').val($('#person-id').val());
        dialog.dialog("open");
    });

    $("#reload-contact-table").button().on("click", function(e) {
        e.preventDefault();
        reloadContactTable();
    });

    reloadContactTable();

});
