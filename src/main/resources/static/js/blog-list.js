var BlogManager = (function() {
	var isOwnBlogs = false;
	
	function getAllBlogs() {
		var request = $.ajax({
			type: "GET",
			url: "/blogs/all/",
			dataType: 'JSON',
			data: {
				isOwnBlogs: isOwnBlogs
			}
		});
		
		request.done(function(response) {
			if(response.success) {
				let template = $("#blog-item").html();
				let temp = $("#temp-container");
				let container = $(".blog-container");
				container.empty()
				let readMore = "<a class='ml-1' id='blog-item-details' href='#' target='_blank'>Read more >></a>";
				
				if(response.data && response.data.length > 0) {
					$(response.data).each(function(key, blog) {
						temp.html(template);
						
						appendMutatingIcons(blog.own, blog.admin);
						
						$(temp).find("#blog-item-id").text(blog.id);
						
						let blogHeader = $(temp).find("#blog-item-header");
						blogHeader.text(blog.subject);
						blogHeader.attr("href", "/blogs/all/view/" + blog.id);
						
						if(!blog.published) {
							$(temp).find("#blog-item-published").text("Unpublished");
						}
						
						let blogImage = $(temp).find("#blog-image");
						blogImage.attr("src", "/blogs/all/image/" + blog.id);
						
						let blogDetails = $(temp).find("#blog-item-details");
						blogDetails.attr("href", "/blogs/all/view/" + blog.id);
						
						$(temp).find("#blog-item-author-name").text(blog.userFullName);
						$(temp).find("#blog-item-creation-date").text(blog.createdAtText);
						$(temp).find("#blog-item-comment-count").text(blog.commentCount);
						
						let editIcon = $(temp).find(".edit-icon");
						if(editIcon) {
							editIcon.attr("href", "/blogs/me/edit/" + blog.id);
						}

						container.append(temp.html());						
						temp.empty();
					});
					attachDeleteListener();
				};
			}
		});
	}
	
	function appendMutatingIcons(isOwn, isAdmin) {
		let editIcon = "<a class='fa fa-edit edit-icon color-green' target='_blank' href='#'></a>";
		let deleteIcon = "<a class='fa fa-trash delete-icon color-red margin-left-20px' href='#'></a>";
		let container = $("#temp-container").find("#mutatable-item-container");
		
		if(container) {
			if(isOwn) {
				container.append(editIcon);
				container.append(deleteIcon);
			} else if(!isOwn && isAdmin) {
				container.append(deleteIcon);
			}
		}
		
	}
	
	function attachListeners() {
		let ownBlogs = $(".own-blogs");
		let otherBlogs = $(".other-blogs");
		if(ownBlogs) {
			ownBlogs.click(function(e) {
				e.preventDefault();
				if($(this).hasClass("active")) {
					return false;
				}
				$(this).addClass("active");
				otherBlogs.removeClass("active");
				isOwnBlogs = true;
				getAllBlogs();
			});
		}
		
		if(otherBlogs) {
			otherBlogs.click(function(e) {
				e.preventDefault();
				if($(this).hasClass("active")) {
					return false;
				}
				$(this).addClass("active");
				ownBlogs.removeClass("active");
				isOwnBlogs = false;
				getAllBlogs();
			});
		}
	}
	
	function attachDeleteListener() {
		$(".delete-icon").click(function(e) {
			e.preventDefault();
			let blogItem = $(this).closest(".blog-item");
			let id = blogItem.find("#blog-item-id").text();
			
			var c = confirm("Are you sure you want to delete this blog?");
			
			if(c) {
				const blogId = $(".blogId").text();
				
				var request = $.ajax({
					type: "DELETE",
					url: `/blogs/me/${id}`,
					dataType: 'JSON'
				});
				
				request.done(function(response) {
					if(response.success) {
						blogItem.remove();
					}
				});
			}

		});
	}
	
	(function() {
		getAllBlogs();
		attachListeners();
	})();
})();