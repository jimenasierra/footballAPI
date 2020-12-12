package com.footballAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class LeagueAllReadyImportedException extends RuntimeException{

    public LeagueAllReadyImportedException(String message) {
        super(message);
    }

}
