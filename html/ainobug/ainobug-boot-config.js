
window.ainobug={};

(function(ainobug){
	function Config(){
		function getWebPath(){
			// return window.location.protocol+"//"+window.location.host+"/"+window.location.pathname.split('/')[1];
            return window.location.protocol+"//"+window.location.host;
		}

		this.getHtmlEndpoint=function(){
			//return getWebPath()+ "/get/gethtml/";
			return getWebPath();
		}

		this.getDataEndpoint=function(){
			//return getWebPath()+"/get/getdata/";
			return "http://api.ainobug.com";
		}

		this.getFileUploaderEndpoint=function(){
			//return getWebPath()+"/get/fileupload/";
			return window.location.protocol+"//"+window.location.host+"/file/batch/upload";
		}

		this.getTokenKey=function(){
			//return getWebPath()+"/get/fileupload/";
			return '_token';
		}

        this.gotoLoginView=function(){
            location.href=this.getHtmlEndpoint()+"/pages/login.html";
        }
	}

    ainobug.config=new Config();

})(window.ainobug);
