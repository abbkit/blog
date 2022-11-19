// lodash.js
// jquery-3.4.1.js
// jquery.toast.js
// jquery-confirm.js
// js.cookie.js

if(! 'ainobug' in window){
    window.ainobug={};
}
(function(ainobug){

    String.prototype.endWith=function(str){
        return _.endsWith(this,str);
    }

    String.prototype.startWith=function(str){
        return _.startsWith(this,str);
    }

    $.fn.getUrlParam = function (name) {
        let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        let r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURI(r[2]); return null;
    }

    function List() {
        this.arrys = [];
        this.position = -1;

        List.prototype.equals= function($_this, $_another) {
            if ($_this.equals && $.isFunction($_this.equals)) {
                return $_this.equals.apply($_this, [ $_another ]);
            } else {
                if ($.type($_this) === 'string' || $.type($_this) === 'boolean'
                    || $.type($_this) === 'number' || $.type($_this) === 'date') {
                    return $_this == $_another;
                } else {
                    for ( let i in $_this) {
                        equals($_this[i], [ $_another[i] ]);
                    }
                }
            }
        }

        List.prototype.size = function() {
            return this.position + 1;
        };

        List.prototype.length = function() {
            return this.arrys.length;
        };

        List.prototype.resize = function() {
            let tempArr = [ this.size() * 1.25 + 5 ];
            for (let i = 0; i < this.size(); i++) {
                tempArr[i] = this.arrys[i];
            }
            this.arrys = tempArr;
        };

        List.prototype.exists = function(id) {
            let exists = false;
            let i = 0;
            for (; i < this.size(); i++) {
                if (equals(this.arrys[i], id)) {
                    exists = true;
                    break;
                }
            }
            return exists;
        };

        List.prototype.indexOf = function(id) {
            let exists = false;
            let i = 0;
            for (; i < this.size(); i++) {
                if (equals(this.arrys[i], id)) {
                    exists = true;
                    break;
                }
            }
            return exists ? i : -1;
        };

        List.prototype.add = function(id) {
            if (this.size() == this.length()) {
                this.resize();
            }
            this.position++;
            this.arrys[this.position] = id;
        };

        List.prototype.compress = function() {
            let tempPosition = -1;
            for (let i = 0; i <= this.position; i++) {
                if (this.get(i) == null) {
                    let j = i + 1;
                    while (j <= this.position) {
                        if (this.get(j) != null) {
                            this.arrys[i] = this.get(j);
                            this.arrys[j] = null;
                            tempPosition = i;
                            break;
                        } else {
                            j++;
                        }
                    }
                } else {
                    tempPosition = i;
                }
            }
            this.position = tempPosition;
        };

        List.prototype.get = function(index) {
            return this.arrys[index];
        }

        List.prototype.remove = function(id) {
            let index = this.indexOf(id);
            if (index != -1) {
                this.arrys[index] = null;
            }
            this.compress();
        };

        List.prototype.val = function() {
            let tempArr = [];
            for (let i = 0; i < this.size(); i++) {
                if (this.get(i) != null) {
                    tempArr[i] = this.get(i);
                }
            }
            return tempArr;
        }
    }


    function ListMap(){

        this.entries = new Map();

        ListMap.prototype.put = function(key, val) {
            this.entries.set(key,val);
        }

        ListMap.prototype.get = function(key) {
            return this.entries.get(key);
        }

        ListMap.prototype.exists = function(key) {
            return this.entries.has(key);
        }

        ListMap.prototype.remove = function(key) {
            return this.entries.delete(key);
        }

        ListMap.prototype.size = function() {
            return this.entries.size;
        }

    }

    function Storage() {

        let datacenter=ainobug.util.newMap();

        Storage.prototype.put= function(key,val){
            datacenter.put(key,val);
        }

        Storage.prototype.get= function(key){
            return datacenter.get(key);
        }

        Storage.prototype.remove= function(key){
            return datacenter.remove(key);
        }

        Storage.prototype.exists= function(key){
            return datacenter.exists(key);
        }

        Storage.prototype.val= function(){
            return datacenter.val();
        }

    }

    function _Util(){


        this.newList=function(){
            return new List();
        };

        this.isList=function(obj){
            return List==obj.constructor
        };

        this.newMap=function(){
            return new ListMap();
        };

        this.log=function(msg){
            if(window.console){
                window.console.log(msg);
            }
        };

        this.json=function(obj){
            return JSON.stringify(obj);
        };


        this.getHtmlEndpoint=function(){
            return ainobug.config.getHtmlEndpoint();
        };

        this.getDataEndpoint=function(){
            return ainobug.config.getDataEndpoint();
        };

        /**
         *{
			url:'www.baidu.com',
			data:{},
			async:true,
			type:'GET'
			}
         */
        function Ajax(){

            this.request=function(options){

                let defOptions={
                    async:true,
                    data:{},
                    headers:{
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    dataType:"json"
                }
                let finalOptions={};
                $.extend(true,finalOptions,defOptions,options);
                $.ajax({
                    url: finalOptions.url,
                    dataType:finalOptions.dataType,
                    type: finalOptions.type,
                    data:finalOptions.data,
                    async:finalOptions.async,
                    headers:
                        $.extend({},finalOptions.headers
                            ,{_token: ainobug.ticket.getTicket()}
                        ),
                    success: function(data){
                        try {

                            ainobug.util.log('success: '+finalOptions.url);

                            //有效的数据类型是 text， html， xml， json，jsonp，和 script
                            if(this.dataType=='html'
                                || this.dataType=='text'
                                || this.dataType=='script'
                                || this.dataType=='xml' ){
                                if(finalOptions.success){
                                    finalOptions.success(data);
                                }
                                return ;
                            }

                            if(data.status=='BYS_ERROR'||data.status=='SYS_ERROR'){
                                ainobug.toast.error(JSON.stringify(data.data));
                                if(finalOptions.error){
                                    finalOptions.error(data);
                                }
                            }
                            else{
                                if(finalOptions.success){
                                    finalOptions.success(data.data);
                                }
                            }
                        }finally {
                            ainobug.util.removeSpinnerOnBtn(finalOptions.targetBtn);
                        }

                    },
                    error:function(data){
                        try {
                            ainobug.util.log('error: ' + finalOptions.url);
                            ainobug.toast.error(data.responseText);
                            if (finalOptions.error) {
                                finalOptions.error(data);
                            }
                        }finally {
                            ainobug.util.removeSpinnerOnBtn(finalOptions.targetBtn);
                        }
                    }
                });

                ainobug.util.addSpinnerOnBtn(finalOptions.targetBtn);

            }
        }

        this.ajaxGet=function(options){
            let ajax=new Ajax();
            ajax.request($.extend({},options,{type:'GET'}));
        }

        this.ajaxPost=function(options){
            let ajax=new Ajax();
            ajax.request($.extend({},options,{type:'POST'}));
        }

        this.addSpinnerOnBtn=function (btnSelector) {
            if(btnSelector){
                let $btn=$(btnSelector);
                let spinner='<span class="spinner-border spinner-border-sm " style="vertical-align: initial;"  ></span>';
                $btn.prepend($(spinner));
                $btn.attr("disabled","true");
                $btn.addClass("ainobug-disabled");
            }
        }

        this.removeSpinnerOnBtn=function (btnSelector) {
            if(btnSelector){

                let $btn=$(btnSelector);
                $btn.find('span.spinner-border').remove();
                $btn.removeAttr("disabled");
                $btn.removeClass("ainobug-disabled");
            }
        }


        this.log=function(msg){
            if(window.console){
                window.console.log(msg);
            }
        }

        this.serializeObj=function(formSelector){
            let obj={};
            let arrays= $(formSelector).serializeArray();

            for(let i=0;i<arrays.length;i++){
                let ele=arrays[i];
                let eleName=$.trim(ele.name);
                let eleValue=$.trim(ele.value);
                obj[eleName]=eleValue;
            }
            return obj;
        }


        this.formatDate=function(d){
            let date = (d.getFullYear()) + "-" +
                (d.getMonth() + 1) + "-" +
                (d.getDate()) + " " +
                (d.getHours()) + ":" +
                (d.getMinutes()) + ":" +
                (d.getSeconds())+"."+
                (d.getMilliseconds());
            return date;
        }

        this.syntaxHighlight=function(json) {
            if (typeof json != 'string') {
                json = JSON.stringify(json, undefined, 2);
            }
            json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
            return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
                function(match) {
                    let cls = 'number';
                    if (/^"/.test(match)) {
                        if (/:$/.test(match)) {
                            cls = 'key';
                        } else {
                            cls = 'string';
                        }
                    } else if (/true|false/.test(match)) {
                        cls = 'boolean';
                    } else if (/null/.test(match)) {
                        cls = 'null';
                    }
                    return '<span class="' + cls + '">' + match + '</span>';
                }
            );
        }

        this.hasAttribute= function(selector,attrName){
            return $(selector)[0].hasAttribute(attrName);
        }

        this.removeAttribute= function(selector,attrName){
            return $(selector)[0].removeAttribute(attrName);
        }

        this.removeParam=function (str) {
            let index;
            if((index=str.indexOf('?'))!=-1){
                return str.substring(0,index);
            }
            return str;
        }

        this.clearSlash=function(str){
            let finalStr='';
            for(let i=0;i<str.length;i++){
                let c=str[i];
                if(('/'==c||'\\'==c)
                    && i<str.length-1
                    && ('/'==str[i+1]||'\\'==str[i+1])){
                    continue;
                }
                else {
                    finalStr+=c;
                }
            }
            return finalStr;
        }

        this.combineUrl=function (host,path) {
            path=this.clearSlash(path);
            if(host.endWith('/')||host.endWith('\\')){
                if(path.startWith('/')||path.startWith('\\')){
                    return host+path.substring(1);
                }
                else {
                    return host+path;
                }
            }
            else {
                if(path.startWith('/')||path.startWith('\\')){
                    return host+path;
                }
                else {
                    return host+'/'+path;
                }
            }
        }

    }

    function Toast() {
        function def(opts){
            let defOpts={
                stack:10,
                position: 'bottom-right'
            }
            return $.extend({},defOpts,opts);
        }

        this.success=function(heading,text){
            $.toast(def({
                heading: heading,
                text: text,
                showHideTransition: 'slide',
                icon: 'success'
            }));
        }

        this.warning=function(heading,text){
            $.toast(def({
                heading: heading,
                text: text,
                showHideTransition: 'slide',
                icon: 'warning'
            }));
        }

        this.info=function(heading,text){
            $.toast(def({
                heading: heading,
                text: text,
                showHideTransition: 'slide',
                icon: 'info'
            }));
        }

        this.error=function(heading,text){
            $.toast(def({
                heading: heading,
                text: text,
                showHideTransition: 'slide',
                hideAfter: 10000,
                icon: 'error'
            }));
        }

    }

    function Confirm() {

        this.confirm=function (success,cancel,content) {

            if(!content){
                content='确定删除？';
            }
            $.confirm({
                title: false,
                content: content,

                buttons: {
                    '确认': function () {
                        if(success){
                            success();
                        }
                    },
                    '撤消': function () {
                        if(cancel){
                            cancel();
                        }
                    }
                }
            });

        }

    }


    function MV() {


        function Directive() {

            let util=ainobug.util;

            /**
             * ainobug-text
             * @param $selector
             * @param record
             * @param context
             * @param index
             */
            this.text =function ($selector,record,context) {
                let property=$selector.attr("ainobug-text");
                let text=val(property,record,context);
                $selector.text(text);
            }

            function refVal(record,refLink) {
                if(refLink.indexOf(".")!=-1){
                    let ks=refLink.split(".");
                    let tempValue=record;
                    ks.forEach(function (k) {
                        tempValue=tempValue[k];
                    })
                    if(tempValue==record){
                        tempValue='';
                    }
                    return tempValue;
                }
                else {
                    return record[refLink];
                }
            }

            function val(property,record,context) {
                let value='';
                //看看是不是函数指令
                if(property.indexOf('(')!=-1){
                    let fn=property.substring(0,property.indexOf('('));

                    if("index"==fn){
                        value= record['ainobugIndex'];
                    }

                    else if("timeFormat"==fn){
                        let index=property.indexOf('{');
                        let tempProperty=property.substring(index+1,property.indexOf('}'));
                        let arg=record[tempProperty];
                        if(typeof(arg) != "undefined" && arg==0) {
                            value="";
                        }
                        else {
                            value=util.formatDate(new Date(arg));
                        }

                    }

                    else {
                        value=context[fn](record);
                    }

                }
                else {
                    if("{this}"==property){
                        return record;
                    }
                    let evalProperty=property;
                    let index;
                    while ((index = evalProperty.indexOf('{')) != -1) {

                        // value before '{'
                        value += evalProperty.substring(0, index);

                        // 查找下一个}，{} 里面是真实地属性名
                        evalProperty = evalProperty.substring(index);
                        let tempProperty = evalProperty.substring(1, evalProperty.indexOf('}'));
                        value += refVal(record,tempProperty);

                        // 赋值剩下的待解释的字符串
                        evalProperty = evalProperty.substring(evalProperty.indexOf('}') + 1);
                    }
                    value +=evalProperty;
                }

                if("null" == value)
                    value="";
                return value;
            }

            /**
             * ainobug-data
             * @param $selector
             * @param record
             * @param context
             * @param index
             */
            this.data = function  ($selector,record,context) {
                let dataPath=$selector.attr("ainobug-data");
                let dataAttrs=dataPath.split(",");
                for(let i=0;i<dataAttrs.length;i++){
                    let dataAttr=dataAttrs[i];
                    let k=dataAttr.split("=")[0];
                    let v=dataAttr.split("=")[1];

                    $selector.data(k.toLowerCase(),val(v,record,context));
                }

            };

            /**
             * ainobug-attr
             * @param $selector
             * @param record
             * @param context
             * @param index
             */
            this.attr = function  ($selector,record,context) {
                let dataPath=$selector.attr("ainobug-attr");
                let dataAttrs=dataPath.split(",");
                for(let i=0;i<dataAttrs.length;i++){
                    let dataAttr=dataAttrs[i];
                    let k=dataAttr.split("=")[0];
                    let v=dataAttr.split("=")[1];

                    $selector.attr(k.toLowerCase(),val(v,record,context));
                }

            }

            /**
             * ainobug-href
             * @param $selector
             * @param record
             * @param context
             * @param index
             */
            this.href = function ($selector,record,context) {
                let property=$selector.attr("ainobug-href");
                let text=val(property,record,context);
                $selector.attr('href',text);
            }

            /**
             * ainobug-id
             * @param $selector
             * @param record
             * @param context
             * @param index
             */
            this.id = function ($selector,record,context) {
                let property=$selector.attr("ainobug-id");
                let text=val(property,record,context);
                $selector.attr('id',text);
            }

            /**
             * ainobug-name
             * @param $selector
             * @param record
             * @param context
             * @param index
             */
            this.name = function ($selector,record,context) {
                let property=$selector.attr("ainobug-name");
                let text=val(property,record,context);
                $selector.attr('name',text);
            }

            this.value =function ($selector,record,context) {
                let property=$selector.attr("ainobug-value");
                let text=val(property,record,context);
                $selector.attr('value',text);
            }


        }



        function Render(directive) {

            let util=ainobug.util;

            function cloneHtmlWithFor(selector) {
                let $selector=$(selector);
                let $clone=$selector.clone();
                // 删除跟多的和 ainobug-for 相关的属性
                util.removeAttribute($clone,"ainobug-for");
                $clone.addClass('ainobug-template-render');
                clearClass($clone);
                return $clone;
            }


            /**
             * 迭代整棵DOM树
             * @param $start
             * @param record
             * @param context
             * @param index
             * @param fn
             */
            function domWalker($start,record,context) {



                if(util.hasAttribute($start,"ainobug-for")){  // alias in {property}
                    let property=$start.attr("ainobug-for");
                    let alias=property.split(" ")[0];
                    let evalProperty=property.substring(property.indexOf('{')+1, property.indexOf('}'));
                    let data=record;
                    if("this"!=evalProperty){
                        data=record[evalProperty];
                    }
                    let $parent=$start.parent();
                    for(let i=0;i<data.length;i++){
                        let rd=data[i];
                        try{
                            rd[alias]=rd;
                            rd['ainobugIndex']=i;
                            // clone出来一个新的dom，以便在迭代的时候append到父节点
                            let $cloneFor=$(cloneHtmlWithFor($start));
                            domWalker($cloneFor,rd,context);
                            $parent.append($cloneFor); //返回处理过的dom，数据已经绑定了
                        }finally {
                            delete rd[alias];
                            delete rd['ainobugIndex'];
                        }
                    }
                    return ;
                }


                if(util.hasAttribute($start,"ainobug-if")){
                    let filter=context["filter"](record,$start);
                    if(!filter){
                        $start.remove(); //从当前dom里面移除
                        return;
                    }
                }

                if(util.hasAttribute($start,"ainobug-text")){
                    directive.text($start,record,context);
                }

                if(util.hasAttribute($start,"ainobug-data")){
                    directive.data($start,record,context);
                }

                if(util.hasAttribute($start,"ainobug-attr")){
                    directive.attr($start,record,context);
                }

                if(util.hasAttribute($start,"ainobug-href")){
                    directive.href($start,record,context);
                }

                if(util.hasAttribute($start,"ainobug-id")){
                    directive.id($start,record,context);
                }

                if(util.hasAttribute($start,"ainobug-name")){
                    directive.name($start,record,context);
                }

                if(util.hasAttribute($start,"ainobug-value")){
                    directive.value($start,record,context);
                }

                $start.children().filter(".ainobug-template-render").remove();
                let $sc=$start.children().not(".ainobug-template-render");
                $sc.each(function (i,element) {

                    domWalker($(element),record,context);

                    if(util.hasAttribute($(element),"ainobug-post")){
                        context.post(record,$(element),context);
                    }

                });

                if(util.hasAttribute($start,"ainobug-post")){
                    context.post(record,$start,context);
                }
            }

            this.render=function (selector,record,context) {

                let $selector=$(selector);
                //迭代整棵DOM树
                domWalker($selector,record,context);
                clearClass($selector);
            }

            function clearClass($selector) {
                $selector.removeClass("ainobug-template-hide");
                util.removeAttribute($selector,"ainobug-template");
                return $selector;
            }
            
        }

        this.renderHtml=function (selector,record,context) {

            new Render(new Directive()).render($(selector),record,context);

        };

        this.cloneHtml=function (selector,record,context) {
            // if(!('append' in context)
            //     || context.append){
            //     context.append=false;
            // }

            let $selector=$(selector).clone();
            new Render(new Directive()).render($selector,record,context);
            return $selector;

        }

        
    }


    ainobug.util=new _Util();
    ainobug.toast=new Toast();
    ainobug.util.confirm=new Confirm().confirm;
    ainobug.storage=new Storage();
    
    ainobug.mv=new MV();

    ainobug.proxy={

        success:function (fn,heading,text) {

            let inner= function(data){
                // 移掉，如果请求正确，避免产生过多的通知
                // ainobug.toast.success(heading?heading:"操作成功!",text?text:"");
                fn(data);
            }
            inner.isProxy=true;
            return inner;
        },
        error:function (fn,heading,text) {
            let inner=  function(data){
                ainobug.toast.error(heading?heading:"操作失败!",text?text:"")
                fn(data);
            }
            inner.isProxy=true;
            return inner;
        }
    };

    ainobug.ready=function(func){
        $(function(){
            try{
                func();
            }catch (e) {
                ainobug.util.log(e);
            }
        });
    };

})(window.ainobug);



