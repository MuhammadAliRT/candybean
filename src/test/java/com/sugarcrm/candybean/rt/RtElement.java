package com.sugarcrm.candybean.rt;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.examples.sugar.Sugar;
import com.sugarcrm.candybean.examples.sugar.SugarUser;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public class RtElement 
{
	private LogInLogOut logInLogOut = new LogInLogOut();
	private WebDriverInterface iface; 
	private Sugar sugar;
	private Map<String,Hook> hooks;
	protected SugarUser adminUser;

	public void LogIn() throws Exception
	{
		sugar = logInLogOut.sugarLogin("admin", "123");
		initializeObjects();
	}
	
	private void initializeObjects()
	{
		iface = sugar.getIface();
		hooks = sugar.getHooks();
		adminUser = sugar.getAdminUser();
	}
	
	public void logout() throws Exception
	{
		logInLogOut.sugarLogout();
	}
	
	public void elementClick(String hookName) throws CandybeanException
	{
		iface.getWebDriverElement(hooks.get(hookName)).click();
	}
	
	public void sendKeys(String hookName, String txt) throws CandybeanException
	{
		iface.getWebDriverElement(hooks.get(hookName)).sendString(adminUser.getBuilder().getRequiredAttributes().get(txt));
	}
	
	public boolean isElementVisible(String hookName) throws CandybeanException
	{
		return iface.getWebDriverElement(hooks.get(hookName)).isDisplayed();
	}
	
	public String getText(String hookName) throws CandybeanException
	{
		return iface.getWebDriverElement(hooks.get(hookName)).getText();
	}
	
	public void waitForVisibility()
	{
		
	}
}
