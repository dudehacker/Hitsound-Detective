package server.model.exception;

public class InvalidUrlException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public InvalidUrlException(String url){
		super("Invalid url: " + url);
	}
}
