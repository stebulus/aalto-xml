package async;

import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;

public class TestPIParsing extends AsyncTestBase
{
    public void testProcInstrParse() throws Exception
    {
        for (int spaces = 0; spaces < 3; ++spaces) {
            String SPC = spaces(spaces);
            _testPI(SPC, 1);
            _testPI(SPC, 2);
            _testPI(SPC, 3);
            _testPI(SPC, 5);
            _testPI(SPC, 11);
            _testPI(SPC, 999);
        }
    }

    public void testProcInstrSkip() throws Exception
    {
        for (int spaces = 0; spaces < 3; ++spaces) {
            String SPC = spaces(spaces);
            _testPISkip(SPC, 1);
            _testPISkip(SPC, 2);
            _testPISkip(SPC, 3);
            _testPISkip(SPC, 5);
            _testPISkip(SPC, 11);
            _testPISkip(SPC, 999);
        }
    }
    
    /*
    /**********************************************************************
    /* Helper methods
    /**********************************************************************
     */

    private final static String XML = "<?p    i ?><root><?pi \nwith\r\ndata??><?x \nfoo> "+UNICODE_SEGMENT+" bar? ?></root><?proc    \r?>";
    
    private void _testPI(String spaces, int chunkSize) throws Exception
    {
        AsyncXMLInputFactory f = new InputFactoryImpl();
        AsyncXMLStreamReader sr = f.createAsyncXMLStreamReader();
        AsyncReaderWrapper reader = new AsyncReaderWrapper(sr, chunkSize, spaces+XML);
        int t = verifyStart(reader);
        assertTokenType(PROCESSING_INSTRUCTION, t);
        assertEquals("p", sr.getPITarget());
        assertEquals("i ", sr.getPIData());
        assertTokenType(START_ELEMENT, reader.nextToken());
        assertEquals("root", sr.getLocalName());
        assertTokenType(PROCESSING_INSTRUCTION, reader.nextToken());
        assertEquals("pi", sr.getPITarget());
        assertEquals("with\ndata?", sr.getPIData());
        assertTokenType(PROCESSING_INSTRUCTION, reader.nextToken());
        assertEquals("x", sr.getPITarget());
        assertEquals("foo> "+UNICODE_SEGMENT+" bar? ", sr.getPIData());
        assertTokenType(END_ELEMENT, reader.nextToken());
        assertEquals("root", sr.getLocalName());
        assertTokenType(PROCESSING_INSTRUCTION, reader.nextToken());
        assertEquals("proc", sr.getPITarget());
        assertEquals("", sr.getPIData());
        assertTokenType(END_DOCUMENT, reader.nextToken());
    }

    private void _testPISkip(String spaces, int chunkSize) throws Exception
    {
        AsyncXMLInputFactory f = new InputFactoryImpl();
        AsyncXMLStreamReader sr = f.createAsyncXMLStreamReader();
        AsyncReaderWrapper reader = new AsyncReaderWrapper(sr, chunkSize, spaces+XML);
        int t = verifyStart(reader);
        assertTokenType(PROCESSING_INSTRUCTION, t);
        assertTokenType(START_ELEMENT, reader.nextToken());
        assertTokenType(PROCESSING_INSTRUCTION, reader.nextToken());
        assertTokenType(PROCESSING_INSTRUCTION, reader.nextToken());
        assertTokenType(END_ELEMENT, reader.nextToken());
        assertTokenType(PROCESSING_INSTRUCTION, reader.nextToken());
        assertTokenType(END_DOCUMENT, reader.nextToken());
        assertFalse(sr.hasNext());
    }
}
