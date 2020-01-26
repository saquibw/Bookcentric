var BlogViewManager = (function() {
	
	function updateSelfComment(comment) {
		const blogId = $(".blogId").text();
		
		var request = $.ajax({
			type: "PUT",
			url: "/blogs/me/comments",
			dataType: 'JSON',
			data: {
				"blogId": blogId,
				"comment": comment
			}
		});
		
		request.done(function(response) {
			if(response.success) {
				const comment = response.data;
				if(comment) {
					renderSelfCommentSection(comment);
				}
			}
		});
	}
	
	function renderSelfCommentSection(blogComment) {
		if(blogComment) {
			
			const comment = blogComment.comment;
			if(comment) {
				$(".self-comment-container").text(comment);
				$(".self-comment-container").prop("disabled", true);
				$(".comment-edit-btn").removeClass("hidden");
				$(".comment-save-btn").addClass("hidden");
				$(".comment-delete-btn").removeClass("hidden");
			}
		}
	}
	
	function getSelfComment() {
		const blogId = $(".blogId").text();
		
		var request = $.ajax({
			type: "GET",
			url: "/blogs/me/comments",
			dataType: 'JSON',
			data: {
				"blogId": blogId
			}
		});
		
		request.done(function(response) {
			if(response.success) {
				const comment = response.data;

				renderSelfCommentSection(comment);				
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
			renderOtherCommentSection(response.data);
		});
	}
	
	function renderOtherCommentSection(commentList) {
		if(commentList && commentList.length > 0) {
			
			commentList.forEach(function(row, index) {
				let template = $("#other-comment-row").html();
				let temp = $(".temp-container");
				temp.html(template);
				
				$(temp).find(".other-user-name").html(row.userName);
				$(temp).find(".other-date").html(row.modifiedAtText);
				$(temp).find(".other-comment").html(row.comment);
								
				$(".other-comment-container").append(temp.html());
			});
			
		}
	}
	
	(function() {
		$(".comment-save-btn").click(function(e) {
			e.preventDefault();
			const text = $(".self-comment-container").val();
			
			if(text) {
				updateSelfComment(text);
			}			
		});
		
		$(".comment-edit-btn").click(function(e) {
			e.preventDefault();
			$(this).addClass("hidden");
			$(".comment-save-btn").removeClass("hidden");
			$(".self-comment-container").prop("disabled", false);
		});
		
		$(".comment-delete-btn").click(function(e) {
			e.preventDefault();
			
			var c = confirm("Are you sure you want to delete this comment?");
			
			if(c) {
				const blogId = $(".blogId").text();
				
				var request = $.ajax({
					type: "DELETE",
					url: `/blogs/me/comments/${blogId}`,
					dataType: 'JSON'
				});
				
				request.done(function(response) {
					if(response.success) {
						location.reload();
					}
				});
			}			
		});
		
		getSelfComment();
		getAllComments();
		
	})();
	
})();