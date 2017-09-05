import org.openqa.selenium.firefox.FirefoxBinary
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile

//noinspection GroovyUnusedAssignment
reportsDir = "build/test-results/integrationTest/test-screenshots"

def developer = System.properties['user.name']
switch (developer) {
    case 'jbeutel':
        configureFirefox('/Applications/Firefox-38.5.2.esr.app/Contents/MacOS/firefox')
        break
    default:
        println "no webdriver configured for $developer; using default (caution: won't work with recent, e.g., Firefox 55)"
}

private void configureFirefox(String path) {
    def ffBinary = new FirefoxBinary(new File(path))
    driver = { new FirefoxDriver(ffBinary, new FirefoxProfile()) }
}
