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
			console.log(response);
			if(response.success) {
				const review = response.data;
				if(review) {
					renderSelfReviewSection(review);
					getAllReviews();
				}
			}
		});
	}
	
	function renderSelfReviewSection(review) {
		let rating = null;
		if(review) {
			rating = review.rating;
			
			const comment = review.comment;
			if(comment) {
				$(".self-comment-container").text(review.comment);
				$(".self-comment-container").prop("disabled", true);
				$(".review-edit-btn").removeClass("hidden");
				$(".review-save-btn").addClass("hidden");
			}
			
			if(comment || review.rating) {
				$(".review-delete-btn").removeClass("hidden");
			}
						
			const date = getFormattedDate(review.modifiedAt)
			const info = "Your review on " + date;
			$(".self-review-info").html(info);
		}
		
		const element = generateRatingElement(rating);
		$(".self-rating-container").html(element);
				
		attachRatingListener();		
	}
	
	function getFormattedDate(date) {
		const dateObj = new Date(date);
		const d = dateObj.getDate() + ' ' + getMonthName(dateObj.getMonth()) + ' ' + dateObj.getFullYear();
		
		return d;
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
			renderRatingCountBlock(response.data);
			renderOtherReviewSection(response.data);
		});
	}
	
	function renderRatingCountBlock(reviewList) {
		let totalRating = 0;
		let average = null;
		let totalRatingCount = 0;
		let totalCommentCount = 0;
		let ratingText = "rating";
		let commentText = "comment";

		if(reviewList && reviewList.length > 0) {
			reviewList.forEach(function(row, index) {
				if(row.rating && row.rating > 0) {
					totalRating += row.rating;
					totalRatingCount++;
					if(totalRatingCount > 1) ratingText = "ratings";
				}
				
				if(row.comment) {
					totalCommentCount++;
					if(totalCommentCount > 1) commentText = "comments";
				}
			});
							
			if(totalRating > 0) {
				average = Math.round(totalRating / reviewList.length);
				
			}
		}
		const totalReviewCountText = totalRatingCount + " " + ratingText + " / " + totalCommentCount + " " + commentText;
		$(".other-rating-container").html(generateRatingElement(average));
		$(".total-review-count").html(totalReviewCountText);
	}
	
	function renderOtherReviewSection(reviewList) {
		if(reviewList && reviewList.length > 0) {
			$(".other-review-container").empty();
			
			reviewList.forEach(function(row, index) {
				let template = $("#other-review-row").html();
				let temp = $(".temp-container");
				temp.html(template);
				
				$(temp).find(".other-user-name").html(row.userFullName);
				$(temp).find(".other-date").html(getFormattedDate(row.modifiedAt));
				$(temp).find(".other-rating").html(generateRatingElement(row.rating));
				$(temp).find(".other-comment").html(row.comment);
								
				$(".other-review-container").append(temp.html());
			});			
		}
	}
	
	function getYear() {
		let yearPlaceholder = $("#bookcentricYear");
		var year = new Date().getFullYear();
		if (yearPlaceholder) {
			$(yearPlaceholder).text(year);
		}
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
			$(".review-save-btn").removeClass("hidden");
			$(".self-comment-container").prop("disabled", false);
		});
		
		$(".review-save-btn").click(function(e) {
			e.preventDefault();
			const comment = $(".self-comment-container").val();
			
			if(comment) {
				updateSelfComment(comment);

				$(this).addClass("hidden");
				$(".review-edit-btn").removeClass("hidden");
				$(".self-comment-container").prop("disabled", true);
			}			
		});
		
		$(".review-delete-btn").click(function(e) {
			e.preventDefault();
			
			var c = confirm("Are you sure you want to delete this review?");
			
			if(c) {
				const bookId = $(".bookId").text();
				
				var request = $.ajax({
					type: "DELETE",
					url: `/reviews/self/${bookId}`,
					dataType: 'JSON'
				});
				
				request.done(function(response) {
					if(response.success) {
						location.reload();
					}
				});
			}			
		});
		
		getSelfReview();
		getAllReviews();
		getYear();
	})();
})();