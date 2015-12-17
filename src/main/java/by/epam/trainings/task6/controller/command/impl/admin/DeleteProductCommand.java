package by.epam.trainings.task6.controller.command.impl.admin;

import by.epam.trainings.task6.controller.command.ICommand;
import by.epam.trainings.task6.domain.Product;
import by.epam.trainings.task6.resource.ConfigurationManager;
import by.epam.trainings.task6.resource.MessageManager;
import by.epam.trainings.task6.service.ProductService;
import by.epam.trainings.task6.service.ServiceException;
import by.epam.trainings.task6.service.impl.ProductServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class DeleteProductCommand implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(DeleteProductCommand.class);
    private static final String PARAM_NAME_PRODUCT_ID = "id";
    private static final String PRODUCTS = "products";
    private static final String MESSAGE = "message";
    private static final String CATALOG_PAGE = "path.page.catalog";
    private static final String ERROR_PAGE = "path.page.error";
    private static final String URL = "url";

    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        int id = Integer.parseInt(request.getParameter(PARAM_NAME_PRODUCT_ID));
        ProductService productService = ProductServiceImpl.getInstance();
        try {
            productService.delete(id);
            List<Product> products = productService.allProducts();
            if (!products.isEmpty()) {
                request.getSession().setAttribute(PRODUCTS, products);
                page = ConfigurationManager.getProperty(CATALOG_PAGE);
                request.getSession().setAttribute(URL, page);
            } else {
                page = ConfigurationManager.getProperty(CATALOG_PAGE);
                request.setAttribute(MESSAGE, MessageManager.HAVE_NOT_PRODUCTS);
            }
        } catch (ServiceException e) {
            LOGGER.error("Error during deleting product: " + e);
            request.setAttribute(MESSAGE, MessageManager.DATABASE_ERROR);
            page = ConfigurationManager.getProperty(ERROR_PAGE);
        }
        return page;
    }
}
