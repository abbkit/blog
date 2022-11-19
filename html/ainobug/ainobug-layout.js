(function(ainobug){


	function LayoutDraw($dom){

		let $htl=$dom;


        /**
         *
         * @param $html
         * @param requsetVO  {
						layoutId:layoutId,
						htmlUrl:htmlUrl
				}
         * @returns {*}
         */
         function js($html,requsetVO){
            $.each($html,function (i,e) {
                let nodeName=e.nodeName;
                if(('SCRIPT'==nodeName||'script'==nodeName)&&$(e).attr('src')!=undefined){
                    let defaultRelative=requsetVO.htmlUrl.substring(0,requsetVO.htmlUrl.lastIndexOf('/'));
                    let absolute=ainobug.util.getHtmlEndpoint()+ "/"+defaultRelative+"/"+$(e).attr('src');
                    let id=$.md5(absolute);
                    $(e).attr('src',absolute).attr('id' ,id);
                }
            });


            $html.find('script').each(function (i ,e) {
                if($(e).attr('src')!=undefined){
                    let defaultRelative=requsetVO.htmlUrl.substring(0,requsetVO.htmlUrl.lastIndexOf('/'));
                    let absolute=ainobug.util.combineUrl(ainobug.util.getHtmlEndpoint(),"/"+defaultRelative+"/"+$(e).attr('src'));
                    let srcText=$(e).attr('src');
                    if(srcText.startWith("/")){
                        absolute=ainobug.util.getHtmlEndpoint()+srcText;
                    }
                    let id=$.md5(absolute);
                    $(e).attr('src',absolute).attr('id' ,id);
                }
            });

            return $html;

        }

        /**
         *
         * @param $html
         * @param requsetVO {
						layoutId:layoutId,
						htmlUrl:htmlUrl
				}
         * @returns {*}
         */
        function css($html,requsetVO) {

            $.each($html,function (i,e) {
                let nodeName=e.nodeName;
                if(('LINK'==nodeName||'link'==nodeName)&&$(e).attr('href')!=undefined){
                    let defaultRelative=requsetVO.htmlUrl.substring(0,requsetVO.htmlUrl.lastIndexOf('/'));
                    let absolute=ainobug.util.getHtmlEndpoint()+ "/"+defaultRelative+"/"+$(e).attr('href');
                    let id=$.md5(absolute);
                    $(e).attr('href',absolute).attr('id' ,id);
                }
            });

            $html.find('link').each(function (i ,e) {
                let hrefText=$(e).attr('href');
                let defaultRelative=requsetVO.htmlUrl.substring(0,requsetVO.htmlUrl.lastIndexOf('/'));
                let absolute=ainobug.util.getHtmlEndpoint()+ "/"+defaultRelative+"/"+$(e).attr('href');
                if(hrefText.startWith("/")){
                    absolute=ainobug.util.getHtmlEndpoint()+hrefText;
                }
                let id=$.md5(absolute);
                $(e).attr('href',absolute).attr('id' ,id);
            });

            return $html;
        }

        /**
         *
         * @param $html
         * @param requsetVO {
						layoutId:layoutId,
						htmlUrl:htmlUrl
				}
         * @returns {*}
         */
        function all($html,requsetVO) {
            js($html,requsetVO);
            css($html,requsetVO);
        }




		function dataHtmlUrls(){
			let all=ainobug.util.newList();
			if($htl.attr("data-htmlurl")!=undefined){
				all.add($htl);
			}
			let $foundHtmlUrl=$htl.find('[data-htmlurl]');
			if($foundHtmlUrl.length>0){
				for(let i=0;i<$foundHtmlUrl.length;i++){
					all.add($($foundHtmlUrl[i]));
				}
			}
			return all.val();
		}

		function request(){
			let $allHtmlUrls=dataHtmlUrls();
			for(let i=0;i<$allHtmlUrls.length;i++){
				let $htmlUrl=$($allHtmlUrls[i]);
				let layoutId=$htmlUrl.data('layoutid');
				let htmlUrl=$htmlUrl.data('htmlurl');
				if(htmlUrl==''){
					continue;
				}
				let requestVO={
						layoutId:layoutId,
						htmlUrl:htmlUrl
				}
				let finalUrl=ainobug.util.combineUrl(ainobug.util.getHtmlEndpoint(),ainobug.util.removeParam(htmlUrl));
				ainobug.util.ajaxGet({
                    dataType:"text",
					url:finalUrl,
					// data:{data:ainobug.util.json(requestVO)},
                    requestVO :requestVO, // avoid closure letiable refer to the same one with reference type
					success:function(html){
						html='<div>'+html+'</div>';
						let requestVO =this.requestVO; // run as local letiable
						let $layoutDom=ainobug.layout.getLayoutDom(requestVO.layoutId);

						let param={};
						let index=requestVO.htmlUrl.indexOf('?param=');
						if(index!=-1){
							param=JSON.parse(requestVO.htmlUrl.substring(index+'?param='.length));
						}

						let token={};
						$layoutDom.data('param',param);
						$layoutDom.data('token',token);
						let $html=$(html);

                        all($html,requestVO); // modify js , css resource location

                        //append url to hash
						location.hash=requestVO.htmlUrl;

						let layout=new LayoutDraw($html);
						layout.draw(requestVO.layoutId);
					},
					error:function(data){
//						let layout=new Layout(data);
//						layout.draw();
					}
                });
			}
		}

		function appendTo(layoutId){
			let $target=$(document).find('[data-layoutid="'+layoutId+'"]');
			$htl.appendTo($target);
		}

		function bingSelect(){
			if(ainobug.$_codeTable&&ainobug.$_codeTable.defaultDraw){
				ainobug.$_codeTable.defaultDraw($htl);
			}
		}

		this.draw=function(layoutId){
			bingSelect();
			if(layoutId){
				appendTo(layoutId);
			}
			request();
		}


	}


	function Layout(){

		this.draw=function(layoutId,$dom){
			let layout=new LayoutDraw($dom);
			layout.draw(layoutId);
		};

		this.isLayout=function($_dom){
			let $dom=$($_dom);
			return $dom.is('div')&&$dom.attr("data-layoutid");
		};

		this.getClosestLayoutId=function($_dom){
			let $dom=$($_dom);
			let $parent=$dom;
			let count=0
			do{
				if(this.isLayout($parent)){
					return $parent.data('layoutid');
				}
				count++;
			}while(count<1000&&($parent=$parent.parent()));
		};


		this.replace=function(layoutId,htmlurl){

			let $layout=$(document).find('[data-layoutid="'+layoutId+'"]');
            $layout.data('htmlurl',htmlurl);
            $layout.empty();
            this.draw(layoutId,$layout);
		};

		this.getLayoutDom=function(layoutId){
			return $(document).find('div[data-layoutId="'+layoutId+'"]');
		};

		this.getParameter=function($dom) {
			let layoutId=this.getClosestLayoutId($dom);
			let layoutDom=this.getLayoutDom(layoutId);
			return $(layoutDom).data('param');
		};

		this.getToken=function($dom) {
			let layoutId=this.getClosestLayoutId($dom);
			let layoutDom=this.getLayoutDom(layoutId);
			return $(layoutDom).data('token');
		};


        function DataExchange(){

            /**
             * {
		 * url:'',
		 * formData:'',
		 * paginationData:''
		 * }
             */
            this.ajaxGet=function(options){

				/*let paginationDataOpts={
				 };
				 if(options.paginationData){
				 paginationDataOpts=$.extend(paginationDataOpts,{
				 paginationData:ainobug.util.json({
				 pageNumber:options.paginationData.pageNumber,
				 pageSize:options.paginationData.pageSize,
				 orders:options.paginationData.orders
				 })
				 })
				 }

				 let formDataOpts={};
				 if(options.formData){
				 formDataOpts=$.extend(formDataOpts,{
				 formData:ainobug.util.json(options.formData)
				 })
				 }*/

                let _data = {};
                if(options.formData){
                    _data=$.extend(_data,
                        options.formData  // form data
                    );
                };
                if(options.paginationData){
                    _data=$.extend(_data,
                        {
                            pageNumber:options.paginationData.pageNumber,
                            pageSize:options.paginationData.pageSize,
                            orders:options.paginationData.orders
                        });
                };
                let finalUrl=ainobug.util.combineUrl(ainobug.util.getDataEndpoint(),options.url);
                ainobug.util.ajaxGet({
                    url:finalUrl,
                    data:_data,
                    success:function(data){
                        if(options.success){
                            options.success(data);
                        }
                    },
					error:function (data) {
                        ainobug.util.log('bys error : '+options.url);
                        // goto login/html
                        // if("BYS_ERROR"==resp.status
                        //     &&"E0005"==resp.data){
                        //     ainobug.config.gotoLoginView();
                        //     return;
                        // }
                        // ainobug.toast.error("error",resp.data);
                        if(options.failure){
                            options.failure(data);
                        }

                    }

                });
            }

            /**
             * {
		 * url:'',
		 * formData:'',
		 * paginationData:'',
		 * token:'',
		 * success:function(){},
		 * failure:function(resp){}
		 * }
             */
            this.ajaxPost=function(options){

				/*let paginationDataOpts={
				 };
				 if(options.paginationData){
				 paginationDataOpts=$.extend(paginationDataOpts,{
				 paginationData:ainobug.util.json({
				 pageNumber:options.paginationData.pageNumber,
				 pageSize:options.paginationData.pageSize,
				 orders:options.paginationData.orders
				 })
				 })
				 }

				 let formDataOpts={};
				 if(options.formData){
				 formDataOpts=$.extend(formDataOpts,{
				 formData:ainobug.util.json(options.formData)
				 })
				 }

				 let tokenOpts={};
				 if(options.token){
				 tokenOpts=$.extend(tokenOpts,{
				 token:ainobug.util.json(options.token)
				 })
				 }*/

                let _data={};
                if(options.formData){
                    _data=$.extend(_data,
                        options.formData  // form data
                    );
                };
                if(options.paginationData){
                    _data=$.extend(_data,
                        {
                            pageNumber:options.paginationData.pageNumber,
                            pageSize:options.paginationData.pageSize,
                            orders:options.paginationData.orders
                        });
                };
                let finalUrl=ainobug.util.combineUrl(ainobug.util.getDataEndpoint(),options.url);
                ainobug.util.ajaxPost({
                    url:finalUrl,
                    data:_data,
                    success:function(data){
                        let success=options.success;
                        if(success.isProxy){
                            success(data);
                        }else{
                            ainobug.proxy.success(success)(data)
                        }
                    },
					error:function (data) {
                        ainobug.util.log('bys error : '+options.url);
                        // goto login/html
                        // if("BYS_ERROR"==resp.status
                        //     &&"E0005"==resp.data){
                        //     ainobug.config.gotoLoginView();
                        //     return;
                        // }
                        // ainobug.toast.error("error",resp.data);
                        if(options.failure){
                            options.failure(data);
                        }

                    }
                });
            }

            /**
             * {
		 * url:'',
		 * formSelector:'',
		 * success:function(){},
		 * failure:function(resp){}
		 * }
             */
            this.submitForm=function(options){
                let $form=$(options.formSelector);
                let layoutId=ainobug.layout.getClosestLayoutId($form);
                let layoutDom=ainobug.layout.getLayoutDom(layoutId);
                let token=$(layoutDom).data('token');
                this.ajaxPost({
                    url:options.url,
                    formData:ainobug.util.serializeObj(options.formSelector),
                    token:token,
                    success:options.success,
                    failure:function(resp){
                        let $form=$(options.formSelector);
                        let layoutId=ainobug.layout.getClosestLayoutId($form);
                        let layoutDom=ainobug.layout.getLayoutDom(layoutId);
                        let token={};
                        if(resp.token){
                            token=resp.token;
                        }
                        return $(layoutDom).data('token',token);
                    }
                });
            }
        }

        function HtmlExchange(){

            this.htmlView=function(htmlUrl,layoutId){
                ainobug.layout.replace(layoutId,htmlUrl);
            };

            this.linkView=function($linkDom,htmlUrl,layoutId){
                if(!layoutId)
                    layoutId=ainobug.layout.getClosestLayoutId($linkDom);
                this.htmlView(htmlUrl,layoutId);
            };

        }

        this.data=new DataExchange();
        this.html=new HtmlExchange();

	}


    ainobug.layout=new Layout();

    ainobug.pageTemplate={
        ajaxGet:function(options){
            ainobug.layout.data.ajaxGet(options);
        },
        submitForm:function(options){
            ainobug.layout.data.submitForm(options);
        },
        ajaxPost:function(options){
            ainobug.layout.data.ajaxPost(options);
        },
        codeTable:function($dom){
            ainobug.$_codeTable.draw($($dom));
        },
        warning:ainobug.toast.warning,
        success:ainobug.toast.success,
        info:ainobug.toast.info,
        error:ainobug.toast.error,
        log:ainobug.util.log,
        storage:ainobug.storage,
		confirm:ainobug.util.confirm

    };

    // 使用此语句执行页面引导脚本
    // window.ainobug.layout.draw('body',$('body'));


	$.fn.extend({
		goView:function(htmlUrl,data,layoutId){
			if(data){
				let defData={
						htmlRequestTime:new Date().getTime()
				}
				htmlUrl=htmlUrl+'?param='+ainobug.util.json($.extend({},defData,data));
			}
			ainobug.layout.html.linkView($(this),htmlUrl,layoutId);
		},
		getViewParam:function(){
			return ainobug.layout.getParameter(this);
		},
		serializeObj:function(){
			return ainobug.util.serializeObj(this);
		}
	});

})(window.ainobug);



