              <!-- Include one of jTable styles. -->
              <link href="/jtable/themes/lightcolor/gray/jtable.min.css" rel="stylesheet" type="text/css" />
              <link href="/jquery-ui/jquery-ui.css" rel="stylesheet" type="text/css" />


    <!-- Include jTable script file. -->
    <script src="/jquery-ui/jquery-ui.js" type="text/javascript"></script>
    <script src="/jtable/jquery.jtable.min.js" type="text/javascript"></script>


<script type="text/javascript">
    $(document).ready(function () {
        $('#PersonTableContainer').jtable({
            title: 'Table of people',
            paging: true, //Enable paging
            pageSize: 10, //Set page size (default: 10)
            sorting: true, //Enable sorting
            defaultSorting: 'Name ASC', //Set default sorting
            
            actions: {
                listAction: '/batchdetailjson?batchid=${batchid}',
                createAction: '/GettingStarted/CreatePerson',
                updateAction: '/GettingStarted/UpdatePerson',
                deleteAction: '/GettingStarted/DeletePerson'
            },
            fields: {


      <#list recs[0]?keys as key>

       
        <#if key?lower_case != "_id" >

          "${key}": {title:"${key}"},
       
         </#if>

       </#list>

          "_id": {title:"ID"}

            }
        });

        
        $('#PersonTableContainer').jtable('load');
        
    });
</script>


