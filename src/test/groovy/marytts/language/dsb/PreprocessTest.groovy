package marytts.language.dsb

import com.ibm.icu.text.NumberFormat
import com.ibm.icu.text.RuleBasedNumberFormat
import marytts.exceptions.MaryConfigurationException
import org.apache.commons.csv.CSVFormat
import org.testng.IExpectedExceptionsHolder
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class PreprocessTest {

    Preprocess preprocess

    @BeforeClass
    void setUp() {
        preprocess = new Preprocess()
    }

    @Test
    void testInitSymbolExpansion() {
        assert preprocess.symbols instanceof Map
    }

    @Test(expectedExceptions = MaryConfigurationException)
    void testInitSymbolExpansionWithInvalidResource() {
        preprocess.initSymbolExpansion('noSuchResource')
    }

    @DataProvider
    Object[][] symbols() {
        def stream = this.getClass().getResourceAsStream('symbols.csv')
        def reader = stream.newReader('UTF-8')
        def csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)
        return csvParser.collect { record ->
            [record.get('symbol'), record.get('expansion')]
        }
    }

    @Test(dataProvider = 'symbols')
    void testExpandSymbol(String input, String expected) {
        def actual = preprocess.expandSymbol(input);
        assert expected == actual
    }

    @Test
    void testExpandUnknownSymbol() {
        def expected = 'fnord'
        def actual = preprocess.expandSymbol(expected)
        assert expected == actual
    }

    @DataProvider
    Object[][] numberTokens() {
        [
                ['123', 123],
                ['3,14159', 3.14159],
                ['Fnord', null]
        ]
    }

    @Test
    void testInitNumberExpansion() {
        assert preprocess.ruleBasedNumberFormat instanceof RuleBasedNumberFormat
        assert preprocess.numberFormat instanceof NumberFormat
    }

    @Test(expectedExceptions = MaryConfigurationException)
    void testInitNumberExpansionWithInvalidResource() {
        preprocess.initNumberExpansion('noSuchResource')
    }

    @Test(dataProvider = 'numberTokens')
    void testParseNumber(String input, Number expected) {
        def actual = preprocess.parseNumber(input)
        assert expected == actual
    }

    @DataProvider
    Object[][] numbers() {
        def stream = this.getClass().getResourceAsStream('numbers.csv')
        def reader = stream.newReader('UTF-8')
        def csvParser = CSVFormat.DEFAULT.parse(reader)
        return csvParser.collect { record ->
            [new BigDecimal(record.get(0)), record.get(1)]
        }
    }

    @Test(dataProvider = 'numbers')
    void testSpelloutNumber(BigDecimal input, String expected) {
        def actual = preprocess.spelloutNumber(input)
        assert expected == actual
    }
}
