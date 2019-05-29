var TreasuryManager = (function() {

	var TYPE_BEST_SELLER = "bestSeller";
	var TYPE_NEW_ARRIVAL = "newArrival";
	var TYPE_READING_CHALLANGE = "readingChallange";
	var SPECIAL_BOOKS_SHOW_LIMIT = 5;
	var NAVIGATION_FORWARD = "forward";
	var NAVIGATION_BACKWARD = "backward";

	var specialBooks = [];
	var specialBooksCount = 0;
	var specialBookStartIndex = 0;
	var specialBookEndIndex = 0;

	function getSpecialBooks(type) {
		var request = $.ajax({
			type: "GET",
			url: "/treasury/get/books",
			dataType: 'JSON',
			data: {
				"type": type
			}
		});

		request.done(function(response){
			if(response.success) {
				specialBooks = response.data;
				specialBooksCount = specialBooks.length;
				
				if(specialBooksCount >= SPECIAL_BOOKS_SHOW_LIMIT) {
					specialBookEndIndex = SPECIAL_BOOKS_SHOW_LIMIT-1;
				} else {
					specialBookEndIndex = specialBooksCount-1;
				}				
				render();			
			}

		});
	};
	
	function showSpecialBooks(navigation) {
						
		if(navigation == NAVIGATION_FORWARD) {
			if((specialBookEndIndex+1) == specialBooksCount) {
				return;
			} else {
				let diff = specialBooksCount - (specialBookEndIndex + 1);
				if(diff >= SPECIAL_BOOKS_SHOW_LIMIT) {
					specialBookStartIndex = specialBookEndIndex + 1;
					specialBookEndIndex = specialBookStartIndex + (SPECIAL_BOOKS_SHOW_LIMIT-1)
				} else {
					specialBookEndIndex = specialBooksCount-1;
					specialBookStartIndex = specialBookEndIndex - (SPECIAL_BOOKS_SHOW_LIMIT-1)
				}
			}
			
		} else if(navigation == NAVIGATION_BACKWARD) {
			if(specialBookStartIndex == 0) {
				return;
			} else {
				if(specialBookStartIndex > (SPECIAL_BOOKS_SHOW_LIMIT-1)) {
					specialBookEndIndex = specialBookStartIndex-1;
					specialBookStartIndex = specialBookEndIndex - (SPECIAL_BOOKS_SHOW_LIMIT-1);
				} else {
					specialBookStartIndex = 0;
					specialBookEndIndex = specialBookStartIndex + (SPECIAL_BOOKS_SHOW_LIMIT-1);
				}
			}
		}
		
		render();
	}
	
	function render() {
		$(".special-books").empty();
		
		var left = "<i class='fa fa-angle-left special-books-navigation special-books-navigation-left'></i>";				
		var right = "<i class='fa fa-angle-right special-books-navigation special-books-navigation-right'></i>";
		
		$(".special-books").append(left);
		for(var i=specialBookStartIndex; i<=specialBookEndIndex; i++) {
			updateTemplateData(specialBooks[i]);
		}
		$(".special-books").append(right);
		
		$(".special-books-navigation-right").click(function() {
			showSpecialBooks(NAVIGATION_FORWARD);
		});
		
		$(".special-books-navigation-left").click(function() {
			showSpecialBooks(NAVIGATION_BACKWARD);
		});
	}

	function updateTemplateData (book) {
		var template = $("#book-item").html();
		var temp = $("#temp-container");
		temp.html(template);
		$(temp).find("#book-name").text(book.name);
		$(temp).find("#book-image").attr("src", "/get/image/" + book.id);
		$(".special-books").append(temp.html());
		temp.empty();
	}
	
	(function() {
		getSpecialBooks(TYPE_BEST_SELLER);
		
		$(".best-seller-selection").click(function(e) {
			e.preventDefault();
			getSpecialBooks(TYPE_BEST_SELLER);
		});
		
		$(".new_arrival_selection").click(function(e) {
			e.preventDefault();
			getSpecialBooks(TYPE_NEW_ARRIVAL);
		});
	})();

	return {
		TYPE_BEST_SELLER: TYPE_BEST_SELLER,
		TYPE_NEW_ARRIVAL: TYPE_NEW_ARRIVAL,
		TYPE_READING_CHALLANGE: TYPE_READING_CHALLANGE,
		getSpecialBooks: getSpecialBooks
	}
})();