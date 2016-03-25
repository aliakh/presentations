import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = {"/servlet-3-1-nonblockings-io"}, asyncSupported = true)
public class Servlet31NonBlockingIO extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        final AsyncContext context = request.startAsync();
        final ServletInputStream inputStream = request.getInputStream();

        inputStream.setReadListener(new ReadListener() {
            @Override
            public void onDataAvailable() throws IOException {
                int length = -1;
                byte data[] = new byte[1024];
                while (inputStream.isReady() && (length = inputStream.read(data)) != -1) {
                    // read the input data
                }
            }

            @Override
            public void onAllDataRead() {
                // invoked when all data for the current request has been read.
                context.complete();
            }

            @Override
            public void onError(Throwable t) {
                // invoked when an error occurs processing the request.
                context.complete();
            }
        });

        final ServletOutputStream output = response.getOutputStream();
        output.setWriteListener(new WriteListener() {
            @Override
            public void onWritePossible() throws IOException {
                if (output.isReady()) {
                    byte[] data = new byte[1024];

                    // write the output data
                    output.write(data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                // invoked when an error occurs writing data using the non-blocking APIs.
                context.complete();
            }
        });
    }
}
