package com.cqfc.servlet.async;

import java.io.IOException;
import java.util.Date;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/async", asyncSupported = true)
public class AsyncServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final AsyncContext asyncContext = req.startAsync(req, res);
        asyncContext.getResponse().getWriter().println("starting async task on " + new Date());
        asyncContext.start(new StockPriceRunner(asyncContext));
        res.getWriter().write("exiting doGet() on " + new Date() + "\r\n");
    }

    private class StockPriceRunner implements Runnable {
        AsyncContext asyncContext;
        
        public StockPriceRunner(AsyncContext asyncContext) {
            this.asyncContext = asyncContext;
        }

        @Override
        public void run() {
            try {
            	Thread.sleep(5000);
            	asyncContext.getResponse().getWriter().printf("async task completed on " + new Date());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
                asyncContext.complete();
            }
        }
    }

}
