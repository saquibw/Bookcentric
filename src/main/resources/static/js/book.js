var BookManager = (function() {
	
	var ACTION_ADD = "add";
	var ACTION_REMOVE = "remove";
	var LIST_NAME_READING_QUEUE = "readingQueue";
	var LIST_NAME_WISHLIST = "wishlist";
	
	function toggleList(element, listName) {
		let elm = $(element);
		let i = elm.find("i");		
		let bookId = elm.attr('data-id');
		let action = "";
		let book = null;
		
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
			toggleReadingQueue(elm, i, data, action)
		} else if (listName == LIST_NAME_WISHLIST) {
			toggleWishlist(elm, i, data, action);
		}
	}
	
	function toggleReadingQueue(elm, i, data, action) {
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
					i.removeClass("color-gray");
					i.addClass("color-green");
				} else {
					elm.removeClass(ACTION_REMOVE);
					elm.addClass(ACTION_ADD);
					i.removeClass("color-green");
					i.addClass("color-gray");
				}
			} else {
				alert(response.data);
			}
		});
	}
	
	function toggleWishlist(elm, i, data, action) {
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
					i.removeClass("color-gray");
					i.addClass("color-red");
				} else {
					elm.removeClass(ACTION_REMOVE);
					elm.addClass(ACTION_ADD);
					i.removeClass("color-red");
					i.addClass("color-gray");
				}
			} else {
				alert(response.data);
			}
		});
	}
	
	function getSelfReview() {
		const bookId = $(".bookId").text();
		
		var request = $.ajax({
			type: "GET",
			url: "/reviews/self",
			dataType: 'JSON',
			data: {
				"bookId": bookId
			}
		});
		
		request.done(function(response) {
			if(response.success) {
				const review = response.data;

				renderSelfReviewSection(review);				
			}
		});
	}
	
	function updateSelfRating(rating) {
		const bookId = $(".bookId").text();
		
		var request = $.ajax({
			type: "PUT",
			url: "/reviews/self/rating",
			dataType: 'JSON',
			data: {
				"bookId": bookId,
				"rating": rating
			}
		});
		
		request.done(function(response) {
			if(response.success) {
				const review = response.data;
				if(review) {
					renderSelfReviewSection(review);
				}
			}
		});
	}
	
	function updateSelfComment(comment) {
		const bookId = $(".bookId").text();
		
		var request = $.ajax({
			type: "PUT",
			url: "/reviews/self/comment",
			dataType: 'JSON',
			data: {
				"bookId": bookId,
				"comment": comment
			}
		});
		
		request.done(function(response) {
			if(response.success) {
				const review = response.data;
				if(review) {
					renderSelfReviewSection(review);
				}
			}
		});
	}
	
	function renderSelfReviewSection(review) {
		let rating = null;
		if(review) {
			rating = review.rating;
			
			$(".self-comment-container").text(review.comment);
			
			const date = new Date(review.modifiedAt);
			const d = date.getDate() + ' ' + getMonthName(date.getMonth()) + ' ' + date.getFullYear();
			const info = "Your review on " + d;
			$(".self-review-info").html(info);
		}
		
		const element = generateRatingElement(rating);
		$(".self-rating-container").html(element);
				
		attachRatingListener();
		
		
	}
	
	function getMonthName(number) {
		const month = {
				1: "Jan", 2: "Feb", 3: "Mar", 4: "Apr",
				5: "May", 6: "Jun", 7: "Jul", 8: "Aug",
				9: "Sep", 10: "Oct", 11: "Nov", 12: "Dec",
		}
		
		return month[number+1];
	}
	
	function generateRatingElement(rating) {
		let container = ""
		for(let i=1; i<=5; i++) {
			let span = "<span class='fa fa-star";
			if(rating > 0 && i <= rating) span += " color-orange";
			span += "' data-sl='" + i + "'></span>";
			container += span;
		}
		return container;
	}
	
	function attachRatingListener() {
		const ratingList = $(".self-rating-container").find("span");
		$(ratingList).each(function(key, val) {
			$(val).click(function(e) {
				e.preventDefault();
				let rating = $(this).data("sl");
				updateSelfRating(rating);
			});
		});
	}
	
	function getAllReviews() {
		const bookId = $(".bookId").text();
		
		var request = $.ajax({
			type: "GET",
			url: "/reviews/other",
			dataType: 'JSON',
			data: {
				"bookId": bookId,
			}
		});
		
		request.done(function(response) {
			console.log(response);
		});
	}
	
	(function() {
		$(".toggle-wishlist").click(function(e) {
			e.preventDefault();
			toggleList(this, LIST_NAME_WISHLIST);
		});
		
		$(".toggle-reading-queue").click(function(e) {
			e.preventDefault();
			toggleList(this, LIST_NAME_READING_QUEUE);
		});
		
		$(".review-edit-btn").click(function(e) {
			e.preventDefault();
			$(this).addClass("hidden");
			$(".review-update-btn").removeClass("hidden");
			$(".self-comment-container").prop("disabled", false);
		});
		
		$(".review-update-btn").click(function(e) {
			e.preventDefault();
			const comment = $(".self-comment-container").val();
			
			updateSelfComment(comment);

			$(this).addClass("hidden");
			$(".review-edit-btn").removeClass("hidden");
			$(".self-comment-container").prop("disabled", true);
		});
		
		getSelfReview();
		getAllReviews();
	})();
})();