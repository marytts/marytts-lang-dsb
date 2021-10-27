package marytts.language.dsb

import marytts.LocalMaryInterface
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import static marytts.datatypes.MaryDataType.TEXT
import static marytts.datatypes.MaryDataType.WORDS

class PreprocessIT {

    LocalMaryInterface mary

    @BeforeClass
    void setUp() {
        mary = new LocalMaryInterface()
        mary.locale = Locale.forLanguageTag('dsb')
        mary.inputType = TEXT
        mary.outputType = WORDS
    }

    @Test
    void 'Given input with real number, When text is converted to words, Then number is expanded correctly'() {
        def input = 'Konstanta π jo iracionalna a transcendentalna licba, kótaryž ma gódnotu wokoło 3,14159.'
        def expected = 'Konstanta pi jo iracionalna a transcendentalna licba, kótaryž ma gódnotu wokoło tśi koma jaden styri jaden pěś źewjeś.'
        def output = mary.generateXML(input)
        def outputStr = output.documentElement.serialize()
        def xmlSlurper = new XmlSlurper(false, false)
        def tokens = xmlSlurper.parseText(outputStr).depthFirst().findAll { it.name() == 't' }
        def actual = tokens.inject('') { result, token ->
            (result.isEmpty() || token ==~ /\p{Punct}/) ? result + token : result + ' ' + token
        }
        assert actual == expected
    }
    
    @Test
    void 'Given input with symbols, When text is converted to words, Then symbols are expanded correctly'() {
        def input = 'Bankowki eksistěruju za gódnoty 5, 10, 20, 50, 100, 200 a 500 €.'
        def expected = 'Bankowki eksistěruju za gódnoty pěś, źaseś, dwaźasća, połsta, sto, dwěsćě a pěśstow eurow.'
        def output = mary.generateXML(input)
        def outputStr = output.documentElement.serialize()
        def xmlSlurper = new XmlSlurper(false, false)
        def tokens = xmlSlurper.parseText(outputStr).depthFirst().findAll { it.name() == 't' }
        def actual = tokens.inject('') { result, token ->
            (result.isEmpty() || token ==~ /\p{Punct}/) ? result + token : result + ' ' + token
        }
        assert actual == expected
    }
}
