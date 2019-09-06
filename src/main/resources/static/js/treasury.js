var TreasuryManager = (function() {

	var TYPE_BEST_SELLER = "bestSeller";
	var TYPE_NEW_ARRIVAL = "newArrival";
	var TYPE_CHILDREN = "children";
	var TYPE_READING_CHALLENGE = "readingChallenge";
	var SPECIAL_BOOKS_SHOW_LIMIT = 5;
	var NAVIGATION_FORWARD = "forward";
	var NAVIGATION_BACKWARD = "backward";
	var ACTION_ADD = "add";
	var ACTION_REMOVE = "remove";
	var LIST_NAME_READING_QUEUE = "readingQueue";
	var LIST_NAME_WISHLIST = "wishlist";

	var specialBooks = [];
	var specialBooksCount = 0;
	var specialBookStartIndex = 0;
	var specialBookEndIndex = 0;
	
	var booksPageNumber = 0;
	var isFirstPage = true;
	var isLastPage = false;
	var totalBookLimit = 30;
	var firstBookNumber, lastBookNumber, totalBookCount;
	
	var searchTimer = null;

	function getSpecialBooks(type) {
		var request = $.ajax({
			type: "GET",
			url: "/treasury/get/books/special",
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
				renderSpecialBooks();			
			}

		});
	};
	
	function getAllBooks(searchText) {
		var param = {
				"pageNumber": booksPageNumber,
				"searchText": searchText
		}
		var request = $.ajax({
			type: "GET",
			url: "/treasury/get/books/all",
			dataType: 'JSON',
			data: param
		});
		
		request.done(function(response) {
			if(response.success) {
				data = response.dataMap;
				books = data.bookList;
				totalBookCount = data.totalCount;
				isFirstPage = data.isFirstPage;
				isLastPage = data.isLastPage;
				
				if(booksPageNumber == 0) {
					firstBookNumber = booksPageNumber + 1;
					lastBookNumber = booksPageNumber + books.length;
				} else {
					firstBookNumber = (booksPageNumber * totalBookLimit) + 1;
					lastBookNumber = (firstBookNumber + books.length) - 1;
				}
				
				if(books && books.length > 0) {
					renderAllBooks(books);
					$(".totalBookCount").text(totalBookCount);
					$(".pageBookCount").text(firstBookNumber + "-" + lastBookNumber);
					$(".pageNumber").text(booksPageNumber+1);
				}
			}
		});
	}
	
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
		
		renderSpecialBooks();
	}
	
	function renderSpecialBooks() {
		let container = $(".special-books");
		let container_navigation_right = $(".special-books-navigation-right");
		let container_navigation_left = $(".special-books-navigation-left");
		let toggle_reading_queue = $(".toggle-reading-queue");
		let toggle_withlist = $(".toggle-wishlist");
		
		container.empty();
		
		let left = "<i class='fa fa-angle-left special-books-navigation special-books-navigation-left'></i>";				
		let right = "<i class='fa fa-angle-right special-books-navigation special-books-navigation-right'></i>";
		
		container.append(left);
		for(let i=specialBookStartIndex; i<=specialBookEndIndex; i++) {
			let element = updateTemplateDataForSpecialBooks(specialBooks[i]);
			
			container.append(element);
			emptyTempContainer();
			
		}
		container.append(right);
		
		container_navigation_right.click(function() {
			showSpecialBooks(NAVIGATION_FORWARD);
		});
		
		container_navigation_left.click(function() {
			showSpecialBooks(NAVIGATION_BACKWARD);
		});
		
		toggleClickAction(container);
	}
	
	function renderAllBooks(books) {
		let container = $(".all-books");
		container.empty();
		
		for(let i=0; i<books.length; i++) {
			let element = updateTemplateData(books[i]);
			
			container.append(element);
			emptyTempContainer();
		}
		
		toggleClickAction(container);
	}
	
	function toggleClickAction(container) {
		$(container).find(".toggle-reading-queue").click(function(e) {
			e.preventDefault();
			toggleList(this, LIST_NAME_READING_QUEUE);
		});
		
		$(container).find(".toggle-wishlist").click(function(e) {
			e.preventDefault();
			toggleList(this, LIST_NAME_WISHLIST);
		});
	}

	function updateTemplateData (book) {
		let template = $("#book-item").html();
		let temp = $("#temp-container");
		temp.html(template);
		
		let bookName = $(temp).find("#book-name");
		let bookNameStr = book.name;
		if(bookNameStr.length > 10) {
			bookNameStr = bookNameStr.substr(0,10).trim() + "...";
		}
		bookName.text(bookNameStr);
		bookName.attr("href", "/book/get/" + book.id);
		bookName.attr('title',book.name);
		
		let bookImage = $(temp).find("#book-image");
		bookImage.attr("src", "/get/image/" + book.id);
		bookImage.parent().attr("href", "/book/get/" + book.id);
		
		
		let readingQueue = $(temp).find(".toggle-reading-queue");
		let readingQueueIcon = readingQueue.find("i");
		
		let wishList = $(temp).find(".toggle-wishlist");
		let wishListIcon = wishList.find("i");
				
		readingQueue.attr("data-id", book.id);
		wishList.attr("data-id", book.id);
		
		if(book.readingQueue) {
			readingQueue.addClass(ACTION_REMOVE);
			readingQueueIcon.addClass("color-green");
		} else {
			readingQueue.addClass(ACTION_ADD);
			readingQueueIcon.removeClass("color-green");
		}
		
		if(book.wishlist) {
			wishList.addClass(ACTION_REMOVE);
			wishListIcon.addClass("color-red");
		} else {
			wishList.addClass(ACTION_ADD);
			wishListIcon.removeClass("color-red");
		}
		
		return temp.html();
	}
	
	function updateTemplateDataForSpecialBooks (book) {
		let template = $("#special-book-item").html();
		let temp = $("#temp-container");
		temp.html(template);
		
		let bookImage = $(temp).find("#book-image");
		bookImage.attr("src", "/get/image/" + book.id);
		bookImage.parent().attr("href", "/book/get/" + book.id);
		
		return temp.html();
	}
	
	function emptyTempContainer() {
		$("#temp-container").empty();
	}
	
	function toggleList(element, listName) {
		let elm = $(element);
		let i = elm.find("i");		
		let bookId = elm.attr('data-id');
		let action = "";
		let book = null;
		
		$.each(specialBooks, function(key, value) {
			if (value.id == bookId) {
				book = value;
				return;
			}
		});
		
		if(elm.hasClass(ACTION_ADD)) {
			action = ACTION_ADD;
		} else {
			action = ACTION_REMOVE;
		}
		
		let data = {
				"bookId": bookId,
				"action": action
			};
		
		if(listName == LIST_NAME_READING_QUEUE) {
			toggleReadingQueue(elm, i, data, book, action)
		} else if (listName == LIST_NAME_WISHLIST) {
			toggleWishlist(elm, i, data, book, action);
		}
	}
	
	function toggleReadingQueue(elm, i, data, book, action) {
		var request = $.ajax({
			type: "POST",
			url: "/user/update/readingqueue",
			dataType: 'JSON',
			data: data
		});
		
		request.done(function(response){
			if(response.success) {
				if(action == ACTION_ADD) {
					elm.removeClass(ACTION_ADD);
					elm.addClass(ACTION_REMOVE);
					i.addClass("color-green");
					book.readingQueue = true;
				} else {
					elm.removeClass(ACTION_REMOVE);
					elm.addClass(ACTION_ADD);
					i.removeClass("color-green");
					book.readingQueue = false;
				}
			} else {
				alert(response.data);
			}
		});
	}
	
	function toggleWishlist(elm, i, data, book, action) {
		var request = $.ajax({
			type: "POST",
			url: "/user/update/wishlist",
			dataType: 'JSON',
			data: data
		});
		
		request.done(function(response){
			if(response.success) {
				if(action == ACTION_ADD) {
					elm.removeClass(ACTION_ADD);
					elm.addClass(ACTION_REMOVE);
					i.addClass("color-red");
					book.wishlist = true;
				} else {
					elm.removeClass(ACTION_REMOVE);
					elm.addClass(ACTION_ADD);
					i.removeClass("color-red");
					book.wishlist = false;
				}
			} else {
				alert(response.data);
			}
		});
	}
	
	(function() {
		getSpecialBooks(TYPE_BEST_SELLER);
		getAllBooks("");
		
		$(".best-seller-selection").click(function(e) {
			e.preventDefault();
			getSpecialBooks(TYPE_BEST_SELLER);
		});
		
		$(".new_arrival_selection").click(function(e) {
			e.preventDefault();
			getSpecialBooks(TYPE_NEW_ARRIVAL);
		});
		
		$(".children_selection").click(function(e) {
			e.preventDefault();
			getSpecialBooks(TYPE_CHILDREN);
		});
		
		$(".reading_challenge_selection").click(function(e) {
			e.preventDefault();
			getSpecialBooks(TYPE_READING_CHALLENGE);
		});
		
		$(".browseLeft").click(function(e) {
			e.preventDefault();
			if(!isFirstPage) {
				booksPageNumber--;
				getAllBooks("");
			}
		});
		
		$(".browseRight").click(function(e) {
			e.preventDefault();
			if(!isLastPage) {
				booksPageNumber++;
				getAllBooks("");
			}
		});
		
		$(".bookSearch").keyup(function(e) {
			e.preventDefault();
			
			let text = $(this).val();
			
			if(searchTimer) {
				clearTimeout(searchTimer);
				searchTimer = null;
			}
						
			searchTimer = setTimeout(function() {
				firstBookNumber = 0;
				lastBookNumber = 0;
				totalBookCount = 0;
				booksPageNumber = 0;
				isFirstPage = true;
				isLastPage = false;
								
				getAllBooks(text);
			}, 1000);
		});
	})();

	return {
		/*TYPE_BEST_SELLER: TYPE_BEST_SELLER,
		TYPE_NEW_ARRIVAL: TYPE_NEW_ARRIVAL,
		TYPE_READING_CHALLANGE: TYPE_READING_CHALLANGE,
		getSpecialBooks: getSpecialBooks*/
	}
})();