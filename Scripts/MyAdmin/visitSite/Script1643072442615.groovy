import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import org.openqa.selenium.Dimension
import org.openqa.selenium.Point
import org.openqa.selenium.chrome.ChromeDriver

import com.kazurayam.materialstore.Metadata
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil


import internal.GlobalVariable


// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(profile)
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

// check the GlobalVariables
assert GlobalVariable.URL != null, "GlobalVariable.URL is not defined"

System.setProperty("webdriver.chrome.driver", DriverFactory.getChromeDriverPath())
ChromeDriver chrome = new ChromeDriver()
chrome.manage().window().setPosition(new Point(0,0))
chrome.manage().window().setSize(new Dimension(1024, 800))

DriverFactory.changeWebDriver(chrome)
WebUI.navigateToUrl("${GlobalVariable.URL}")
// -------- The top page is now open --------------------------------------

URL url = new URL(WebUI.getUrl())

// take the screensho using the Katalon keyword, save it into the store
WebUI.callTestCase(findTestCase("MyAdmin/materializeScreenshot"),
	[
		"chrome": chrome,
		"store": store,
		"jobName": jobName,
		"jobTimestamp": jobTimestamp,
		"profile": profile
	]
)

// download the web resources (HTML, CSS and JavaScript) of the page, save them into the store
try {
	if (GlobalVariable.visitSite_by_Selenium4_CDT) {
		// using Selenium 4 + DevTools
		WebUI.callTestCase(findTestCase("MyAdmin/materializeCssJs_by_Selenium4_CDT"),
			[
				"chrome": chrome,
				"store": store,
				"jobName": jobName,
				"jobTimestamp": jobTimestamp,
				"profile": profile
			]
		)
	} else {
		// using kklisura's CDT support
		WebUI.callTestCase(findTestCase("MyAdmin/materializeCssJs_by_kklisura_CDT"),
			[
				"chrome": chrome,
				"store": store,
				"jobName": jobName,
				"jobTimestamp": jobTimestamp,
				"profile": profile
			]
		)
	}
} catch (Exception e) {
	KeywordUtil.markFailedAndStop(e.getMessage())
}

// we have done materializing the screenshot and the page source
WebUI.closeBrowser()
