package server.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DeletedBeatmapAdvice {
	@ResponseBody
	@ExceptionHandler(DeletedBeatmapException.class)
	@ResponseStatus(HttpStatus.GONE)
	String handler(DeletedBeatmapException ex) {
		return ex.getMessage();
	}
}
