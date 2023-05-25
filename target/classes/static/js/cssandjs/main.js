document.addEventListener("DOMContentLoaded", function(event) {
   
const showNavbar = (toggleId, navId, bodyId, headerId) =>{
const toggle = document.getElementById(toggleId),
nav = document.getElementById(navId),
bodypd = document.getElementById(bodyId),
headerpd = document.getElementById(headerId)

// Validate that all variables exist
if(toggle && nav && bodypd && headerpd){
toggle.addEventListener('click', ()=>{
// show navbar
nav.classList.toggle('show-s')
// change icon
toggle.classList.toggle('bx-x')
// add padding to body
bodypd.classList.toggle('body-pd')
// add padding to header
headerpd.classList.toggle('body-pd')
})
}
}

showNavbar('header-toggle','nav-bar','body-pd','header')

/*===== LINK ACTIVE =====*/
const linkColor = document.querySelectorAll('.nav_link')

function colorLink(){
if(linkColor){
linkColor.forEach(l=> l.classList.remove('active'))
this.classList.add('active')
}
}
linkColor.forEach(l=> l.addEventListener('click', colorLink))

 // Your code to run since DOM is loaded and ready
 
var links = document.querySelectorAll('.nav_link_dynamic');
var currentPath = window.location.pathname;

links.forEach(function(link) {
  if (link.getAttribute('href') === currentPath) {
    link.classList.add('active');
  }
});

});


$(function() {
	$('.accordion').find('.accordion__title').click(function(){
		// Adds active class
		$(this).toggleClass('active');
		// Expand or collapse this panel
		$(this).next().slideToggle('fast');
		// Hide the other panels
		$('.accordion__content').not($(this).next()).slideUp('fast');
		// Removes active class from other titles
		$('.accordion__title').not($(this)).removeClass('active');		
	});
});

