<html>
<head>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Testing Websites</title>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<script type='text/javascript'
		src="js/bootstrap3-typeahead.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
		$( "#accordion" ).accordion({
		      collapsible: true,
		      active:false
		 });
	});
</script>

</head>
<body>
	
	<html>
	
	<html></html>
	</html>
	
  <input id="typeahead-input" type="text" data-provide="typeahead" />


<script type="text/javascript">



function process(search){
	alert('search');
}

$(document).ready(function() {
    $('#typeahead-input').typeahead({
        source: function (query, process) {
            return $.get('getBookViaAjax?query=' + query, function (data) {
                return process(data.search_results);
            });
        }
    });
})
</script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script type='text/javascript' src="js/select2.min.js"></script>

</body>
</html>