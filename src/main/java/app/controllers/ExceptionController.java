package app.controllers;


import app.exceptions.ApiException;
//import app.exceptions.Message;
import app.exceptions.Message;
import app.routes.Routes;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionController {
    // This class is responsible for handling exceptions (errors) in your app.


    // Creates a Logger instance called LOGGER.
    // LoggerFactory.getLogger(Routes.class) means:
    //   "When this logger writes messages, tag them with the class 'Routes'".
    // This is useful for debugging because you’ll see which part of the app logged something.
    private final Logger LOGGER = LoggerFactory.getLogger(Routes.class);

    public void apiExceptionHandler(ApiException e, Context ctx) {
        // A method that handles ApiException errors.
        // Parameters:
        // - e: the exception object that was thrown.
        // - ctx: the Javalin Context, giving access to request and response.

        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        // Logs the error with severity "error".
        // It tries to log:
        // - requestInfo: some attribute (probably set elsewhere) describing the request.
        // - ctx.res().getStatus(): the current HTTP response status.
        // - e.getMessage(): the error message from the exception.

        // Sets the HTTP response status to the status code inside the ApiException (e.g., 404, 400, etc.).
        ctx.status(e.getStatusCode());

        // Returns a JSON response to the client.
        // The JSON looks like: { "status": <code>, "message": "<error text>"
        ctx.json(new Message(e.getStatusCode(), e.getMessage()));
    }

    // A method that handles ANY kind of general exception (not just ApiException).
    // This is a "catch-all" for unexpected errors.
    public void exceptionHandler(Exception e, Context ctx) {
        // Logs the unexpected error with severity "error".
        // Logs the same info: request details, status code, and exception message.
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());

        // Sets the HTTP response status code to 500 (Internal Server Error),
        // since this is not a specific error we recognize.
        ctx.status(500);

        // Sends a JSON response with the error message to the client.
        // Example: { "status": 500, "message": "NullPointerException ..." }
        ctx.json(new Message(500, e.getMessage()));
    }

}

