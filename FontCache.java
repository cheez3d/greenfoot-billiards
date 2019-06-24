/**
 * Gestioneaza fonturile.
 * 
 * @version 2018-01-30
 */

import java.awt.Font;

import java.io.File;

import java.util.Map;
import java.util.HashMap;

public class FontCache {
    private static final String DIR = "fonts";
    private static final String[] EXTS = new String[]{"otf", "ttf"};
    
    private static final Map<String, Map<Integer, Map<Integer, Font>>> fonts = new HashMap<String, Map<Integer, Map<Integer, Font>>>();
    
    public static Font getFont(String name, int style, int size) throws Exception {
        Map<Integer, Map<Integer, Font>> sizes = fonts.get(name);
        if (sizes == null) { 
            sizes = new HashMap<Integer, Map<Integer, Font>>();
            fonts.put(name, sizes);
        }
        
        Map<Integer, Font> styles = sizes.get(size);
        if (styles == null) { 
            styles = new HashMap<Integer, Font>();
            sizes.put(size, styles);
        }
        
        Font font = styles.get(style);
        if (font == null) {
            for (int i = 0; i < EXTS.length; ++i) {
                File fontFile = new File(DIR + "\\" + name + "." + EXTS[i]);
                
                if (fontFile.exists() && fontFile.isFile()) {
                    Font baseFont;
                    
                    // daca nu exista inca niciun font salvat, creeaza unul pentru a putea folosi deriveFont pe el
                    if (styles.isEmpty()) {
                        // incearca sa creezi baseFont
                        try { baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile); }
                        catch (Exception e) { throw e; }
                        
                        styles.put(style, baseFont); // salveaza baseFont
                        
                        // daca criteriile pentru font sunt aceleasi ca cele ale baseFont, atunci returneaza baseFont, altfel deriva un font nou din baseFont
                        font = (baseFont.getStyle() == style && baseFont.getSize() == size) ? baseFont : baseFont.deriveFont(style, size);
                        
                    // acceseaza primul element din map pentru a putea folosi deriveFont pe el
                    } else {
                        baseFont = styles.values().iterator().next();
                        
                        font = baseFont.deriveFont(style, size);
                    }
                    
                    
                }
            }
            
            // daca fontul nu a putut fi gasit/creat din fisier, incercam cu fonturile built-in
            if (font == null) {
                font = new Font(name, style, size);
            }
            
            styles.put(style, font); // salveaza fontul
        }
        
        return font;
    }
            
    public static Font getFont(String name, int size) throws Exception {
        return getFont(name, Font.PLAIN, size);
    }
}
