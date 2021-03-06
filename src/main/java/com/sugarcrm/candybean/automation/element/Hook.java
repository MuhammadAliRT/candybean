/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.automation.element;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.openqa.selenium.By;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.MalformedHookException;
import com.thoughtworks.selenium.SeleniumException;

/**
 * A mechanism to locate an element on a page using a pre-defined hook string and a {@link Strategy}.
 * A collection of {@link Hook} objects can be obtained from a properties file that contains
 * all the hooks. The format of a hook is as follows: <br>
 * <b>key=strategy:hook</b> <br>where they key is a uniqe key assigned to the element, the strategy is the type of strategy
 * used to locate the element, and the hook is the value used by the strategy to locate the element.
 *
 */
public class Hook {
	
	public static final String HOOK_DELIMITER = ":";
	
	public enum Strategy { CSS, XPATH, ID, NAME, LINK, PLINK, CLASS, TAG; }
	public final Strategy hookStrategy;
	public final String hookString;

	public Hook(Strategy hookStrategy, String hookString) {
		this.hookStrategy = hookStrategy;
		this.hookString = hookString;
	}
	
	/**
	 * Returns a preloaded hashmap based on the given, formatted hooks (Properties) file.
	 * 
	 * @param hooks
	 * @return
	 * @throws MalformedHookException 
	 */
	public static Map<String, Hook> getHooks(Properties hooks) throws MalformedHookException {
		Map<String, Hook> hooksMap = new HashMap<String, Hook>();
		int i = 0;
		for(String name : hooks.stringPropertyNames()) {
			i++;
//			System.out.println("hook name: " + name);
//			String[] strategyNHook = hooks.getProperty(name).split(HOOK_DELIMITER);
			String[] strategyNHook = Configuration.getPlatformValue(hooks, name).split(HOOK_DELIMITER);
	//		System.out.println("strategy: " + strategyNHook[0] + ", hook: " + strategyNHook[1]);

			if (strategyNHook.length != 2) {
				throw new MalformedHookException(name);
			}else {
	//			System.out.println("key: " + name + ",    strategy: " + strategyNHook[0] + ",     hook: " + strategyNHook[1]);
				Strategy strategy = Hook.getStrategy(strategyNHook[0]);
				String hook = strategyNHook[1];
				hooksMap.put(name, new Hook(strategy, hook));
			}
		}
		return hooksMap;
	}
	
	/**
	 * Returns the Candybean-defined hook strategy based on the given string.
	 * 
	 * @param strategy
	 * @return
	 * @throws SeleniumException 
	 */
	public static Strategy getStrategy(String strategy) {
		switch(strategy) {
		case "CSS": return Strategy.CSS;
		case "ID": return Strategy.ID;
		case "NAME": return Strategy.NAME;
		case "XPATH": return Strategy.XPATH;
		case "LINK": return Strategy.LINK;
		case "PLINK": return Strategy.PLINK;
		case "CLASS": return Strategy.CLASS;
		case "TAG": return Strategy.TAG;
		default:
			throw new SeleniumException("Selenium: Strategy not recognized: " + strategy);
		}
	}

	/**
	 * A helper method to convert Hook to By
	 * @param hook	The hook that specifies a web element
	 * @return		The converted By
	 * @throws CandybeanException
	 */
	public static By getBy(Hook hook) throws CandybeanException {
		switch (hook.hookStrategy) {
			case CSS:
				return By.cssSelector(hook.hookString);
			case XPATH:
				return By.xpath(hook.hookString);
			case ID:
				return By.id(hook.hookString);
			case NAME:
				return By.name(hook.hookString);
			case LINK:
				return By.linkText(hook.hookString);
			case PLINK:
				return By.partialLinkText(hook.hookString);
			case CLASS:
				return By.className(hook.hookString);
			case TAG:
				return By.tagName(hook.hookString);
			default:
				throw new CandybeanException("Strategy type not recognized.");
		}
	}

	/**
	 * A helper method to convert Hook to By
	 * @return		The converted By
	 * @throws CandybeanException
	 */
	public By getBy() throws CandybeanException {
		return this.getBy(this);
	}
	
	public String toString() {
		return "VHook(" + this.getHookStrategy() + "," + this.getHookString() + ")";
	}

	public Strategy getHookStrategy() {
		return hookStrategy;
	}

	public String getHookString() {
		return hookString;
	}
}
