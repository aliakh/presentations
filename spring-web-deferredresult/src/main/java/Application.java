import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@EnableAutoConfiguration
public class Application {

    private static final int THREADS_COUNT = 100;

    private static final int DELAY_MILLISECONDS = 100;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);

    private final Timer timer = new Timer();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/sync")
    public String getSync() throws Exception {
        Thread.sleep(DELAY_MILLISECONDS);
        return "sync";
    }

    @RequestMapping(value = "/async-blocking")
    public DeferredResult<String> getAsyncBlocking() {
        DeferredResult<String> deferred = new DeferredResult<>();

        executorService.submit(() -> {
            try {
                Thread.sleep(DELAY_MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            deferred.setResult("async-blocking");
        });

        return deferred;
    }

    @RequestMapping("/async-nonblocking")
    public DeferredResult<String> getAsyncNonBlocking() {
        final DeferredResult<String> deferred = new DeferredResult<>();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (deferred.isSetOrExpired()) {
                    throw new RuntimeException();
                } else {
                    deferred.setResult("async-nonblocking");
                }
            }
        }, DELAY_MILLISECONDS);

        return deferred;
    }

}