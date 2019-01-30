package server.model.exception;

public class DeletedBeatmapException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public DeletedBeatmapException(String url){
		super("This beatmap has been deleted: " + url);
	}
}
