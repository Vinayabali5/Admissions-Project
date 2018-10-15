$(function() {
    var _dupData = {};

    function clearApplication() {
        if (confirm("This will clear the current Application form." + "\n" + "Are you sure?")) {
            $("#application-form").trigger('reset');
            document.getElementById("application-form").reset();
            $("#twin-match").dialog('close');
        } else {
            // $("#twin-match").dialog('close');
        }
    }

    var findDuplicates = function() {
        _dupData = {};
        var surname = $('#surname').val();
        var dob = $('#dob').datepicker("getDate");
        if (surname !== "" && dob !== null) {
            var jsonPacket = {};
            jsonPacket.surname = surname;
            jsonPacket.dob = (new Date(dob.toString())).toJSON();
            $.ajax({
                dataType : "json",
                contentType : "application/json",
                type : "POST",
                url : "/duplicate-detection/",
                data : JSON.stringify(jsonPacket),
                success : function(data) {
                    if (!data) {
                        console.log("No match found");
                    } else {
                        console.log("Match Found");
                        _dupData = data;
                        $("#twin-match").show();
                        $("#twin-match").dialog({
                            autoOpen : true,
                            width : 1000,
                            height : 400,
                            modal : true,
                            buttons : {
                                "Duplicate Application" : clearApplication,
                                Continue : function() {
                                    $(this).dialog('close');
                                }
                            },
                        });

                        $('#twin-table > tbody > tr').remove();
                        $.each(data.matches, function(key, value) {
                            $('<tr>').append(
                                    $('<td>').text(value.person.surname),
                                    $('<td>').text(value.person.firstName),
                                    $('<td>').text(value.person.middleNames),
                                    $('<td>').text(value.person.dob),
                                    $('<td>').text(value.person.home),
                                    $('<td>').text(value.person.mobile),
                                    $('<td>').text(value.person.address.line1 + value.person.address.town + value.person.address.postcode),
                                    $('<td>').html(
                                            '<a id="' + key + '" data-index="' + key
                                                    + '" class="select-twin ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" href="/duplicate-detection/' + value.applicationId
                                                    + '" role="button">Select Twin</a>')).appendTo('#twin-table > tbody');

                        });
                        configureTwinButtons()

                    }
                }
            });
        }
    }

    function configureTwinButtons() {
        $(".select-twin").button().on("click", function(e) {
            e.preventDefault();
            selectTwin(this.attributes["data-index"].value);
        });
    }

    function selectTwin(index) {
        var home = _dupData.matches[index].person.home;
        copyHome(home);
        var address = _dupData.matches[index].person.address;
        copyAddress(address);
        var contacts = _dupData.matches[index].person.contacts;
        copyContacts(contacts);
        var addressId = _dupData.matches[index].person.contacts[0].contact.address;
        copyAddressId(addressId);
        var addressId1 = _dupData.matches[index].person.contacts[1].contact.address;
        copyAddressId1(addressId1);
        $("#twin-match").dialog('close');
    }

    function copyHome(home) {
        if (home) {
            $('#home').val(home);
            $('#contact-0-home').val(home);
            $('#contact-1-home').val(home);
        }
    }

    function copyAddressId(addressId) {
        if (addressId != null) {
            var selectorPart = '#contact0';
            $(selectorPart + '-line1').val(addressId.line1);
            $(selectorPart + '-line2').val(addressId.line2);
            $(selectorPart + '-line3').val(addressId.line3);
            $(selectorPart + '-line4').val(addressId.line4);
            $(selectorPart + '-line5').val(addressId.line5);
            $(selectorPart + '-town').val(addressId.town);
            $(selectorPart + '-county').val(addressId.county);
            $(selectorPart + '-postcode').val(addressId.postcode);
            $(selectorPart + '-buildingName').val(addressId.buildingName);
            $(selectorPart + '-subBuilding').val(addressId.subBuilding);
        } else {
            console.log('No Parent Contacts Found');
        }
    }
    function copyAddressId1(addressId1) {
        if (addressId1 != null) {
            var selectorPart = '#contact1';
            $(selectorPart + '-line1').val(addressId.line1);
            $(selectorPart + '-line2').val(addressId.line2);
            $(selectorPart + '-line3').val(addressId.line3);
            $(selectorPart + '-line4').val(addressId.line4);
            $(selectorPart + '-line5').val(addressId.line5);
            $(selectorPart + '-town').val(addressId.town);
            $(selectorPart + '-county').val(addressId.county);
            $(selectorPart + '-postcode').val(addressId.postcode);
            $(selectorPart + '-buildingName').val(addressId.buildingName);
            $(selectorPart + '-subBuilding').val(addressId.subBuilding);
        } else {
            console.log('No Parent Contacts Found');
        }
    }

    function copyAddress(address) {
        if (address) {
            console.log("Copying Twin Address");
            $('#addressId').val(address.id);
            $('#line1').val(address.line1);
            $('#line2').val(address.line2);
            $('#line3').val(address.line3);
            $('#line4').val(address.line4);
            $('#line5').val(address.line5);
            $('#town').val(address.town);
            $('#county').val(address.county);
            $('#postcode').val(address.postcode);
            $('#buildingName').val(address.buildingName);
            $('#subBuilding').val(address.subBuilding);
        } else {
            console.log("No Twin Address Found");
        }
    }

    function copyContacts(contacts) {
        if (contacts) {
            contacts.forEach(function(c, index) {
                var selectorPart = '#contact-' + index;
                $(selectorPart + '-id').val(c.id);
                $(selectorPart + '-contactType').val(c.contactType.id);
                $(selectorPart + '-title').val(c.contact.title.id);
                $(selectorPart + '-first-name').val(c.contact.firstName);
                $(selectorPart + '-surname').val(c.contact.surname);
                $(selectorPart + '-home').val(c.contact.home);
                $(selectorPart + '-mobile').val(c.contact.mobile);
                $(selectorPart + '-work').val(c.contact.work);
                $(selectorPart + '-email').val(c.contact.email);
                $(selectorPart + '-contactable').attr("checked", c.contactable);
                $(selectorPart + '-preferred').attr("checked", c.preferred);
            });
        } else {
            console.log('No Contacts Found');
        }
    }

    $("#twin-match").hide();
    $("#surname,#dob").on('change', findDuplicates);

});
