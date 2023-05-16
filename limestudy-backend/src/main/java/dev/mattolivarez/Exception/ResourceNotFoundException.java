/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Contains extended RuntimeException using ResponseStatus annontation
*/

package dev.mattolivarez.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
