package hello;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Request, Response> {
  public Response handleRequest(Request input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("*** Request handling function body entered. " + input.toString());
				logger.log("Added new kinds of functionality here");
		return new Response("Go Serverless v1.0! Your function executed successfully!", input);
	}

}
