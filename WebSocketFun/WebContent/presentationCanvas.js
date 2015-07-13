/**
 * 
 */

function initCanvasPresentation() {

	var canvasDiv = window.document.getElementById('canvasDiv');
	canvasPresentation = document.createElement('canvas');
	canvasPresentation.id = "presentationCanvas";
	canvasPresentation.width = INITICANVASWIDTH;
	canvasPresentation.height = INITICANVASHEIGHT;
	canvasPresentation.left = 0;
	canvasPresentation.right = 0;
	canvasPresentation.style.zIndex = -10000;
	canvasDiv.appendChild(canvasPresentation);
	contextPresentation = canvasPresentation.getContext('2d');

	PDFJS.getDocument(presentationURI).then(function(pdf) {

		setUpPresentationFirst(pdf);
		window.currentPage = 0;
		renderPresentationPage(pdf);

	});

}

setUpPresentationFirst = function(pdf) { 
	window.numPresentationPages = pdf.numPages;
	window.presentationPdf = pdf;
	
	$("#menuDiv").append("<button id=\"previousPage\">Previous Page</button>");
	$("#previousPage").css({"height":"100%"});
	$("#previousPage").click(goPreviousPage);
	
	$("#menuDiv").append("<button id=\"nextPage\">Next Page</button>");
	$("#nextPage").css({"height":"100%"});
	$("#nextPage").click(goNextPage);
}

renderPresentationPage = function(pdf) {

	if (pdf != null && typeof pdf != 'undefined' && pdf != undefined) {
		
		pdf.getPage(currentPage + 1).then(function(page) {

			var desiredWidth = CURRENTCANVASWIDTH;
			var viewport = page.getViewport(1);
			var scale = desiredWidth / viewport.width;
			var scaledViewport = page.getViewport(scale);

			var renderContext = {
				canvasContext : contextPresentation,
				viewport : scaledViewport
			};
			page.render(renderContext);
		});
	}
}

goPreviousPage = function() {
	if(currentPage > 0){
		currentPage--;
		redrawCurrentPageContents();
		var mes = {
				command : "changePage",
				pageNum : currentPage
			};
		Chat.socket.send(JSON.stringify(mes));
	}
}

goNextPage = function() {
	if(currentPage < numPresentationPages-1){
		currentPage++;
		redrawCurrentPageContents();
		var mes = {
				command : "changePage",
				pageNum : currentPage
			};
		Chat.socket.send(JSON.stringify(mes));
	}
}

changePage = function(pageNum) {
	if(pageNum < numPresentationPages && pageNum >= 0){
		currentPage = pageNum;
		redrawCurrentPageContents();
	}
}

