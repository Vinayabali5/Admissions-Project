$(function() {
	
	$("#studentLetter").click(function(){
		 var jsonPacket = {};
		$.ajax({
			dataType : "json",
            contentType : "application/json",
            type : "GET",
            url : "/wordGenerator/getById",
            data : JSON.stringify(jsonPacket),
            success : function(data) {
		    alert("data");
		    }
		});
	});
	
});