import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/servlet-3-0-asynchronous-processing", asyncSupported = true)
public class Servlet30AsynchronousProcessing extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        final AsyncContext asyncContext = request.startAsync();
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                // notifies that a new asynchronous cycle is being initiated.
            }
            
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                // notifies that an asynchronous operation has been completed.
            }
            
            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                // notifies that an asynchronous operation has timed out.
                event.getAsyncContext().complete();
            }
            
            @Override
            public void onError(AsyncEvent event) throws IOException {
                // notifies that an asynchronous operation has failed to complete.
            }
        });

        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
                // read the input data from the request.

                HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
                // write the output data from the response.

                asyncContext.complete();
            }
        });
    }
}
