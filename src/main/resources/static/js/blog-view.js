var BlogViewManager = (function() {
	
	function updateSelfComment() {
		const comment = $(".self-comment-container").val();
		
		if(!comment) return false;
		
		const blogId = $(".blogId").text();
		const commentId = $(".editableCommentId").text();
		
		var request = $.ajax({
			type: "PUT",
			url: "/blogs/me/comments",
			dataType: 'JSON',
			data: {
				"blogId": blogId,
				"commentId": commentId,
				"comment": comment
			}
		});
		
		request.done(function(response) {
			if(response.success) {
				location.reload();
			}
		});
	}
	
	function getAllComments() {
		const blogId = $(".blogId").text();
		
		var request = $.ajax({
			type: "GET",
			url: "/blogs/all/comments",
			dataType: 'JSON',
			data: {
				"blogId": blogId,
			}
		});
		
		request.done(function(response) {
			renderOtherCommentSection(response);
		});
	}
	
	function renderOtherCommentSection(response) {
		const commentList = response.data;
		if(commentList && commentList.length > 0) {
			
			const isAdmin = response.admin;
			const loggedinUserId = response.loggedinUserId;
			
			commentList.forEach(function(row, index) {
				let template = $("#other-comment-row").html();
				let temp = $(".temp-container");
				temp.html(template);
				
				$(temp).find(".other-user-name").html(row.commentUserName);
				$(temp).find(".other-date").html(row.modifiedAtText);
				$(temp).find(".other-comment").html(row.comment);
				
				commentId = `comment_id_${row.id}`;
				$(temp).find(".other-comment").attr("id", commentId);
				
				const commentUserId = row.commentUserId;
				if(loggedinUserId === commentUserId) {
					$(temp).find(".edit-icon").attr("data-id", row.id);
					$(temp).find(".delete-icon").attr("data-id", row.id);
				} else if(isAdmin) {
					$(temp).find(".edit-icon").remove();
					$(temp).find(".delete-icon").attr("data-id", row.id);
				} else {
					$(temp).find(".edit-icon").remove();
					$(temp).find(".delete-icon").remove();
				}
												
				$(".other-comment-container").append(temp.html());
				temp.empty();
			});
			
			attachEditListener();
			attachDeleteListener();
			
		}
	}
	
	function attachEditListener() {
		$(".edit-icon").click(function(e) {
			e.preventDefault();

			const dataId = $(this).data("id");
			const commentId = `comment_id_${dataId}`;				
			const comment = $(`#${commentId}`).html();
			
			$(".editableCommentId").text(dataId);
			$(".self-comment-container").text(comment);
			
			$(".comment-save-btn").addClass("hidden");
			$(".comment-update-btn").removeClass("hidden");
			
			$(".self-comment-container").focus();
		});
	}
	
	function attachDeleteListener() {
		$(".delete-icon").click(function(e) {
			e.preventDefault();

			const commentId = $(this).data("id");

			var c = confirm("Are you sure you want to delete this comment?");

			if(c) {
				var request = $.ajax({
					type: "DELETE",
					url: `/blogs/me/comments/${commentId}`,
					dataType: 'JSON'
				});

				request.done(function(response) {
					if(response.success) {
						location.reload();
					}
				});
			}

		});
	}
	
	function getYear() {
		let yearPlaceholder = $("#bookcentricYear");
		var year = new Date().getFullYear();
		if (yearPlaceholder) {
			$(yearPlaceholder).text(year);
		}
	}
	
	(function() {
		$(".comment-save-btn").click(function(e) {
			e.preventDefault();			
			
			updateSelfComment();						
		});
		
		$(".comment-update-btn").click(function(e) {
			e.preventDefault();			
			
			updateSelfComment();						
		});
		
		$(".comment-cancel-btn").click(function(e) {
			e.preventDefault();
			
			$(".editableCommentId").text("");
			$(".self-comment-container").text("");
			
			$(".comment-save-btn").removeClass("hidden");
			$(".comment-update-btn").addClass("hidden");
		});
		
		getAllComments();
		getYear();
		
	})();
	
})();