package market;

import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.util.ArrayList;
import java.util.List;

public class Hooks {

    static List<AssertionError> errorList = new ArrayList<>();

    @Before
    public void clearData() {
       errorList.clear();
    }

    @After
    public void checkData() {
        if(!errorList.isEmpty()){
            for (AssertionError assertionError : errorList) {
                assertionError.printStackTrace();
            }
        }
    }
}
