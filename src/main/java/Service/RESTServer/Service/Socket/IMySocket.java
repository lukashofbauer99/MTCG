package Service.RESTServer.Service.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IMySocket {

    void close() throws IOException;

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;
}
