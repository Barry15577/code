/*
* Image conversion dialog script
**/

var tableData = [],
//Editor page table
editorTable = null,
chartsConfig = window.typeConfig,
resizeTimer = null,
//Initial default chart type
currentChartType = 0;

window.onload = function () {

editorTable = domUtils.findParentByTagName( editor.selection.getRange().startContainer, 'table', true);

//Table not found, display error page
if ( !editorTable ) {
document.body.innerHTML = "<div class='edui-charts-not-data'>Data not found</div>";
return;
}

//Initialize chart type selection
    initChartsTypeView();
    renderTable( editorTable );
    initEvent();
    initUserConfig( editorTable.getAttribute( "data-chart" ) );
    $( "#scrollBed .view-box:eq("+ currentChartType +")" ).trigger( "click" );
    updateViewType( currentChartType );

    dialog.addListener( "resize", function () {

        if ( resizeTimer != null ) {
            window.clearTimeout( resizeTimer );
        }

        resizeTimer = window.setTimeout( function () {

            resizeTimer = null;

            renderCharts();

        }, 500 );

    } );

};

function initChartsTypeView () {

    var contents = [];

    for ( var i = 0, len = chartsConfig.length; i<len; i++ ) {

        contents.push( '<div class="view-box" data-chart-type="'+ i +'"><img width="300" src="images/charts'+ i +'.png"></div>' );

    }

    $( "#scrollBed" ).html( contents.join( "" ) );

}

// Render the table so that users can edit data
function renderTable ( table ) {

var tableHtml = [];

// Construct data
    for ( var i = 0, row; row = table.rows[ i ]; i++ ) {

        tableData[ i ] = [];
        tableHtml[ i ] = [];

        for ( var j = 0, cell; cell = row.cells[ j ]; j++ ) {

            var value = getCellValue( cell );

            if ( i > 0 && j > 0 ) {
                value = +value;
            }

            if ( i === 0 || j === 0 ) {
                tableHtml[ i ].push( '<th>'+ value +'</th>' );
            } else {
                tableHtml[ i ].push( '<td><input type="text" class="data-item" value="'+ value +'"></td>' );
            }

            tableData[ i ][ j ] = value;

        }

        tableHtml[ i ] = tableHtml[ i ].join( "" );

    }

    //draw table
$( "#tableContainer" ).html( '<table id="showTable" border="1"><tbody><tr>'+ tableHtml.join( "</tr><tr>" ) +'</tr></tbody></table>' );

}

/*
* Initialize the current chart properties according to the existing chart properties of the table
*/
function initUserConfig ( config ) {

    var parsedConfig = {};

    if ( !config ) {
        return;
    }

    config = config.split( ";" );

    $.each( config, function ( index, item ) {

        item = item.split( ":" );
        parsedConfig[ item[ 0 ] ] = item[ 1 ];

    } );

    setUserConfig( parsedConfig );

}

function initEvent () {

    var cacheValue = null,
       //Number of chart types
        typeViewCount = chartsConfig.length- 1,
        $chartsTypeViewBox = $( '#scrollBed .view-box' );

    $( ".charts-format" ).delegate( ".format-ctrl", "change", function () {

        renderCharts();

    } )

    $( ".table-view" ).delegate( ".data-item", "focus", function () {

        cacheValue = this.value;

    } ).delegate( ".data-item", "blur", function () {

        if ( this.value !== cacheValue ) {
            renderCharts();
        }

        cacheValue = null;

    } );

    $( "#buttonContainer" ).delegate( "a", "click", function (e) {

        e.preventDefault();

        if ( this.getAttribute( "data-title" ) === 'prev' ) {

            if ( currentChartType > 0 ) {
                currentChartType--;
                updateViewType( currentChartType );
            }

        } else {

            if ( currentChartType < typeViewCount ) {
                currentChartType++;
                updateViewType( currentChartType );
            }

        }

    } );

    //Chart type changes
$( '#scrollBed' ).delegate( ".view-box", "click", function (e) {

var index = $( this ).attr( "data-chart-type" );
$chartsTypeViewBox.removeClass( "selected" );
$( $chartsTypeViewBox[ index ] ).addClass( "selected" );

currentChartType = index | 0;

//Pie chart, disable partial configuration
if ( currentChartType === chartsConfig.length - 1 ) {

disableNotPieConfig();

//Enable full configuration
        } else {

            enableNotPieConfig();

        }

        renderCharts();

    } );

}

function renderCharts () {

    var data = collectData();

    $('#chartsContainer').highcharts( $.extend( {}, chartsConfig[ currentChartType ], {

        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        title: {
            text: data.title,
            x: -20 //center
        },
        subtitle: {
            text: data.subTitle,
            x: -20
        },
        xAxis: {
            title: {
                text: data.xTitle
            },
            categories: data.categories
        },
        yAxis: {
            title: {
                text: data.yTitle
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            enabled: true,
            valueSuffix: data.suffix
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 1
        },
        series: data.series

    } ));

}

function updateViewType ( index ) {

    $( "#scrollBed" ).css( 'marginLeft', -index*324+'px' );

}

function collectData () {

    var form = document.forms[ 'data-form' ],
        data = null;

    if ( currentChartType !== chartsConfig.length - 1 ) {

        data = getSeriesAndCategories();
        $.extend( data, getUserConfig() );

    //Pie chart data format
} else {
data = getSeriesForPieChart();
data.title = form[ 'title' ].value;
data.suffix = form[ 'unit' ].value;
}

return data;

}

/**
* Get user configuration information
*/
function getUserConfig () {

    var form = document.forms[ 'data-form' ],
        info = {
            title: form[ 'title' ].value,
            subTitle: form[ 'sub-title' ].value,
            xTitle: form[ 'x-title' ].value,
            yTitle: form[ 'y-title' ].value,
            suffix: form[ 'unit' ].value,
           //Data alignment
tableDataFormat: getTableDataFormat (),
//Pie chart prompt text
            tip: $( "#tipInput" ).val()
        };

    return info;

}

function setUserConfig ( config ) {

    var form = document.forms[ 'data-form' ];

    config.title && ( form[ 'title' ].value = config.title );
    config.subTitle && ( form[ 'sub-title' ].value = config.subTitle );
    config.xTitle && ( form[ 'x-title' ].value = config.xTitle );
    config.yTitle && ( form[ 'y-title' ].value = config.yTitle );
    config.suffix && ( form[ 'unit' ].value = config.suffix );
    config.dataFormat == "-1" && ( form[ 'charts-format' ][ 1 ].checked = true );
    config.tip && ( form[ 'tip' ].value = config.tip );
    currentChartType = config.chartType || 0;

}

function getSeriesAndCategories () {

    var form = document.forms[ 'data-form' ],
        series = [],
        categories = [],
        tmp = [],
        tableData = getTableData();

    //Reverse data
    if ( getTableDataFormat() === "-1" ) {

        for ( var i = 0, len = tableData.length; i < len; i++ ) {

            for ( var j = 0, jlen = tableData[ i ].length; j < jlen; j++ ) {

                if ( !tmp[ j ] ) {
                    tmp[ j ] = [];
                }

                tmp[ j ][ i ] = tableData[ i ][ j ];

            }

        }

        tableData = tmp;

    }

    categories = tableData[0].slice( 1 );

    for ( var i = 1, data; data = tableData[ i ]; i++ ) {

        series.push( {
            name: data[ 0 ],
            data: data.slice( 1 )
        } );

    }

    return {
        series: series,
        categories: categories
    };

}

/*
* Get the data source data alignment
*/
function getTableDataFormat () {

var form = document.forms[ 'data-form' ],
items = form['charts-format'];

return items[ 0 ].checked ? items[ 0 ].value : items[ 1 ].value;

}

/*
* Disable non-pie chart type configuration items
*/
function disableNotPieConfig() {

    updateConfigItem( 'disable' );

}

/*
* Enable non-pie chart type configuration items
*/
function enableNotPieConfig() {

updateConfigItem( 'enable' );

}

function updateConfigItem ( value ) {

var table = $( "#showTable" )[ 0 ],
isDisable = value === 'disable' ? true : false;

//Input processing in table
    for ( var i = 2 , row; row = table.rows[ i ]; i++ ) {

        for ( var j = 1, cell; cell = row.cells[ j ]; j++ ) {

            $( "input", cell ).attr( "disabled", isDisable );

        }

    }

    //Other item processing
$( "input.not-pie-item" ).attr( "disabled", isDisable );
$( "#tipInput" ).attr( "disabled", !isDisable )

}

/*
* Get pie chart data
* Only the first row of pie chart data is taken
**/
function getSeriesForPieChart () {

    var series = {
            type: 'pie',
            name: $("#tipInput").val(),
            data: []
        },
        tableData = getTableData();


    for ( var j = 1, jlen = tableData[ 0 ].length; j < jlen; j++ ) {

        var title = tableData[ 0 ][ j ],
            val = tableData[ 1 ][ j ];

        series.data.push( [ title, val ] );

    }

    return {
        series: [ series ]
    };

}

function getTableData () {

    var table = document.getElementById( "showTable" ),
        xCount = table.rows[0].cells.length - 1,
        values = getTableInputValue();

    for ( var i = 0, value; value = values[ i ]; i++ ) {

        tableData[ Math.floor( i / xCount ) + 1 ][ i % xCount + 1 ] = values[ i ];

    }

    return tableData;

}

function getTableInputValue () {

    var table = document.getElementById( "showTable" ),
        inputs = table.getElementsByTagName( "input" ),
        values = [];

    for ( var i = 0, input; input = inputs[ i ]; i++ ) {
        values.push( input.value | 0 );
    }

    return values;

}

function getCellValue ( cell ) {

    var value = utils.trim( ( cell.innerText || cell.textContent || '' ) );

    return value.replace( new RegExp( UE.dom.domUtils.fillChar, 'g' ), '' ).replace( /^\s+|\s+$/g, '' );

}


//dialog confirmation event
dialog.onok = function () {

//Collect information
var form = document.forms[ 'data-form' ],
info = getUserConfig();

//Add chart type
info.chartType = currentChartType;

//Synchronize table data to editor
syncTableData();

//Execute chart command
editor.execCommand( 'charts', info );

};

/*
* Synchronize the table data of the chart editing view to the original table in the editor
*/
function syncTableData () {

    var tableData = getTableData();

    for ( var i = 1, row; row = editorTable.rows[ i ]; i++ ) {

        for ( var j = 1, cell; cell = row.cells[ j ]; j++ ) {

            cell.innerHTML = tableData[ i ] [ j ];

        }

    }

}
