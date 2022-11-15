package nextstep.mvc;

import static jakarta.servlet.http.HttpServletResponse.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapters = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);
        if (handler.isEmpty()) {
            response.setStatus(SC_NOT_FOUND);
            return;
        }
        final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
        handleRequest(request, response, handler, handlerAdapter);
    }

    private static void handleRequest(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object handler, final HandlerAdapter handlerAdapter) throws ServletException {
        try {
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.renderView(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
