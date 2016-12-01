package org.arquillian.example;
/**
 * Created by hemani on 12/1/16.
 */

import java.io.File;
import java.net.URL;

import org.arquillian.extension.recorder.video.Recorder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class LoginScreenGrapheneTest {
    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "login.war")
                .addClasses(Credentials.class, User.class, LoginController.class)
                .addAsWebResource(new File(WEBAPP_SRC, "login.html"))
                .addAsWebResource(new File(WEBAPP_SRC, "home.html"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(
                        new StringAsset("<faces-config version=\"2.0\"/>"),
                        "faces-config.xml");

    }

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentUrl;

    @FindBy(id = "loginForm:userName")                                  // 2. injects an element
    private WebElement userName;

    @FindBy(id = "loginForm:password")
    private WebElement password;

    @FindBy(id = "loginForm:login")
    private WebElement loginButton;

    @FindBy(tagName = "li")                     // 2. injects a first element with given tag name
    private WebElement facesMessage;

    @FindByJQuery("p:visible")                  // 3. injects an element using jQuery selector
    private WebElement signedAs;

    @FindBy(css = "input[type=submit]")
    private WebElement whoAmI;


  //  @ArquillianResource
   // Recorder recorder;

    @Test
    public void should_login_successfully() {
      //  recorder.startRecording();

        browser.get(deploymentUrl.toExternalForm() + "login.jsf");      // 1. open the tested page

        userName.sendKeys("demo");                                      // 3. control the page
        password.sendKeys("demo");

        guardHttp(loginButton).click();                            // 1. synchronize full-page request
        assertEquals("Welcome", facesMessage.getText().trim());

        guardAjax(whoAmI).click();                                 // 2. synchronize AJAX request
        assertTrue(signedAs.getText().contains("demo"));

//        recorder.stopRecording();
    }
}
