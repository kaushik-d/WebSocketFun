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
		// Using promise to fetch the page
		window.numPresentationPages = pdf.numPages();
		
		for(var i = 0; i < numPresentationPages; i++) {
			window.savedDrawCommands[i.toString()] = new Array(); 
		}
	});
	window.currentPage = 1;
	renderPresentationPage();
}

renderPresentationPage = function() {
	
	PDFJS.getDocument(presentationURI).then(function(pdf) {
		// Using promise to fetch the page
		
		pdf.getPage(currentPage+1).then(function(page) {

			var desiredWidth = CURRENTCANVASWIDTH;
			var viewport = page.getViewport(1);
			var scale = desiredWidth / viewport.width;
			var scaledViewport = page.getViewport(scale);

			//
			// Render PDF page into canvas context
			//
			var renderContext = {
				canvasContext : contextPresentation,
				viewport : scaledViewport
			};
			page.render(renderContext);
		});
	});
};