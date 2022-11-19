/**
 *
 * need put directly the modal dom under body level ,
 *
 * <div class="modal fade  ainobug-template-hide   "
 data-backdrop="static" data-keyboard="false" tabindex="-1"
 ainobug-template="modal"

 >
 <div class="modal-dialog  ainobug-modal-dialog    ">
 <div class="modal-content ainobug-modal-content ">

 <div class="modal-body " ainobug-attr="data-htmlUrl={url},data-layoutId={layoutId}"   >

 </div>

 <button type="button" class="close ainobug-modal-close " data-dismiss="modal" aria-label="Close">
 <svg width="2em" height="2em" viewBox="0 0 16 16" class="bi bi-x-circle" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
 <path fill-rule="evenodd" d="M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
 <path fill-rule="evenodd" d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
 </svg>
 </button>

 </div>

 </div>
 </div>
 *
 * Created by J on 2017/10/19.
 */
$.extend(window.ainobug.pageTemplate,{

    currentModal:function () {

        return $(this.root).parents('.ainobug-modal-dialog').parent();
    },

    currentModalSource: function () {
        return this.currentModal().data('modalSource');
    },

    closeModal: function () {
        $(this.currentModal()).modal('hide');
    },

    /**
     * modalOpts : {

					 * hidden :  // fn() 监听函数，当弹出页面关闭的时候能够获得通知
					 * source :  // 谁触发了这个弹出框 ， CSS SELECTOR

					 * }
     */
    openModal: function (url, params,modalOpts) {

        if(!modalOpts){
            modalOpts={}
        }

        let htmlUrl=url+'?param='+ainobug.util.json(params);

        let $dom=ainobug.mv.cloneHtml('[ainobug-template="modal"]',
            {
                url: htmlUrl,
                layoutId: new Date().getTime()
            });

        ainobug.layout.draw('body',$dom);

        $dom.modal({
            show: true,
            keyboard: false,
            backdrop: 'static'
        });

        if(modalOpts.hidden){
            $dom.data('modalHiddenFunc',modalOpts.hidden);
        }

        if(modalOpts.source){
            $dom.data('modalSource',modalOpts.source);
        }

        $dom.on('hidden.bs.modal', function (e) {
            try {
                if ($dom.data('modalHiddenFunc')) {
                    $dom.data('modalHiddenFunc')(e);
                }
            } catch (e) {
            }
            if ($dom.data('modalSkip')) {
                return;
            }
            let modalReturnFn = $dom.data('modalReturnFn');
            let result = [];
            if (modalReturnFn) {
                result[0] = modalReturnFn();
            }
            try {
                modalOpts.hidden(e, result);
            } catch (e) {
            }
            $dom.remove();
        });
    }
});

