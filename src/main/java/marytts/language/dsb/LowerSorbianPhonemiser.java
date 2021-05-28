package marytts.language.dsb;

import marytts.exceptions.MaryConfigurationException;

import java.io.IOException;

public class LowerSorbianPhonemiser extends marytts.modules.JPhonemiser {
    public LowerSorbianPhonemiser() throws IOException, MaryConfigurationException {
        super("dsb.");
    }
}
