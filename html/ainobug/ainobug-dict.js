(function(ainobug ){
	
	function newMap(){
		return ainobug.util.newMap();
	}
	
	function newList(){
		return ainobug.util.newList();
	}
	
	function Code(code,name){
		this.code=code;
		this.name=name;
		this.desc='';
		this.type='';
		this.source;
		Code.prototype.equals=function($obj) {
			return this.type == $obj.type
					&&this.code == $obj.code;
		}
	}
	
	
	let codes=newMap();
	ainobug.storage.put('_codetable_key_',codes);
	
	let codeNames=newMap();
	ainobug.storage.put('_codetable_quick_names_key_',codes);
	
	let finalOpts;
	
	function codeNameKey(type,code){
		return type+"-"+code;
	}
	/**
	 * {
	 * 
	 * 
	 * 
	 * }
	 */
	function load(options){
		finalOpts=options;
		let data=options.data;
		for(let i=0;i<data.length;i++){
			let _code=data[i];
			store(_code);
		}
	}
	
	
	function store(_code){
		let type=finalOpts.getType(_code);
		let code=finalOpts.getCode(_code);
		let name=finalOpts.getName(_code);
		let desc=finalOpts.getDesc(_code);
		let codeObj=new Code(code,name);
		codeObj.desc=desc;
		codeObj.type=type;
		codeObj.source=_code;
		// store code types.
		if(codes.exists(type)){
			let codeList=codes.get(type);
			codeList.add(codeObj);
		}
		else{
			let codeList=newList();
			codeList.add(codeObj);
			codes.put(type,codeList);
		}
		
		//store code names
		let key=codeNameKey(type,code);
		if(!codeNames.exists(key)){
			codeNames.put(key,name);
		}
		
		return codeObj;
	}
	
	function getName(type,code){
		let key=codeNameKey(type,code);
		let name=codeNames.get(key);
		return name?name:'';
	}
	
	function getCodes(type){
		let _codes=codes.get(type);
		return _codes?_codes:newList();
	}
	
	/**
	 * fnFilter:function($dom,_code){
			return true;
		}
	 */
	function draw($dom,fnFilter){
		$dom=$($dom);
		let _fnFilter=fnFilter?fnFilter:function($dom,_code){
			return true;
		}
		
		function selectRender(_select){
			let $select=$(_select);
			let type=$select.data('codetype');
			let _codeList=getCodes(type);
			let codealter=$select.data('codealter');

			function process(data,external){
				let _codeTempList=data;
				if(!ainobug.util.isList(_codeTempList)){
					_codeTempList=newList();
					for(let i=0;i<data.length;i++){
						_codeTempList.add(data[i]);
					}
				}
				let _codeList=newList();
				if(!external){
					for(let i=0;i<_codeTempList.size();i++){
						_codeList.add(store(_codeTempList.get(i)));
					}
				}
				else{
					_codeList=data;
				}
				let optionEles='';
				if(codealter||codealter==''){
					optionEles=optionEles+'<option value="" >'+(codealter==''?'请选择':codealter)+'</option>'
				}
				for(let i=0;i<_codeList.size();i++){
					let _code=_codeList.get(i);
					if(_fnFilter($select,_code)){
						optionEles=optionEles+'<option value="'+_code.code+'" >'+_code.name+'</option>'
					}
				}
				$select.empty().append(optionEles);
			}
			
			if(_codeList.size()==0&&finalOpts&&finalOpts.fnData){
				finalOpts.fnData(type,process);
			}
			else{
				process(_codeList,'inner');
			}
			
		}
		
		if($dom.is('select')
				&&$dom.hasClass('codetable')){
			$dom.each(function(){
				selectRender(this);
			});
		}
		else{
			$dom.find('.codetable').each(function(){
				selectRender(this);
			});
		}
		
	}
	
	ainobug.$_codeTable={
			
			/**
			 * {
			 * 
			 * fnDatas:function(callback(datas){}){   初始化加载所有的CODETABLES
			 * },
			 * 
			 * fnData:function(type,callback){  如果在本地JS里面没有找到，会调用此API来获取CODE（单个CODE加载）
			 * },
			 * getType:function(data){  获取TYPE API
			 *		return data.desc;
			 *	},
			 *	getCode:function(data){  获取CODE API
			 *		return data.code;
			 *	},
			 *	getName:function(data){  获取NAME API
			 *		return data.value;
			 *	},
			 *	getDesc:function(data){  获取DESC API
			 *		return data.desc;
			 *	}
			 * }
			 * @param options
			 */
			codeTable:function(options){
				function callback(datas){
					ainobug.$_codeTable.load($.extend({},{data:datas},options));
				}
				options.fnDatas(callback);
			},
			/**
			 * {
						getType:function(data){
							return data.desc;
						},
						getCode:function(data){
							return data.code;
						},
						getName:function(data){
							return data.value;
						},
						getDesc:function(data){
							return data.desc;
						},
						data:{}
				}
			 * @param options
			 */
			load:function(options){
				let _defOpts={
						getType:function(data){
							return data.desc;
						},
						getCode:function(data){
							return data.code;
						},
						getName:function(data){
							return data.value;
						},
						getDesc:function(data){
							return data.desc;
						},
						data:{}
				}
				
				let finalOpts={};
				if(options){
					finalOpts=$.extend({},_defOpts,options);
				}
				else{
					finalOpts=$.extend({},_defOpts);
				}
				load(finalOpts);
			},
			getName:getName,
			/**
			 * draw($dom,fnFilter)
			 * fnFilter:function($dom,_code){
					return true;
				}
			 */
			draw:draw,
			
			/**
			 * 获取CODES - LIST ， 根据TYPE ， 此API 只会从本地JS缓存中拿
			 */
			getCodes:getCodes,
			
			defaultDraw:function($dom){
				ainobug.$_codeTable.draw($($dom).find('.codetable'));
			},

			val:function () {
				return codes.val();
            }
	}
	
	
	
})(window.ainobug)