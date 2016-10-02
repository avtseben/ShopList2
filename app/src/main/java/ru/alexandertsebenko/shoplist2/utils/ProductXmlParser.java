package ru.alexandertsebenko.shoplist2.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.alexandertsebenko.shoplist2.datamodel.ProdCategory;
import ru.alexandertsebenko.shoplist2.ui.adapter.ParentItem;

/**
 * Created by avtseben on 02.10.2016.
 */
public class ProductXmlParser {

    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(in,null);
            parser.nextTag();
            return readProductDictionary(parser);
        } finally {
            in.close();
        }
    }
    private List readProductDictionary(XmlPullParser parser) throws XmlPullParserException, IOException {
        List categories = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "products");
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("category")){
                categories.add(readCategory(parser));
            }
        }
        return categories;
    }
    private ProdCategory readCategory(XmlPullParser parser) throws XmlPullParserException, IOException{

        String catName = parser.getAttributeValue(null,"name");
        String catImage = parser.getAttributeValue(null,"image");
        List<String> productInCategory = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "category");
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("product")){
                productInCategory.add(readText(parser));
            }

        }
        return new ProdCategory(catName,catImage,productInCategory);
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if(parser.next() == XmlPullParser.TEXT){
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
