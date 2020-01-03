var CaptchaManager = (function() {
	
	var captchaRand1, captchaRand2, rightAnswer;
	
	function init() {
		captchaRand1 = Math.floor(Math.random()*10);
		captchaRand2 = Math.floor(Math.random()*10);
		rightAnswer = captchaRand1 + captchaRand2;
		
		$("#captchaValue").text("What is " + captchaRand1 + " + " + captchaRand2 + " ?");
	}
	
	function initListener() {
		$(document).find("form").submit(function(e) {			
			let userAnswer = $("#captchaAnswer").val();
			
			if(userAnswer != rightAnswer) {
				e.preventDefault();
				$("#captchaError").text("Wrong value entered!");
				init();
			} 
		});
	}
	
	(function() {
		init();
		initListener();
	})();
})();