import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/async-servlet", asyncSupported = true)
public class ServletAsync extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        final AsyncContext asyncContext = request.startAsync();
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                // notify that a new asynchronous cycle is being initiated.
            }
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                // notify that an asynchronous operation has been completed.
            }
            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                // notify that an asynchronous operation has timed out.
                event.getAsyncContext().complete();
            }
            @Override
            public void onError(AsyncEvent event) throws IOException {
                // notify that an asynchronous operation has failed to complete.
            }
        });

        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
                // read from the request
                HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
                // write to the response
                asyncContext.complete();
            }
        });
    }
}
