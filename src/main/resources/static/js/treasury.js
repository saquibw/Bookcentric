var TreasuryManager = (function() {

	var TYPE_BEST_SELLER = "bestSeller";
	var TYPE_NEW_ARRIVAL = "newArrival";
	var TYPE_CHILDREN = "children";
	var TYPE_READING_CHALLENGE = "readingChallenge";
	var SPECIAL_BOOKS_SHOW_LIMIT = 6;
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

	var currentPageNumber = 0;
	var isFirstPage = true;
	var isLastPage = false;
	var totalPageCount = 0;
	var totalBookLimit = 30;
	var firstBookNumber, lastBookNumber, totalBookCount;
	var totalPageNumberShow = 10;

	var searchTimer = null;
	
	var SESSION_STORAGE_SEARCH_KEY = "searchTag";

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

	function getAllBooks(searchText, searchTag) {						
		var param = {
				"pageNumber": currentPageNumber,
				"searchText": searchText ? searchText : "",
				"searchTag": searchTag ? searchTag : "",
		}
		
		if(searchText || searchTag) {
			$(".clearSearch").removeClass("hidden");
		}
		
		$(".all-books").empty();
		$(".pageNumberContainer").empty();
		$(".all-books").append("<div class='loader'></div>");
		$(".searchTag").html("");		
				
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
				totalBookCount = data.totalBookCount;
				isFirstPage = data.isFirstPage;
				isLastPage = data.isLastPage;
				totalPageCount = data.totalPageCount;

				if(currentPageNumber == 0) {
					firstBookNumber = currentPageNumber + 1;
					lastBookNumber = currentPageNumber + books.length;
				} else {
					firstBookNumber = (currentPageNumber * totalBookLimit) + 1;
					lastBookNumber = (firstBookNumber + books.length) - 1;
				}

				if(books && books.length > 0) {
					renderAllBooks(books);
					$(".totalBookCount").text(totalBookCount);
					$(".pageBookCount").text(firstBookNumber + "-" + lastBookNumber);
					$(".pageNumber").text(currentPageNumber+1);
					
					renderPageNumber();
					
					if(searchTag) {
						let t = searchTag.split("_");
						$(".searchTag").html("for " + t[0] + " <i>" + t[1] + "</i>");
					}
					
					if(searchText) {
						$(".searchTag").html("for search <i>" + searchText + "</i>");
					}
				}
			} else {
				$(".all-books").find(".loader").remove();
			}
		});
		
		request.fail(function() {
			$(".all-books").find(".loader").remove();
		});
	}
	
	function renderPageNumber() {
		const container = $(".pageNumberContainer");
		container.empty();
		let start, end = 0;
		let selected = currentPageNumber+1;
		
		if(totalPageCount < 10) {
			start = 1;
			end = totalPageCount;
		} else {
			if(selected <= 5) {
				start = 1;
				end = 10;
			} else if(selected >= (totalPageCount-5)) {
				end = totalPageCount;
				start = end-9;
			} else {
				start = selected - 4;
				end = selected + 5;
			}
		}
		
		for(i=start; i<=end; i++) {
			let temp = "" + i-1 + "";
			let c = "";
			if(i == selected) c = "class='active'";
			let a = "<a href='javascript:void(0)' " + c + " data-page=" + temp + "><label>" + i +"</label></a>";
			container.append(a);
		}
		
		container.find("a").click(function(e) {
			e.preventDefault();
			
			let pageNo = $(this).data("page");
			
			if(currentPageNumber != pageNo) {				
				currentPageNumber = pageNo;				
				let searchText = $(".bookSearch").val();
				let searchTag = getFromStorage(SESSION_STORAGE_SEARCH_KEY);
				getAllBooks(searchText, searchTag);
				moveToPosition();
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

		container.empty();

		for(let i=specialBookStartIndex; i<=specialBookEndIndex; i++) {
			let element = updateTemplateDataForSpecialBooks(specialBooks[i]);

			container.append(element);
			emptyTempContainer();

		}
	}
	
	function attachSpecialBookNavigationListener() {
		let container_navigation_right = $(".special-books-navigation-right");
		let container_navigation_left = $(".special-books-navigation-left");

		container_navigation_right.click(function() {
			showSpecialBooks(NAVIGATION_FORWARD);
		});

		container_navigation_left.click(function() {
			showSpecialBooks(NAVIGATION_BACKWARD);
		});
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

	function generateSearchBoxData(searchText) {
		var param = {
				"pageNumber": currentPageNumber,
				"searchText": searchText,
				"searchTag": ""
		}
		var request = $.ajax({
			type: "GET",
			url: "/treasury/get/books/search/all",
			dataType: 'JSON',
			data: param
		});

		request.done(function(response) {
			if(response.success) {
				data = response.dataMap;
				books = data.bookList;
				if(books && books.length > 0) {
					let loader = $(".bookSearchLoader");
					loader.addClass("hidden");
					var bookList = [];
					var genreList = [];
					var authorList = [];
					var tagList = [];
					searchText = searchText.toLowerCase();

					books.forEach(function(val, key) {
						let bookName = val.name;
						let bookId = val.id;
						let b = {bookId, bookName};
						let genreName = val.genreName;
						let authorName = val.authorName;
						let tagName = val.tagName;

						if(bookName.toLowerCase().includes(searchText)) {
							bookList.push(b);
						}
						if(genreName.toLowerCase().includes(searchText)) {
							insertInTypeList(genreName, genreList, searchText);
						}
						if(authorName.toLowerCase().includes(searchText)) {
							insertInTypeList(authorName, authorList, searchText);
						}
						if(tagName.toLowerCase().includes(searchText.toLowerCase())) {
							insertInTypeList(tagName, tagList, searchText);
						}
					});
					populateSearchBoxData("Book", bookList);
					populateSearchBoxData("Author", authorList);
					populateSearchBoxData("Genre", genreList);
					populateSearchBoxData("Tag", tagList);
					
					searchBoxOnClick();
				} else {
					toggleSearchOptionBoxView(false);
				}
			}
		});
	}
	
	function populateSearchBoxData(name, list) {
		if(list && list.length > 0) {
			let ul = "<div style='overflow-y: auto; max-height:250px;'><ul class='list-unstyled'>";
			ul += "<li><strong>" + name + "</strong></li>";		
			ul += polulateSearchBoxRows(name, list);		
			ul += "</ul></div>";
			
			$(".treasurySearchItems").append(ul);
		}		
	}
	
	function polulateSearchBoxRows(name, list) {
		let row = "<li>";
		let rowEnd = "</li>";
		let output = "";
		if(name == "Book") {
			list.forEach(function(val, key) {
				let bookNameStr = val.bookName;
				if(bookNameStr.length > 28) {
					bookNameStr = bookNameStr.substr(0,28).trim() + "...";
				}
				let col = row + "<a href='/book/get/" + val.bookId + "' target='_blank' data-toggle='tooltip' title='" + val.bookName + "'>" + bookNameStr + "</a>" + rowEnd;
				output += col;
			});
		} else {
			let className = name.toLowerCase() + "Element";
			row = "<li class='" + className + "'>";
			list.forEach(function(val, key) {
				let itemNameStr = val;
				if(itemNameStr.length > 25) itemNameStr = itemNameStr.substr(0,25).trim() + "...";
				let col = row + "<a href='javascript:void(0)' data-toggle='tooltip' title='" + val + "'>" + itemNameStr + "</a>" + rowEnd;
				output += col;
			});
		}
		return output;
	}

	function insertInTypeList(typeName, typeList, searchText) {		
		if (typeName.includes(', ')) {
			let tempArr = typeName.split(', ');
			tempArr.forEach(function(val, key) {
				if(val.toLowerCase().includes(searchText)) {
					if(!doesArrayContain(typeList, val)) typeList.push(val);
				}
			});
		} else {
			if(!doesArrayContain(typeList, typeName)) typeList.push(typeName);
		}
	}

	function doesArrayContain(array, data) {
		if(array.includes(data)) return true;
		return false;
	}

	function toggleSearchOptionBoxView(doShow) {
		let container = $(".treasurySearchOptions");
		let searchBox = $(".bookSearch");
		let loader = $(".bookSearchLoader");

		if(doShow) {
			$(".treasurySearchItems").empty();
			container.width(searchBox.width());
			container.removeClass("hidden");
			loader.removeClass("hidden");
		} else {
			container.addClass("hidden");
			loader.addClass("hidden");
		}
	}
	
	function searchBoxOnClick() {
		let container = $(".treasurySearchItems");
		$(container).find(".genreElement").click(function(e) {
			e.preventDefault();
			let text = $(this).find("a").text();
			setToStorage(SESSION_STORAGE_SEARCH_KEY, "genre_" + text);
			location.reload();
		});
		
		$(container).find(".tagElement").click(function(e) {
			e.preventDefault();
			let text = $(this).find("a").text();
			setToStorage(SESSION_STORAGE_SEARCH_KEY, "tag_" + text);
			location.reload();
		});
		
		$(container).find(".authorElement").click(function(e) {
			e.preventDefault();
			let text = $(this).find("a").text();
			setToStorage(SESSION_STORAGE_SEARCH_KEY, "author_" + text);
			location.reload();
		});
	}
	
	function setToStorage(key, value) {
		sessionStorage.setItem(key, value);
	}
	
	function getFromStorage(key) {
		return sessionStorage.getItem(key);
	}
	
	function removeFromStorage(key) {
		sessionStorage.removeItem(key);
	}
	
	function moveToPosition() {
		window.scrollTo(0, 900);
	}
	
	function getYear() {
		let yearPlaceholder = $("#bookcentricYear");
		var year = new Date().getFullYear();
		if(yearPlaceholder) {
			$(yearPlaceholder).text(year);
		}		
	}

	(function() {
		let searchTagKey = getFromStorage(SESSION_STORAGE_SEARCH_KEY);
		
		getSpecialBooks(TYPE_BEST_SELLER);
		getAllBooks("", searchTagKey);
		
		attachSpecialBookNavigationListener();
		
		getYear();

		$(".best-seller-selection").click(function(e) {
			e.preventDefault();
			specialBookStartIndex = 0;
			getSpecialBooks(TYPE_BEST_SELLER);
		});

		$(".new_arrival_selection").click(function(e) {
			e.preventDefault();
			specialBookStartIndex = 0;
			getSpecialBooks(TYPE_NEW_ARRIVAL);
		});

		$(".children_selection").click(function(e) {
			e.preventDefault();
			specialBookStartIndex = 0;
			getSpecialBooks(TYPE_CHILDREN);
		});

		$(".reading_challenge_selection").click(function(e) {
			e.preventDefault();
			specialBookStartIndex = 0;
			getSpecialBooks(TYPE_READING_CHALLENGE);
		});

		$(".browseLeft").click(function(e) {
			e.preventDefault();
			if(!isFirstPage) {
				let searchText = $(".bookSearch").val();
				let searchTag = getFromStorage(SESSION_STORAGE_SEARCH_KEY);
				currentPageNumber--;
				getAllBooks(searchText, searchTag);
				moveToPosition();
			}
		});

		$(".browseRight").click(function(e) {
			e.preventDefault();
			if(!isLastPage) {
				let searchText = $(".bookSearch").val();
				let searchTag = getFromStorage(SESSION_STORAGE_SEARCH_KEY);
				currentPageNumber++;
				getAllBooks(searchText, searchTag);
				moveToPosition();
			}
		});
		
		$(".browseToFirst").click(function(e) {
			e.preventDefault();
			if(!isFirstPage) {
				let searchText = $(".bookSearch").val();
				let searchTag = getFromStorage(SESSION_STORAGE_SEARCH_KEY);
				currentPageNumber = 0;
				getAllBooks(searchText, searchTag);
				moveToPosition();
			}
		});
		
		$(".browseToLast").click(function(e) {
			e.preventDefault();
			if(!isLastPage) {
				let searchText = $(".bookSearch").val();
				let searchTag = getFromStorage(SESSION_STORAGE_SEARCH_KEY);
				currentPageNumber = totalPageCount-1;
				getAllBooks(searchText, searchTag);
				moveToPosition();
			}
		});
		
		$(".clearSearch").click(function(e) {
			e.preventDefault();
			$(".bookSearch").val("");
			removeFromStorage(SESSION_STORAGE_SEARCH_KEY);
			currentPageNumber = 0;
			$(this).addClass("hidden");
			
			getAllBooks("", "");
		});

		$(".bookSearch").keyup(function(e) {
			e.preventDefault();

			let text = $(this).val();
			if(e.which == 13) {
				if(searchTimer) {
					clearTimeout(searchTimer);
					searchTimer = null;
				}
				
				firstBookNumber = 0;
				lastBookNumber = 0;
				totalBookCount = 0;
				currentPageNumber = 0;
				isFirstPage = true;
				isLastPage = false;
				
				toggleSearchOptionBoxView(false);

				getAllBooks(text.trim(), "");
			} else {
				if(searchTimer) {
					clearTimeout(searchTimer);
					searchTimer = null;
				}

				searchTimer = setTimeout(function() {
					if(text && text.length > 0) {
						toggleSearchOptionBoxView(true);
						generateSearchBoxData(text.trim());
					} else {
						$(".treasurySearchOptions").addClass("hidden");
					}						
				}, 1500);				 				
			}			
		});
	})();
})();