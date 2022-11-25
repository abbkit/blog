package com.abbkit.project.model;


public class ResponseModel<T> implements Model{

	/**
	 * @see ResponseStatus#code
	 */
	private int code=0;

	/**
	 * @see ResponseStatus#desc
	 */
	private String status;

	/**
	 * the data for the subview if status is OK , message for showing if status is ERROR.
	 */
	private T data;

	public static ResponseModel newSuccess(Object data){
		ResponseModel responseModel=new ResponseModel();
		responseModel.status=ResponseStatus.SUCCESS.getDesc();
		responseModel.code=ResponseStatus.SUCCESS.getCode();
		responseModel.setData(data);
		return responseModel;
	}

	public static ResponseModel newMessage(Object data){
		ResponseModel responseModel=new ResponseModel();
		responseModel.status=ResponseStatus.MESSAGE.getDesc();
		responseModel.code=ResponseStatus.MESSAGE.getCode();
		responseModel.setData(data);
		return responseModel;
	}
	
	public static ResponseModel newError(Object data){
		ResponseModel responseModel=new ResponseModel();
		responseModel.status=ResponseStatus.FAIL.getDesc();
		responseModel.code=ResponseStatus.FAIL.getCode();
		responseModel.setData(data);
		return responseModel;
	}


	public boolean isSuccess(){
		return ResponseStatus.SUCCESS.getCode()==this.code;
	}



	//-------------------------------------------------------------------
	public void setData(T data) {
		this.data = data;
	}


	public T getData() {
		return data;
	}

	public int getCode() {
		return code;
	}

}

