<style>
.modal.dialog { /* customized styles. this way you can have N dialogs, each one with its own size styles */
    width: 80%;
    height: 50%;
    left: 20%; /* ( window's width [100%] - dialog's width [60%] ) / 2 */
}

/* media query for mobile devices */
@media ( max-width: 480px ) {
    .modal.dialog1 {
        height: 90%;
        left: 5%; /* ( window's width [100%] - dialog's width [90%] ) / 2 */
        top: 5%;
        width: 90%;
    }
}

/* split the modal in two divs (header and body) with defined heights */
.modal .modal-header {
    height: 10%;
}

.modal .modal-body {
    height: 90%;
    width: 100%;
}

/* The div inside modal-body is the key; there's where we put the content (which may need the vertical scrollbar) */
.modal .modal-body div {
    height: 100%;
    overflow-y: auto;
    width: 100%;
    overflow-x: auto;
}

#panelGuest{
    overflow-y:auto;
    overflow-x:auto;
    width: 100%;
    float:left;
    border-right:solid #ff920a 2px;
    min-height:100px;
}

</style>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">

	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">

			<div class="modal-header" >
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>

			<div class="modal-body" >
					  <div id="mycontent"></div>
			</div>
		</div>

		<div class="modal-footer">
			<button type="submit" class="btn btn-danger btn-default pull-right"
				data-dismiss="modal">
				<span class="glyphicon glyphicon-remove"></span> Cancel
			</button>
		</div>

	</div>
</div>
</div>
