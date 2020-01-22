var BlogManager = (function() {
	
	function getAllBlogs() {
		var request = $.ajax({
			type: "GET",
			url: "/blogs/all/",
			dataType: 'JSON',
		});
		
		request.done(function(response) {
			if(response.success) {
				console.log(response.data);
			}
		});
	}
	
	(function() {
		getAllBlogs();
	})();
})();