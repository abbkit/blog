(function (ainobug) {

    function Ticket() {

        function getKey(){
            return ainobug.config.getTokenKey();
        }

        this.getTicket=function(){
            return Cookies.get(getKey());
        };

        this.setTicket=function(ticket){
            Cookies.set(getKey(),ticket);
        };

        this.removeTicket=function(){
            Cookies.remove(getKey());
        }
        
    }

    ainobug.ticket=new Ticket();


})(window.ainobug);