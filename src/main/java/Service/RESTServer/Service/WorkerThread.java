package Service.RESTServer.Service;

import Service.RESTServer.Service.Methods.IHTTPMethod;
import Service.RESTServer.Service.Request.IRequestContext;
import Service.RESTServer.Service.Request.RequestContext;
import Service.RESTServer.Service.Socket.IMySocket;
import lombok.AllArgsConstructor;

import java.io.*;
import java.util.List;

@AllArgsConstructor
public class WorkerThread implements Runnable {

    IMySocket s;
    List<IHTTPMethod> registeredMethods;

    @Override
    public void run() {
        IRequestContext requestContext;
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

            //read in Request
            requestContext = new RequestContext(reader);

            if (!requestContext.getHeaders().isEmpty()) {
                System.out.println(requestContext.formatedString());

                //Check supported Methods
                for (IHTTPMethod method : registeredMethods) {
                    if (method.analyse(requestContext)) {
                        method.exec(requestContext).sendResponse(writer);
                        break;
                    }
                }
            }


            writer.close();
            reader.close();
            // Close the socket itself
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
