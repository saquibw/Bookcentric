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
						
						let blogHeader = $(temp).find("#blog-item-header");
						blogHeader.text(blog.subject);
						blogHeader.attr("href", "/blogs/all/view/" + blog.id);
						
						let blogImage = $(temp).find("#blog-image");
						blogImage.attr("src", "/blogs/all/image/" + blog.id);
						
						let blogDetails = $(temp).find("#blog-item-details");
						blogDetails.attr("href", "/blogs/all/view/" + blog.id);
						
						$(temp).find("#blog-item-author-name").text(blog.userFullName);
						$(temp).find("#blog-item-creation-date").text(blog.createdAtText);
						
						let editIcon = $(temp).find(".edit-icon");
						if(editIcon) {
							editIcon.attr("href", "/blogs/me/edit/" + blog.id);
						}

						container.append(temp.html());						
						temp.empty();
					});
					
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
	
	(function() {
		getAllBlogs();
		attachListeners();
	})();
})();