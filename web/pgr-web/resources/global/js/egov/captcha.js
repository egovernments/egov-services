"use strict";

var captcha;

function randomString() {
	var length = 6;
	var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	var result ='';
	for (var i = length; i > 0; --i) {
		result += chars[Math.floor(Math.random() * chars.length)];
	}
	return result;
}

captcha = function(canvasfd, imagefd){
	this.canvas = $(canvasfd);
	this.imagefd = $(imagefd);
	this.captchaCode = '';
	this.context=this.canvas[0].getContext("2d");
	this.refreshCaptcha();
}

captcha.prototype.validateCaptcha = function(code){
	return this.captchaCode == code;
}

captcha.prototype.refreshCaptcha = function(){
	this.captchaCode = randomString();
	this.context.clearRect(0 , 0, this.canvas.width(), this.canvas.height());
	this.context.font = '22pt Calibri';
	this.context.textAlign="center";
	this.context.textBaseline="middle";
	this.context.strokeText(this.captchaCode, this.canvas.width()/2, this.canvas.height()/2);
	this.imagefd.attr('src',this.context.canvas.toDataURL());
}

$.Captcha = captcha;