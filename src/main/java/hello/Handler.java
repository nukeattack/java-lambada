package hello;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Handler implements RequestHandler<Request, Response> {

	@Override
	public Response handleRequest(Request input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("*** Request handling function body entered. " + input.toString());
		return new Response("Go Serverless v1.0! Your function executed successfully!", input);
	}

}
