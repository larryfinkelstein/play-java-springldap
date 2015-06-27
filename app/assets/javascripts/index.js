$(function() {
	//var list = [ "aaa", "abb", "acc" ];
	//var list = $.get("/labels");
	
	//alert(list);
	
	$("#uid").autocomplete({
		//source: "/labels"
		//source: list
		source: function (request, response) {
			$.ajax({
				url: "/person/search/"+request.term,
				dataType: "json",
				minLength: 2,
				success: function(data) {
					response($.map(data, function(item) {
						return [{
							label: item.cn+" ("+item.uid+")",
							value: item.uid
						}];
					}));
				}
			});
		}
	});
});
