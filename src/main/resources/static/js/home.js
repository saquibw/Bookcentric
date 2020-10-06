var HomeManager = (function() {
	
	var TYPE_NEW_ARRIVAL = "newArrival";
	
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
				if(response.data && response.data.length > 0) {
					const container = $(".newBookContainer");
					
					const tempContainer = $("#temp-container");
					const tempBookContainer = $("#temp-book-container");
					
					const bookItemContainer = $("#newBookItemContainerScript").html();
					const bookItemTemplate = $("#newBookItem").html();
										
					tempContainer.html(bookItemContainer);
					tempBookContainer.html(bookItemTemplate);
										
					for(let i=0; i<response.data.length; i++) {
						tempBookContainer.find("img").attr("src", "/get/image/" + response.data[i].id);
						tempBookContainer.find("a").attr("href", "/book/get/" + response.data[i].id);
						tempContainer.find(".newBookItemContainer").append(tempBookContainer.html());
						
						if((i+1)%4 == 0) {
							container.append(tempContainer.html());
							tempContainer.empty();
							tempContainer.html(bookItemContainer);
						} else if((i+1) == response.data.length) {
							container.append(tempContainer.html());
							tempContainer.empty();
							tempBookContainer.empty();
						}
					}					
				}
				initPageSliders();
			}

		});
	}
	
	function getTestimonials() {
		var request = $.ajax({
			type: "GET",
			url: "/open/testimonials",
			dataType: 'JSON'
		});
		
		request.done(function(response) {
			if(response.success) {
				if(response.data && response.data.length > 0) {
					const container = $(".testimonialContainer");
					const tempContainer = $("#temp-testimonial-container");
					const testimonialItemContainer = $("#testimonialItemContainerScript").html();
					
					tempContainer.html(testimonialItemContainer);
					
					for(let i=0; i<response.data.length; i++) {
						const testimonial = response.data[i];
						tempContainer.find("#testimonialDescription").text(testimonial.description);
						tempContainer.find(".testimonial-author").text(testimonial.authorName);
						
						container.append(tempContainer.html());
					}
					tempContainer.empty();
				}
			}
		});
	}
	
	function initPageSliders(){
	    (function($){
	        "use strict";
	        
	        // Fullwidth slider
	        $(".fullwidth-slider").owlCarousel({
	            slideSpeed: 350,
	            singleItem: true,
	            autoHeight: true,
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });
	        
	        // Fullwidth slider
	        $(".fullwidth-slider-fade").owlCarousel({
	            transitionStyle: "fadeUp",
	            slideSpeed: 350,
	            singleItem: true,
	            autoHeight: true,
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });
	        
	        // Fullwidth gallery
	        $(".fullwidth-gallery").owlCarousel({
	            transitionStyle: "fade",
	            autoPlay: 5000,
	            slideSpeed: 700,
	            singleItem: true,
	            autoHeight: true,
	            navigation: false,
	            pagination: false
	        });
	        
	        // Item carousel
	        $(".item-carousel").owlCarousel({
	            autoPlay: 2500,
	            //stopOnHover: true,
	            items: 3,
	            itemsDesktop: [1199, 3],
	            itemsTabletSmall: [768, 3],
	            itemsMobile: [480, 1],
	            navigation: false,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });
	        
	        // Item carousel
	        $(".small-item-carousel").owlCarousel({
	            autoPlay: 2500,
	            stopOnHover: true,
	            items: 6,
	            itemsDesktop: [1199, 4],
	            itemsTabletSmall: [768, 3],
	            itemsMobile: [480, 2],
	            pagination: false,
	            navigation: false,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });
	        
	        // Single carousel
	        $(".single-carousel").owlCarousel({
	            singleItem: true,
	            autoHeight: true,
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });
	        
	        // Content Slider
	        $(".content-slider").owlCarousel({
	            slideSpeed: 350,
	            singleItem: true,
	            autoHeight: true,
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });

	        // Photo slider
	        $(".photo-slider").owlCarousel({
	            slideSpeed: 350,
	            items: 4,
	            itemsDesktop: [1199, 4],
	            itemsTabletSmall: [768, 2],
	            itemsMobile: [480, 1],
	            autoHeight: true,
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        }); 
	        
	        // Work slider
	        $(".work-full-slider").owlCarousel({
	            slideSpeed : 350,
	            singleItem: true,
	            autoHeight: true,
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });
	        
	        // Blog posts carousel
	        $(".blog-posts-carousel").owlCarousel({
	            autoPlay: 5000,
	            stopOnHover: true,
	            items: 3,
	            itemsDesktop: [1199, 3],
	            itemsTabletSmall: [768, 2],
	            itemsMobile: [480, 1],
	            pagination: false,
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });
	        
	        // Blog posts carousel alt
	        $(".blog-posts-carousel-alt").owlCarousel({
	            autoPlay: 3500,
	            stopOnHover: true,
	            slideSpeed: 350,
	            singleItem: true,
	            autoHeight: true,
	            pagination: false,
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });
	        
	        // Image carousel
	        $(".image-carousel").owlCarousel({
	            autoPlay: 5000,
	            stopOnHover: true,
	            items: 4,
	            itemsDesktop: [1199, 3],
	            itemsTabletSmall: [768, 2],
	            itemsMobile: [480, 1],
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"]
	        });
	        
	        // Fullwidth slideshow
	        
	          var sync1 = $(".fullwidth-slideshow");
	          var sync2 = $(".fullwidth-slideshow-pager");
	  
	        $(".fullwidth-slideshow").owlCarousel({
	            autoPlay: 5000,
	            stopOnHover: true,
	            transitionStyle: "fade",
	            slideSpeed: 350,
	            singleItem: true,
	            autoHeight: true,
	            pagination: false,
	            navigation: true,
	            navigationText: ["<i class='fa fa-angle-left'></i>", "<i class='fa fa-angle-right'></i>"],
	            afterAction : syncPosition,
	            responsiveRefreshRate : 200
	        });
	        $(".fullwidth-slideshow-pager").owlCarousel({
	            autoPlay: 5000,
	            stopOnHover: true,
	            items: 8,
	            itemsDesktop: [1199,8],
	            itemsDesktopSmall: [979,7],
	            itemsTablet: [768,6],
	            itemsMobile: [480,4],
	            autoHeight: true,
	            pagination: false,
	            navigation: false,
	            responsiveRefreshRate : 100,
	            afterInit : function(el){
	              el.find(".owl-item").eq(0).addClass("synced");
	            }
	        });
	        
	        function syncPosition(el){
	            var current = this.currentItem;
	            $(".fullwidth-slideshow-pager").find(".owl-item").removeClass("synced").eq(current).addClass("synced")
	            if ($(".fullwidth-slideshow-pager").data("owlCarousel") !== undefined) {
	                center(current)
	            }
	        }
	        
	        $(".fullwidth-slideshow-pager").on("click", ".owl-item", function(e){
	            e.preventDefault();
	            var number = $(this).data("owlItem");
	            sync1.trigger("owl.goTo", number);
	        });
	 
	        function center(number){
	            var sync2visible = sync2.data("owlCarousel").owl.visibleItems;
	            var num = number;
	            var found = false;
	            for (var i in sync2visible) {
	                if (num === sync2visible[i]) {
	                    var found = true;
	                }
	            }
	            if (found === false) {
	                if (num > sync2visible[sync2visible.length - 1]) {
	                    sync2.trigger("owl.goTo", num - sync2visible.length + 2)
	                }
	                else {
	                    if (num - 1 === -1) {
	                        num = 0;
	                    }
	                    sync2.trigger("owl.goTo", num);
	                }
	            }
	            else 
	                if (num === sync2visible[sync2visible.length - 1]) {
	                    sync2.trigger("owl.goTo", sync2visible[1])
	                }
	                else 
	                    if (num === sync2visible[0]) {
	                        sync2.trigger("owl.goTo", num - 1)
	                    }
	        }
	          
	        var owl = $(".fullwidth-slideshow").data("owlCarousel");
	        
	        /*$(document.documentElement).keyup(function(event){
	            // handle cursor keys
	            if (event.keyCode == 37) {
	                owl.prev();
	            }
	            else 
	                if (event.keyCode == 39) {
	                    owl.next();
	                }
	        });*/
	        
	        if ($(".owl-carousel").length) {
	            var owl = $(".owl-carousel").data('owlCarousel');
	            owl.reinit();
	        }

	    })(jQuery);
	};
	
	function getYear() {
		let yearPlaceholder = $("#bookcentricYear");
		var year = new Date().getFullYear();
		if(yearPlaceholder) {
			$(yearPlaceholder).text(year);
		}		
	}
	
	(function() {
		getTestimonials();
		getSpecialBooks(TYPE_NEW_ARRIVAL);
		getYear();
	})();
})();