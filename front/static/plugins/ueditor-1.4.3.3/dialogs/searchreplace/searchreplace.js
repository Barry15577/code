
//Clear the traces of the last selection
editor.firstForSR = 0;
editor.currentRangeForSR = null;
//Register switching events for tab
/**
* Tab click event handler
* @param tabHeads
* @param tabBodys
* @param obj
*/
function clickHandler( tabHeads,tabBodys,obj ) {
//head style change
for ( var k = 0, len = tabHeads.length; k < len; k++ ) {
tabHeads[k].className = "";
}
obj.className = "focus";
//body visibility
    var tabSrc = obj.getAttribute( "tabSrc" );
    for ( var j = 0, length = tabBodys.length; j < length; j++ ) {
        var body = tabBodys[j],
            id = body.getAttribute( "id" );
        if ( id != tabSrc ) {
            body.style.zIndex = 1;
        } else {
            body.style.zIndex = 200;
        }
    }

}

/**
* TAB switching
* @param tabParentId The parent node ID of the tab or the object itself
*/
function switchTab( tabParentId ) {
    var tabElements = $G( tabParentId ).children,
        tabHeads = tabElements[0].children,
        tabBodys = tabElements[1].children;

    for ( var i = 0, length = tabHeads.length; i < length; i++ ) {
        var head = tabHeads[i];
        if ( head.className === "focus" )clickHandler(tabHeads,tabBodys, head );
        head.onclick = function () {
            clickHandler(tabHeads,tabBodys,this);
        }
    }
}
$G('searchtab').onmousedown = function(){
    $G('search-msg').innerHTML = '';
    $G('replace-msg').innerHTML = ''
}
//Is it case-sensitive?
function getMatchCase(id) {
return $G(id).checked ? true : false;
}
//Search
$G("nextFindBtn").onclick = function (txt, dir, mcase) {
    var findtxt = $G("findtxt").value, obj;
    if (!findtxt) {
        return false;
    }
    obj = {
        searchStr:findtxt,
        dir:1,
        casesensitive:getMatchCase("matchCase")
    };
    if (!frCommond(obj)) {
        var bk = editor.selection.getRange().createBookmark();
        $G('search-msg').innerHTML = lang.getEnd;
        editor.selection.getRange().moveToBookmark(bk).select();


    }
};
$G("nextReplaceBtn").onclick = function (txt, dir, mcase) {
    var findtxt = $G("findtxt1").value, obj;
    if (!findtxt) {
        return false;
    }
    obj = {
        searchStr:findtxt,
        dir:1,
        casesensitive:getMatchCase("matchCase1")
    };
    frCommond(obj);
};
$G("preFindBtn").onclick = function (txt, dir, mcase) {
    var findtxt = $G("findtxt").value, obj;
    if (!findtxt) {
        return false;
    }
    obj = {
        searchStr:findtxt,
        dir:-1,
        casesensitive:getMatchCase("matchCase")
    };
    if (!frCommond(obj)) {
        $G('search-msg').innerHTML = lang.getStart;
    }
};
$G("preReplaceBtn").onclick = function (txt, dir, mcase) {
    var findtxt = $G("findtxt1").value, obj;
    if (!findtxt) {
        return false;
    }
    obj = {
        searchStr:findtxt,
        dir:-1,
        casesensitive:getMatchCase("matchCase1")
    };
    frCommond(obj);
};
//replace
$G("repalceBtn").onclick = function () {
    var findtxt = $G("findtxt1").value.replace(/^\s|\s$/g, ""), obj,
        replacetxt = $G("replacetxt").value.replace(/^\s|\s$/g, "");
    if (!findtxt) {
        return false;
    }
    if (findtxt == replacetxt || (!getMatchCase("matchCase1") && findtxt.toLowerCase() == replacetxt.toLowerCase())) {
        return false;
    }
    obj = {
        searchStr:findtxt,
        dir:1,
        casesensitive:getMatchCase("matchCase1"),
        replaceStr:replacetxt
    };
    frCommond(obj);
};
//Replace all
$G("repalceAllBtn").onclick = function () {
    var findtxt = $G("findtxt1").value.replace(/^\s|\s$/g, ""), obj,
        replacetxt = $G("replacetxt").value.replace(/^\s|\s$/g, "");
    if (!findtxt) {
        return false;
    }
    if (findtxt == replacetxt || (!getMatchCase("matchCase1") && findtxt.toLowerCase() == replacetxt.toLowerCase())) {
        return false;
    }
    obj = {
        searchStr:findtxt,
        casesensitive:getMatchCase("matchCase1"),
        replaceStr:replacetxt,
        all:true
    };
    var num = frCommond(obj);
    if (num) {
        $G('replace-msg').innerHTML = lang.countMsg.replace("{#count}", num);
    }
};
//implement
var frCommond = function (obj) {
    return editor.execCommand("searchreplace", obj);
};
switchTab("searchtab");
