package myTests;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.examples.sugar.Sugar;
import com.sugarcrm.candybean.examples.sugar.SugarUser;
import com.sugarcrm.candybean.examples.sugar.SugarUser.SugarUserBuilder;
import com.sugarcrm.candybean.utilities.Utils;

public class Login {
	private static Sugar sugar;
	
	@BeforeClass
	public static void first() throws Exception
	{
		Candybean candybean = getCandybean();
		Configuration sugarConfig = getSugarConfig();
		Properties sugarHooks = getSugarHooks();
		SugarUser adminUser = new SugarUserBuilder("Admin", "Conrad", "cwarmbold@sugarcrm.com", "310.993.2449", "123").build(); //asdf
		sugar = new Sugar(candybean, sugarConfig, sugarHooks, adminUser);

	}

	private static Candybean getCandybean() throws Exception 
	{
		String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY, Candybean.getDefaultConfigFile());
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		return Candybean.getInstance(candybeanConfig);
	}
	
	private static Configuration getSugarConfig() throws Exception 
	{
		String sugarConfigStr = System.getProperty("sugar.config", Candybean.ROOT_DIR + File.separator + "config" + File.separator + "sugar.config");
		return new Configuration(new File(Utils.adjustPath(sugarConfigStr)));
	}
	private static Properties getSugarHooks() throws Exception {

		String sugarHooksStr = System.getProperty("sugar.hooks", Candybean.ROOT_DIR + File.separator + "config" + File.separator + "sugar.hooks");
		Properties sugarHooks = new Properties();
		sugarHooks.load(new FileInputStream(new File(Utils.adjustPath(sugarHooksStr))));

		return sugarHooks;
	}
	
	@Test
	public void sugarLogin() throws Exception
	{
		sugar.login();
		TimeUnit.SECONDS.sleep(2);
		sugar.clickWelcomeButton();
	}
}
