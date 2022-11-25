/**
 * Created by J on 2020/3/10.
 */
/*
 * fingerprintJS 0.5.5 - Fast browser fingerprint library
 * https://github.com/Valve/fingerprintjs
 * Copyright (c) 2013 Valentin Vasilyev (valentin.vasilyev@outlook.com)
 * Licensed under the MIT (http://www.opensource.org/licenses/mit-license.php) license.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

;(function (name, context, definition) {
    if (typeof module !== 'undefined' && module.exports) { module.exports = definition(); }
    else if (typeof define === 'function' && define.amd) { define(definition); }
    else { context[name] = definition(); }
})('Fingerprint', this, function () {
    'use strict';

    var Fingerprint = function (options) {
        var nativeForEach, nativeMap;
        nativeForEach = Array.prototype.forEach;
        nativeMap = Array.prototype.map;

        this.each = function (obj, iterator, context) {
            if (obj === null) {
                return;
            }
            if (nativeForEach && obj.forEach === nativeForEach) {
                obj.forEach(iterator, context);
            } else if (obj.length === +obj.length) {
                for (var i = 0, l = obj.length; i < l; i++) {
                    if (iterator.call(context, obj[i], i, obj) === {}) return;
                }
            } else {
                for (var key in obj) {
                    if (obj.hasOwnProperty(key)) {
                        if (iterator.call(context, obj[key], key, obj) === {}) return;
                    }
                }
            }
        };

        this.map = function(obj, iterator, context) {
            var results = [];
            // Not using strict equality so that this acts as a
            // shortcut to checking for `null` and `undefined`.
            if (obj == null) return results;
            if (nativeMap && obj.map === nativeMap) return obj.map(iterator, context);
            this.each(obj, function(value, index, list) {
                results[results.length] = iterator.call(context, value, index, list);
            });
            return results;
        };

        if (typeof options == 'object'){
            this.hasher = options.hasher;
            this.screen_resolution = options.screen_resolution;
            this.screen_orientation = options.screen_orientation;
            this.canvas = options.canvas;
            this.ie_activex = options.ie_activex;
        } else if(typeof options == 'function'){
            this.hasher = options;
        }
    };

    Fingerprint.prototype = {
        get: function(){
            var keys = [];
            keys.push(navigator.userAgent);
            keys.push(navigator.language);
            keys.push(screen.colorDepth);
            if (this.screen_resolution) {
                var resolution = this.getScreenResolution();
                if (typeof resolution !== 'undefined'){ // headless browsers, such as phantomjs
                    keys.push(resolution.join('x'));
                }
            }
            keys.push(new Date().getTimezoneOffset());
            keys.push(this.hasSessionStorage());
            keys.push(this.hasLocalStorage());
            keys.push(this.hasIndexDb());
            //body might not be defined at this point or removed programmatically
            if(document.body){
                keys.push(typeof(document.body.addBehavior));
            } else {
                keys.push(typeof undefined);
            }
            keys.push(typeof(window.openDatabase));
            keys.push(navigator.cpuClass);
            keys.push(navigator.platform);
            keys.push(navigator.doNotTrack);
            keys.push(this.getPluginsString());
            if(this.canvas && this.isCanvasSupported()){
                keys.push(this.getCanvasFingerprint());
            }
            if(this.hasher){
                return this.hasher(keys.join('###'), 31);
            } else {
                return this.murmurhash3_32_gc(keys.join('###'), 31);
            }
        },

        /**
         * JS Implementation of MurmurHash3 (r136) (as of May 20, 2011)
         *
         * @author <a href="mailto:gary.court@gmail.com">Gary Court</a>
         * @see http://github.com/garycourt/murmurhash-js
         * @author <a href="mailto:aappleby@gmail.com">Austin Appleby</a>
         * @see http://sites.google.com/site/murmurhash/
         *
         * @param {string} key ASCII only
         * @param {number} seed Positive integer only
         * @return {number} 32-bit positive integer hash
         */

        murmurhash3_32_gc: function(key, seed) {
            var remainder, bytes, h1, h1b, c1, c2, k1, i;

            remainder = key.length & 3; // key.length % 4
            bytes = key.length - remainder;
            h1 = seed;
            c1 = 0xcc9e2d51;
            c2 = 0x1b873593;
            i = 0;

            while (i < bytes) {
                k1 =
                    ((key.charCodeAt(i) & 0xff)) |
                    ((key.charCodeAt(++i) & 0xff) << 8) |
                    ((key.charCodeAt(++i) & 0xff) << 16) |
                    ((key.charCodeAt(++i) & 0xff) << 24);
                ++i;

                k1 = ((((k1 & 0xffff) * c1) + ((((k1 >>> 16) * c1) & 0xffff) << 16))) & 0xffffffff;
                k1 = (k1 << 15) | (k1 >>> 17);
                k1 = ((((k1 & 0xffff) * c2) + ((((k1 >>> 16) * c2) & 0xffff) << 16))) & 0xffffffff;

                h1 ^= k1;
                h1 = (h1 << 13) | (h1 >>> 19);
                h1b = ((((h1 & 0xffff) * 5) + ((((h1 >>> 16) * 5) & 0xffff) << 16))) & 0xffffffff;
                h1 = (((h1b & 0xffff) + 0x6b64) + ((((h1b >>> 16) + 0xe654) & 0xffff) << 16));
            }

            k1 = 0;

            switch (remainder) {
                case 3: k1 ^= (key.charCodeAt(i + 2) & 0xff) << 16;
                case 2: k1 ^= (key.charCodeAt(i + 1) & 0xff) << 8;
                case 1: k1 ^= (key.charCodeAt(i) & 0xff);

                    k1 = (((k1 & 0xffff) * c1) + ((((k1 >>> 16) * c1) & 0xffff) << 16)) & 0xffffffff;
                    k1 = (k1 << 15) | (k1 >>> 17);
                    k1 = (((k1 & 0xffff) * c2) + ((((k1 >>> 16) * c2) & 0xffff) << 16)) & 0xffffffff;
                    h1 ^= k1;
            }

            h1 ^= key.length;

            h1 ^= h1 >>> 16;
            h1 = (((h1 & 0xffff) * 0x85ebca6b) + ((((h1 >>> 16) * 0x85ebca6b) & 0xffff) << 16)) & 0xffffffff;
            h1 ^= h1 >>> 13;
            h1 = ((((h1 & 0xffff) * 0xc2b2ae35) + ((((h1 >>> 16) * 0xc2b2ae35) & 0xffff) << 16))) & 0xffffffff;
            h1 ^= h1 >>> 16;

            return h1 >>> 0;
        },

        // https://bugzilla.mozilla.org/show_bug.cgi?id=781447
        hasLocalStorage: function () {
            try{
                return !!window.localStorage;
            } catch(e) {
                return true; // SecurityError when referencing it means it exists
            }
        },

        hasSessionStorage: function () {
            try{
                return !!window.sessionStorage;
            } catch(e) {
                return true; // SecurityError when referencing it means it exists
            }
        },

        hasIndexDb: function () {
            try{
                return !!window.indexedDB;
            } catch(e) {
                return true; // SecurityError when referencing it means it exists
            }
        },

        isCanvasSupported: function () {
            var elem = document.createElement('canvas');
            return !!(elem.getContext && elem.getContext('2d'));
        },

        isIE: function () {
            if(navigator.appName === 'Microsoft Internet Explorer') {
                return true;
            } else if(navigator.appName === 'Netscape' && /Trident/.test(navigator.userAgent)){// IE 11
                return true;
            }
            return false;
        },

        getPluginsString: function () {
            if(this.isIE() && this.ie_activex){
                return this.getIEPluginsString();
            } else {
                return this.getRegularPluginsString();
            }
        },

        getRegularPluginsString: function () {
            return this.map(navigator.plugins, function (p) {
                var mimeTypes = this.map(p, function(mt){
                    return [mt.type, mt.suffixes].join('~');
                }).join(',');
                return [p.name, p.description, mimeTypes].join('::');
            }, this).join(';');
        },

        getIEPluginsString: function () {
            if(window.ActiveXObject){
                var names = ['ShockwaveFlash.ShockwaveFlash',//flash plugin
                    'AcroPDF.PDF', // Adobe PDF reader 7+
                    'PDF.PdfCtrl', // Adobe PDF reader 6 and earlier, brrr
                    'QuickTime.QuickTime', // QuickTime
                    // 5 versions of real players
                    'rmocx.RealPlayer G2 Control',
                    'rmocx.RealPlayer G2 Control.1',
                    'RealPlayer.RealPlayer(tm) ActiveX Control (32-bit)',
                    'RealVideo.RealVideo(tm) ActiveX Control (32-bit)',
                    'RealPlayer',
                    'SWCtl.SWCtl', // ShockWave player
                    'WMPlayer.OCX', // Windows media player
                    'AgControl.AgControl', // Silverlight
                    'Skype.Detection'];

                // starting to detect plugins in IE
                return this.map(names, function(name){
                    try{
                        new ActiveXObject(name);
                        return name;
                    } catch(e){
                        return null;
                    }
                }).join(';');
            } else {
                return ""; // behavior prior version 0.5.0, not breaking backwards compat.
            }
        },

        getScreenResolution: function () {
            var resolution;
            if(this.screen_orientation){
                resolution = (screen.height > screen.width) ? [screen.height, screen.width] : [screen.width, screen.height];
            }else{
                resolution = [screen.height, screen.width];
            }
            return resolution;
        },

        getCanvasFingerprint: function () {
            var canvas = document.createElement('canvas');
            var ctx = canvas.getContext('2d');
            // https://www.browserleaks.com/canvas#how-does-it-work
            var txt = 'http://valve.github.io';
            ctx.textBaseline = "top";
            ctx.font = "14px 'Arial'";
            ctx.textBaseline = "alphabetic";
            ctx.fillStyle = "#f60";
            ctx.fillRect(125,1,62,20);
            ctx.fillStyle = "#069";
            ctx.fillText(txt, 2, 15);
            ctx.fillStyle = "rgba(102, 204, 0, 0.7)";
            ctx.fillText(txt, 4, 17);
            return canvas.toDataURL();
        }
    };


    return Fingerprint;

});

//------------------------------------------------------------------------------

(function ($) {


    $.getUrlParam = function (name) {

        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");

        var r = window.location.search.substr(1).match(reg);

        if (r != null) return decodeURI(r[2]); return null;

    }

    //apiUrl : "http://api.ainobug.com/blog-api"
    //apiUrl : "http://localhost:8989"
    $.blogConfig={
        apiUrl : "http://api.ainobug.com/blog-api"

    };

    function getHtmlUrl() {
        return window.location.pathname;
    };

    function setUnique(unique) {
        sessionStorage.setItem("blogUnique",unique);
    }

    function getUnique() {
        var unique=sessionStorage.getItem("blogUnique");
        if(unique){
            return unique;
        }else {
            return null;
        }
    }

    if(!getUnique()){
        var fingerprint = new Fingerprint().get();
        $.get($.blogConfig.apiUrl+"/access/unique?fingerprint="+fingerprint, function(result){
            if(result.code==0){
                setUnique(result.data);
            }
        },'json');
    }

    function node() {
        var unique=getUnique();
        if(unique){
            $.blogs.get($.blogConfig.apiUrl+"/access/node?unique="+unique, function(result){
            },'json');
        }
    }

    function access() {
        var unique=getUnique();
        $.blogs.get($.blogConfig.apiUrl+"/access/track?unique="+unique+"&url="+getHtmlUrl(), function(result){
        },'json');
    }

    setTimeout(function () {
        node();
        access();
    },1000);


    function ajax(type,url,headers,data,success,dataType) {

        $.extend(headers,{"Blog-Unique":getUnique()})

        $.ajax({
            url: url,
            dataType:dataType,
            type: type,
            data:data,
            async:true,
            headers:headers,
            success: success
        });

    }

    $.blogs={

        get : function (url,data,success,error,type) {

            // Shift arguments if data argument was omitted
            if ( $.isFunction( data ) ) {
                type=error;
                error=success;
                success=data;
                data = undefined;
            }

            if ( !$.isFunction( error ) ) {
                type=error;
                error=undefined;
            }

            if(!type){
                type='json';
            }

            var fn=function (result) {
                if(result.code==0){
                    success(result);
                }else {
                    if(error) error(result);
                }
            };

            ajax('GET',url,{},data,fn,type)

        },


        post : function (url,data,success,error,type) {

            // Shift arguments if data argument was omitted
            if ( $.isFunction( data ) ) {
                type=error;
                error=success;
                success=data;
                data = undefined;
            }

            if ( !$.isFunction( error ) ) {
                type=error;
                error=undefined;
            }

            if(!type){
                type='json';
            }

            var fn=function (result) {
                if(result.code==0){
                    success(result);
                }else {
                    if(error) error(result);
                }
            };

            ajax('POST',url,{},data,fn,type)

        },


        formatDate : function(d){

            if(typeof d == "number") d=new Date(d);

            var date = (d.getFullYear()) + "-" +
                (d.getMonth() + 1) + "-" +
                (d.getDate()) + " " +
                (d.getHours()) + ":" +
                (d.getMinutes()) + ":" +
                (d.getSeconds())+"."+
                (d.getMilliseconds());
            return date;
        },

        msg : function (text,type) {
            var $msgDom=$(this.getTemplate()).find('#msg');
            $msgDom.prepend(text);
            $msgDom.find('button').on('click',function () {
               $('#msg').remove();
            });
            $('blogHeader').after($msgDom);
        },

        setTemplate :function(template) {
            if(sessionStorage){
                sessionStorage.setItem('template',template);
            }
        },

        getTemplate : function() {
            if(sessionStorage){
                return sessionStorage.getItem('template');
                // return null;
            }
        }


    }





})(jQuery);

$(function () {

    $('a[role="back"]').on('click',function () {
        $('#'+$(this).attr('target-hide')).hide();
        $('#'+$(this).attr('target-show')).show();
    })

});

$(function () {

    if(!getTemplate()){
        var dateTime=new Date().getTime();
        $.get("template.html?"+dateTime, function(result){
            setTemplate(result);
        },'html');
    }


    function setTemplate(template) {
        $.blogs.setTemplate(template);
    }

    function getTemplate() {
        return $.blogs.getTemplate();
    }


    function renderHeader(activeItem,tpl) {
        var $template=$(tpl);
        var $nav=$template.find('nav');
        $nav.find('li[name="'+activeItem+'"]').addClass('active');
        $('blogHeader').append($nav);
        // $('blogHeader').css('min-height',$('blogHeader').find('nav').outerHeight())
        // $('blogHeader').css('display','inline-block');
    }


    function blog_loadHeader(activeItem) {
        var tpl=getTemplate();
        if(tpl){
            renderHeader(activeItem,tpl);
        }else {
            $.get("template.html", function(result){
                renderHeader(activeItem,result);
            },'html');
        }
    }

    if($('blogHeader').length>0){
        var selectItem=$('blogHeader').data('item');
        blog_loadHeader(selectItem);
    }


    function renderFooter(tpl) {
        var $template=$(tpl);
        var $footer=$template.find('footer');
        $('blogFooter').append($footer);
    }

    function blog_loadFooter() {
        var tpl=getTemplate();
        if(tpl){
            renderFooter(tpl);
        }else {
            $.get("template.html", function(result){
                renderFooter(result);
            },'html');
        }
    }

    var footerSelect=$('blogFooter');
    if(footerSelect.length>0){
        blog_loadFooter(selectItem);
    }


    // $(window).on("load resize",function(){
    //     var h=window.innerHeight||document.body.clientHeight||document.documentElement.clientHeight;
    //     h=h-parseInt($('blogHeader').css('min-height'));
    //     $(".blog-main").css("min-height",h);
    //     $(".blog-write-main").css("min-height",h);
    // });

});