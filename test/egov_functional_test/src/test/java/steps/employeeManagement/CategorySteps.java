package steps.employeeManagement;

import cucumber.api.java8.En;
import pages.employeeManagement.category.CategoryPage;
import steps.BaseSteps;

public class CategorySteps extends BaseSteps implements En {
    public CategorySteps() {

        And("^user will enter the category details for creation$", () -> {
            pageStore.get(CategoryPage.class).enterCategoryDetails();
        });
    }
}
