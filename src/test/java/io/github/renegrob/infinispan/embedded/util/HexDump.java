package io.github.renegrob.infinispan.embedded.util;

import java.io.IOException;
import java.util.HexFormat;

public class HexDump {
    private static final int BYTES_PER_LINE = 16;
    private static final int BYTES_PER_BLOCK = 8;
    public static final int OFFSET_DIGITS = 8;
    public static final char NON_PRINTABLE_CHAR = '.';
    private String lineSeparator = System.lineSeparator();;

    public String toHexDump(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        appendHexDump(sb, bytes);
        return sb.toString();
    }

    /**
     * 00000000  89 50 4e 47 0d 0a 1a 0a  00 00 00 0d 49 48 44 52  |.PNG........IHDR|
     */
    public void appendHexDump(Appendable appendable, byte[] bytes) {
        try {
            HexFormat hexFormat = HexFormat.of();

            for (int offset = 0; offset <= bytes.length - BYTES_PER_LINE; offset += BYTES_PER_LINE) {
                appendable.append(hexFormat.toHexDigits(offset, OFFSET_DIGITS));
                appendable.append("  ");
                for (int i = 0; i < BYTES_PER_LINE; i++) {
                    if (i == BYTES_PER_BLOCK) {
                        appendable.append(' ');
                    }
                    hexFormat.toHexDigits(appendable, bytes[offset + i]);
                    appendable.append(' ');
                }
                appendable.append(" |");
                for (int i = 0; i < BYTES_PER_LINE; i++) {
                    char ch = (char) bytes[offset + i];
                    appendable.append(isPrintable(ch) ? ch : NON_PRINTABLE_CHAR);
                }
                appendable.append('|');
                appendable.append(lineSeparator);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isPrintable(char ch) {
        return ch >= 0x20 && ch < 0x7f;
    }
}
