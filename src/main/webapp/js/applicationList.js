jQuery(function($) {

	var items = $("table tbody tr");

	var numItems = items.length;
	var perPage = 15;

	items.slice(perPage).hide();

	$(".pagination-page").pagination({
		items : numItems,
		itemsOnPage : perPage,
		cssStyle : "light-theme",
		onPageClick : function(pageNumber) {
			var showFrom = perPage * (pageNumber - 1);
			var showTo = showFrom + perPage;

			items.hide().slice(showFrom, showTo).show();
		}
	});

	var checkFragment = function() {
		var hash = window.location.hash || "#page-1";
		hash = hash.match(/^#page-(\d+)$/);

		if (hash)
			$("#pagination").pagination("selectPage", parseInt(hash[1]));
	};
	$(window).bind("popstate", checkFragment);
	checkFragment();
	

});